package com.switchfully.eurder_db.service;


import com.switchfully.eurder_db.dto.CreateOrderDto;
import com.switchfully.eurder_db.dto.CreateOrderLineDto;
import com.switchfully.eurder_db.dto.OrderDto;
import com.switchfully.eurder_db.dto.ReOrderDto;
import com.switchfully.eurder_db.entity.Customer;
import com.switchfully.eurder_db.entity.Item;
import com.switchfully.eurder_db.entity.Order;
import com.switchfully.eurder_db.entity.OrderLine;
import com.switchfully.eurder_db.exception.*;
import com.switchfully.eurder_db.mapper.OrderLineMapper;
import com.switchfully.eurder_db.mapper.OrderMapper;
import com.switchfully.eurder_db.repository.ItemRepository;
import com.switchfully.eurder_db.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    public static final int SHIPPING_DAYS_IN_STOCK = 1;
    public static final int SHIPPING_DAYS_NOT_IN_STOCK = 7;

    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
    private final OrderLineMapper orderLineMapper;
    private final OrderRepository orderRepository;

    public OrderService(ItemRepository itemRepository, OrderMapper orderMapper, OrderLineMapper orderLineMapper, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderMapper = orderMapper;
        this.orderLineMapper = orderLineMapper;
        this.orderRepository = orderRepository;
    }



    public OrderDto placeOrder(Customer customer, CreateOrderDto createOrderDto) throws NoOrderLinesException, InvalidAmountInOrderInOrderLineException, UnknownItemIdException {
        if (createOrderDto.getOrderLines().isEmpty()) {
            throw new NoOrderLinesException();
        }

        Order order = createOrder(customer, createOrderDto);
        updateStock(order);

        return orderMapper.orderToOrderDto(order);
    }

    public OrderDto reOrder(Customer customer, Long id, ReOrderDto reOrderDto) throws NoOrderLinesException, InvalidAmountInOrderInOrderLineException, UnknownItemIdException {
        Order originalOrder = orderRepository.findById(id).orElseThrow(UnknownOrderIdException::new);
        CreateOrderDto createOrderDto = orderMapper.reOrderDtoToCreateOrderDto(originalOrder, reOrderDto);

        Order order = createOrder(customer, createOrderDto);
        updateStock(order);

        return orderMapper.orderToOrderDto(order);
    }

    private Order createOrder(Customer customer, CreateOrderDto createOrderDto) throws InvalidAmountInOrderInOrderLineException, UnknownItemIdException {
        List<OrderLine> orderLines = createOrderLines(createOrderDto);

        return orderRepository.save(new Order(customer.getId(), customer.getAddress(), createOrderDto.getOrderDate(), orderLines));
    }

    private List<OrderLine> createOrderLines(CreateOrderDto createOrderDto) throws InvalidAmountInOrderInOrderLineException, UnknownItemIdException {
        return createOrderDto.getOrderLines().stream()
                .map(createOrderLineDto -> createOrderLine(createOrderDto, createOrderLineDto))
                .toList();
    }

    private OrderLine createOrderLine(CreateOrderDto createOrderDto, CreateOrderLineDto createOrderLineDto) throws InvalidAmountInOrderInOrderLineException, UnknownItemIdException {
        if (createOrderLineDto.getAmountInOrder() < 1) {
            throw new InvalidAmountInOrderInOrderLineException();
        }

        Item item = itemRepository.findById(createOrderLineDto.getItemId()).orElseThrow(UnknownItemIdException::new);
        LocalDate shippingDate = getShippingDate(item, createOrderLineDto.getAmountInOrder(), createOrderDto.getOrderDate());

        return orderLineMapper.createOrderLineDtoToOrderLine(item, createOrderLineDto, shippingDate);
    }

    private LocalDate getShippingDate(Item item, Integer amountInOrder, LocalDate orderDate) {
        return orderDate.plusDays(item.getAmountInStock() >= amountInOrder ? SHIPPING_DAYS_IN_STOCK : SHIPPING_DAYS_NOT_IN_STOCK);
    }

    private void updateStock(Order order) throws UnknownItemIdException {
        order.getOrderLines().forEach(orderLine -> {
            Item item = itemRepository.findById(orderLine.getItemId()).orElseThrow(UnknownItemIdException::new);
            item.setAmountInStock(item.getAmountInStock() - orderLine.getAmountInOrder());

            itemRepository.save(item);
        });
    }



    public void validateOrderByIdForCustomer(Customer customer, Long id) throws UnknownOrderIdException, OrderIsNotForCustomerException {
        Order order = orderRepository.findById(id).orElseThrow(UnknownOrderIdException::new);
        if (!order.getCustomerId().equals(customer.getId())) {
            throw new OrderIsNotForCustomerException();
        }
    }

    public OrderDto findById(Long id) throws UnknownOrderIdException {
        Order order = orderRepository.findById(id).orElseThrow(UnknownOrderIdException::new);

        return orderMapper.orderToOrderDto(order);
    }

    public List<OrderDto> findAllOrdersForCustomer(Customer customer) {
        return orderRepository.findAllByCustomerId(customer.getId()).stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList());
    }

    public List<OrderDto> findAllOrdersForShippingDate(LocalDate shippingDate) {
        return orderRepository.findAllByOrderLines_shippingDate(shippingDate).stream()
                .map(orderMapper::orderToOrderDto)
                .map(orderDto -> new OrderDto(
                        orderDto.getId(),
                        orderDto.getCustomerId(),
                        orderDto.getCustomerAddress(),
                        orderDto.getOrderLines().stream()
                                .filter(orderLineDto -> orderLineDto.getShippingDate().equals(shippingDate))
                                .collect(Collectors.toList()),
                        orderDto.getOrderDate(),
                        orderDto.getTotalPrice()
                ))
                .collect(Collectors.toList());
    }
}
