insert into "user"
(id, name, email, idp_user_id, shipping_address) values
('u01', 'uname01', 'u01@example.com', 'idp01', 'address01'),
('u02', 'uname02', 'u02@example.com', 'idp02', 'address02'),
('u51', 'uname51', 'u51@example.com', 'idp51', 'address03'),
('u52', 'uname52', 'u52@example.com', 'idp52', 'address04');

insert into sell
(id, user_id, product_name, description, price, sell_date_time, status) values
('s01', 'u01', 'pname01', 'desc01', 1000, '2025-02-02T10:10:10', 'COMPLETED'),
('s02', 'u02', 'pname02', 'desc02', 1000, '2025-02-02T10:10:10', 'NEED_SHIPPING'),
('s03', 'u01', 'pname03', 'desc03', 1000, '2025-02-02T10:10:10', 'COMPLETED'),
('s04', 'u02', 'pname04', 'desc04', 1000, '2025-02-02T10:10:10', 'COMPLETED'),
('s05', 'u02', 'pname05', 'desc05', 1000, '2025-02-02T10:10:10', 'NOW_SELLING');

insert into buy
(id, sell_id, user_id, buy_date_time) values
('b01', 's01', 'u51', '2025-01-01T10:10:10'),
('b02', 's02', 'u51', '2025-03-01T10:10:10'),
('b03', 's03', 'u52', '2025-01-01T10:10:10'),
('b04', 's04', 'u51', '2025-02-01T10:10:10');
