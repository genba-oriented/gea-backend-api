drop table if exists review;
drop table if exists buy;
drop table if exists product_image_data;
drop table if exists product_image;
drop table if exists sell;
drop table if exists "user";

create table "user" (
    id varchar(100) primary key,
    name varchar(100),
    email varchar(100),
    idp_user_id varchar(100),
    balance integer,
    shipping_address varchar(100),
    activated boolean,
    unique(idp_user_id)
);

create table sell (
    id varchar(100) primary key,
    user_id varchar(100),
    product_name varchar(100),
    description varchar(500),
    price integer,
    sell_date_time timestamp,
    edit_date_time timestamp,
    shipped_date_time timestamp,
    completed_date_time timestamp,
    status varchar(50),
    constraint fk_sell_user foreign key(user_id) references "user"(id)
);

create table product_image (
    id varchar(100) primary key,
    sell_id varchar(100),
    "order" integer,
    constraint fk_product_image_sell foreign key(sell_id) references sell(id)
);
create table product_image_data (
    id varchar(100) primary key,
    product_image_id varchar(100),
    data bytea,
    unique(product_image_id),
    constraint fk_product_image_data_product_image foreign key(product_image_id) references product_image(id)
);

create table buy (
    id varchar(100) primary key,
    sell_id varchar(100),
    user_id varchar(100),
    buy_date_time timestamp,
    unique(sell_id),
    constraint fk_buy_sell foreign key(sell_id) references sell(id),
    constraint fk_buy_user foreign key(user_id) references "user"(id)
);

create table review (
    id varchar(100) primary key,
    sell_id varchar(100),
    as_buyer boolean,
    reviewee_user_id varchar(100),
    reviewer_user_id varchar(100),
    score integer,
    comment varchar(200),
    unique(sell_id, reviewee_user_id),
    unique(sell_id, reviewer_user_id),
    constraint fk_reviewee_user foreign key(reviewee_user_id) references "user"(id),
    constraint fk_reviewer_user foreign key(reviewer_user_id) references "user"(id)
);

