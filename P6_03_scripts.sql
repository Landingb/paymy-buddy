create database paymybuddy;

use paymybuddy;


CREATE TABLE IF NOT EXISTS `role` (
  `id` INT UNSIGNED UNSIGNED NOT NULL AUTO_INCREMENT,
  `rolename` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;


INSERT INTO `role` (`id`, `rolename`) VALUES
	(1, 'USER'),
	(2, 'ADMIN');


CREATE TABLE IF NOT EXISTS `user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `firstname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  `lastname` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  `inscriptionDateTime` datetime NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `bankaccountnumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;


CREATE TABLE IF NOT EXISTS `transactions_bank` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT UNSIGNED NOT NULL,
  `bankaccountnumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
  `datetime` datetime NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_user_id` (`user_id`) USING BTREE,
  CONSTRAINT `FK_transactions_bank_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;


CREATE TABLE IF NOT EXISTS `transactions_user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usersource_id` INT UNSIGNED NOT NULL,
  `userdestination_id` INT UNSIGNED NOT NULL,
  `dateTime` datetime NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `currency` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL,
  `fees` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_tu_userdestinationid` (`userdestination_id`) USING BTREE,
  KEY `FK_tu_usersourceid` (`usersource_id`) USING BTREE,
  CONSTRAINT `FK_transactions_user_paymybuddy.user` FOREIGN KEY (`userdestination_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_transactions_user_paymybuddy.user_2` FOREIGN KEY (`usersource_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;


CREATE TABLE IF NOT EXISTS `user_connections` (
  `user_id` INT UNSIGNED NOT NULL,
  `connection_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`,`connection_id`) USING BTREE,
  KEY `FK_userconnections_connectionid` (`connection_id`) USING BTREE,
  CONSTRAINT `FK_user_connections_paymybuddy.user` FOREIGN KEY (`connection_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_connections_paymybuddy.user_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;


CREATE TABLE IF NOT EXISTS `user_roles` (
  `users_id` INT UNSIGNED NOT NULL,
  `roles_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`users_id`,`roles_id`) USING BTREE,
  KEY `fk_roles_id` (`roles_id`) USING BTREE,
  CONSTRAINT `FK_user_roles_paymybuddy.role` FOREIGN KEY (`roles_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_user_roles_paymybuddy.user` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

INSERT INTO `user` (`firstname`, `lastname`, `email`, `inscriptionDateTime`, `password`, `enabled`, `bankaccountnumber`, `amount`, `currency`) VALUES
("Anthony", "Barré", "barre@gmail.com", '2021-10-16', "$2y$10$ykswHhdaWgVH/WsJrphRAOYzD.kuiO9BVQ41OyzojmBA.58gAjUaK", 1, 589323, 0.00, "EUR"),
("Michael", "Anthony", "anthony@gmail.com", '2021-10-17', "$2y$10$YMKlqX3JKwZgzBSJ45/6J.ywm08PCr.2SxC1jfJ1woR1/9q6FV3iC", 1, 232454, 0.00, "EUR");


