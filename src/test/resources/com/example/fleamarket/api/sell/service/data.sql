insert into "user"
(id, name, email, idp_user_id) values
('u01', 'uname01', 'u01@example.com', 'idp01');

insert into sell
(id, user_id, product_name, description, price, sell_date_time) values
('s01', 'u01', 'pname01', 'desc01', 1000, '2025-02-02T10:10:10'),
('s02', 'u01', 'pname02', 'desc02', 2000, '2025-02-02T10:10:10');

insert into product_image
(id, sell_id) values
('pi01', 's02');

insert into product_image_data
(id, product_image_id, data) values
('pid01', 'pi01', null);
