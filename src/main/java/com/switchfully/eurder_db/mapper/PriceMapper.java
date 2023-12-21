package com.switchfully.eurder_db.mapper;

import com.switchfully.eurder_db.dto.CreatePriceDto;
import com.switchfully.eurder_db.dto.PriceDto;
import com.switchfully.eurder_db.dto.UpdatePriceDto;
import com.switchfully.eurder_db.entity.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {
    public Price createPriceDtoToPrice(CreatePriceDto createPriceDto) {
        return new Price(createPriceDto.getAmount(), createPriceDto.getCurrency());
    }

    public Price updatePriceDtoToPrice(UpdatePriceDto updatePriceDto) {
        return new Price(updatePriceDto.getAmount(), updatePriceDto.getCurrency());
    }

    public PriceDto priceToPriceDto(Price price) {
        return new PriceDto(price.getAmount(), price.getCurrency());
    }
}
