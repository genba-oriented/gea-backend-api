insert into "user"
(id, name, email, idp_user_id) values
('u01', 'uname01', 'u01@example.com', 'idp01'),
('u02', 'uname02', 'u02@example.com', 'idp02'),
('u03', 'uname03', 'u03@example.com', 'idp03');

insert into sell
(id, user_id, product_name, description, price, sell_date_time) values
('s01', 'u01', 'pname01', 'desc01', 1000, '2025-02-02T10:10:10'),
('s02', 'u02', 'pname02', 'desc02', 1000, '2025-02-02T10:10:10'),
('s03', 'u01', 'pname03', 'desc03', 1000, '2025-02-02T10:10:10');

insert into review
(id, sell_id, as_buyer, reviewee_user_id, reviewer_user_id, score, comment) values
('r01', 's01', true, 'u01', 'u03', 5, 'good!'),
('r02', 's01', false, 'u03', 'u01', 5, 'good!'),
('r03', 's02', true, 'u01', 'u02', 1, 'bad!'),
('r04', 's03', true, 'u01', 'u03', 5, 'good!');
