drop database tictactoe; 
CREATE DATABASE tictactoe CHARACTER SET utf8 COLLATE utf8_general_ci;
use tictactoe;
create table if not exists `account`(
	accID int primary key auto_increment,
    `username` varchar(255),
    `password` varchar(255),
    `name` varchar(255),
    `point` float
);
create table if not exists `game`(
	gameID int primary key auto_increment,
    accID1 int,
    accID2 int,
    result int,
	foreign  key (accID1) references `account`(accID),
	foreign  key (accID2) references `account`(accID)
);
create table if not exists `move`(
	moveID int primary key auto_increment,
    gameID int,
    x int,
    y int,
    accID int,
	foreign  key (accID) references `account`(accID),
	foreign  key (gameID) references `game`(gameID)
);
insert into `account`(`username`, `password`, `name`, `point`) values('player1', '123456', 'Kien', 0);
select * from game;