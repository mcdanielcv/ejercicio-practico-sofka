CREATE DATABASE IF NOT EXISTS microservicios_db;

use microservicios_db;

CREATE TABLE IF NOT EXISTS client (
  `client_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `identification` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `state` tinyint(1) NOT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `client` (`name`,`gender`,`identification`,`edad`,`address`,`phone`,`password`,`state`) VALUES ('Jose Lema',NULL,NULL,0,'Otavalo sn y principal ','098254785','1234',1);
INSERT INTO `client` (`name`,`gender`,`identification`,`edad`,`address`,`phone`,`password`,`state`) VALUES ('Marianela Montalvo',NULL,NULL,0,'Amazonas y  NNUU','097548965','5678 ',1);
INSERT INTO `client` (`name`,`gender`,`identification`,`edad`,`address`,`phone`,`password`,`state`) VALUES ('Juan Osorio',NULL,NULL,0,'13 junio y Equinoccial','098874587','1245',1);

CREATE TABLE IF NOT EXISTS account  (
  `account_number` bigint NOT NULL,
  `client_id` bigint NOT NULL,
  `state` tinyint(1) NOT NULL,
  `initial_balance` double NOT NULL,
  `available_balance` double NOT NULL,
  `account_type` varchar(255) NOT NULL,
  PRIMARY KEY (`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `account` (`account_number`,`client_id`,`state`,`initial_balance`,`tipo_account`,`available_balance`) VALUES (225487,2,'1',700,'Corriente',700);
INSERT INTO `account` (`account_number`,`client_id`,`state`,`initial_balance`,`tipo_account`,`available_balance`) VALUES (478758,1,'1',1425,'Ahorro',1425);
INSERT INTO `account` (`account_number`,`client_id`,`state`,`initial_balance`,`tipo_account`,`available_balance`) VALUES (495878,3,'1',150,'Ahorro',150);
INSERT INTO `account` (`account_number`,`client_id`,`state`,`initial_balance`,`tipo_account`,`available_balance`) VALUES (496825,2,'1',0,'Ahorro',0);
INSERT INTO `account` (`account_number`,`client_id`,`state`,`initial_balance`,`tipo_account`,`available_balance`) VALUES (585545,1,'1',1000,'Corriente',1000);

CREATE TABLE IF NOT EXISTS transaction  (
  `num_transaccion` bigint NOT NULL AUTO_INCREMENT,
  `account_number` bigint NOT NULL,
  `type_movement` varchar(255) NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `value` double NOT NULL,
  `balance` double NOT NULL,
  PRIMARY KEY (`num_transaccion`),
  CONSTRAINT UC_Usuario UNIQUE (account_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `transaction` (`account_number`,`type_movement`,`transaction_date`,`value`,`balance`) VALUES (496825,'Retiro','2022-02-08 00:00:00.000000',-540,0);
INSERT INTO `transaction` (`account_number`,`type_movement`,`transaction_date`,`value`,`balance`) VALUES (478758,'Retiro','2022-02-10 00:00:00.000000',-575,1425);
INSERT INTO `transaction` (`account_number`,`type_movement`,`transaction_date`,`value`,`balance`) VALUES (225487,'Deposito','2022-02-11 00:00:00.000000',600,700);
INSERT INTO `transaction` (`account_number`,`type_movement`,`transaction_date`,`value`,`balance`) VALUES (495878,'Deposito','2022-02-12 00:00:00.000000',150,150);