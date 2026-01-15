create table if not exists merchant_category
(
    id           bigint auto_increment primary key,
    name_eng     varchar(255) not null,

    constraint unq__category_name unique (name_eng)
);

create table if not exists merchant(
    id                     bigint auto_increment primary key,
    category_id            bigint       not null,
    name                   varchar(255) not null,
    description_eng        varchar(255) not null,

    constraint fk__merchant_category foreign key (category_id) references merchant_category (id)
);

create index idx__merchant_category on merchant (category_id);

create table if not exists shop(
    id                     bigint auto_increment primary key,
    merchant_id            bigint       not null,
    name                   varchar(255) not null,
    city                   varchar(128) null,
    district               varchar(128) default null,
    street                 varchar(255) null,
    building               varchar(12)  default null,
    latitude               double       null,
    longitude              double       null,

    constraint fk__shop_merchant foreign key (merchant_id) references merchant (id)
);

create index idx__shop_merchant_id on shop (merchant_id);
