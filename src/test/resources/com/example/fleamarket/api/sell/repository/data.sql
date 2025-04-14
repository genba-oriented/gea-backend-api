insert into "user"
(id, name, email, idp_user_id) values
('u01', 'uname01', 'u01@example.com', 'idp01'),
('u02', 'uname02', 'u02@example.com', 'idp02');

insert into sell
(id, user_id, product_name, description, price, sell_date_time) values
('s01', 'u01', 'pname01', 'desc01', 1000, '2025-02-02T10:10:10'),
('s02', 'u02', 'pname02', 'desc02', 1000, '2025-02-02T10:10:10'),
('s03', 'u01', 'pname03', 'desc03', 1000, '2025-02-02T10:10:10'),
('s04', 'u02', 'pfoo04', 'desc04', 1000, '2025-02-02T10:10:10'),
('s05', 'u02', 'pname06', 'desc05', 1000, '2025-02-13T10:10:10'),
('s06', 'u02', 'pname06', 'desc06', 1000, '2025-02-14T10:10:10'),
('s07', 'u02', 'pname07', 'desc07', 1000, '2025-02-15T10:10:10'),
('s08', 'u02', 'pfoo08', 'desc08', 1000, '2025-02-02T10:10:10'),
('s09', 'u02', 'pname09', 'desc09', 1000, '2025-03-02T10:10:10'),
('s10', 'u02', 'pfoo10', 'desc10', 1000, '2025-04-02T10:10:10'),
('s11', 'u02', 'pname11', 'desc11', 1000, '2025-02-02T10:10:10');

insert into product_image
(id, sell_id) values
('pi01', 's01'),
('pi02', 's01');


