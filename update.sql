alter table if exists admin alter column id set data type bigint;
create table item (id bigserial not null, amount_in_stock integer, description varchar(255), name varchar(255), price_id bigint, primary key (id));
create table price (id bigserial not null, amount float(53), currency varchar(255) check (currency in ('EUR')), primary key (id));
alter table if exists item drop constraint if exists UK_57dc0hho1ryh6aqftv96bgp3r;
alter table if exists item add constraint UK_57dc0hho1ryh6aqftv96bgp3r unique (price_id);
alter table if exists item add constraint FKmmkymx95re1lfh7jt7v6kimcj foreign key (price_id) references price;
