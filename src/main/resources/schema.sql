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
    accID1 int,
    accID2 int,
    result int,
	foreign  key (accID1) references `account`(id),
	foreign  key (accID2) references `account`(id)
);
create table if not exists `move`(
	moveID int primary key auto_increment,
    gameID int,
    x int,
    y int,
    accID int,
	foreign  key (accID) references `account`(id),
	foreign  key (gameID) references `game`(id)
);
