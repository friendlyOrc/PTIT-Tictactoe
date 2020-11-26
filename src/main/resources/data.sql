
delete from `account`;
delete from `game`;
delete from `move`;

insert into `account`(`username`, `password`, `name`, `point`, `status`) values('player1', '123456', 'Kien', 0, 0);
insert into `account`(`username`, `password`, `name`, `point`, `status`) values('player2', '123456', 'NotKien', 1.0, 0);