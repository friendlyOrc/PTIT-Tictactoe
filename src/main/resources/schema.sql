create table if not exists `account`(
	id int primary key auto_increment,
    `username` varchar(255),
    `password` varchar(255),
    `name` varchar(255),
    `point` float,
    `status` int
);
create table if not exists `game`(
	id int primary key auto_increment,
    acc1_id int,
    acc2_id int,
    result int,
	foreign  key (acc1_id) references `account`(id),
	foreign  key (acc2_id) references `account`(id)
);