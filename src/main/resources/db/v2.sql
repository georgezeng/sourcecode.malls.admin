--liquibase formatted sql

--changeset admin:2

CREATE TABLE `merchant_user` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`username` varchar(50) NOT NULL,
	`password` varchar(255) NOT NULL,
	`email` varchar(50),
	`mobile` varchar(50),
	`header` varchar(255),
	`enabled` bit(1) NOT NULL,
	`create_by` varchar(50) NOT NULL,
	`update_by` varchar(50) NOT NULL,
	`create_time` datetime NOT NULL,
	`update_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `idx_merchant_User_username` (`username`),
	INDEX `idx_merchant_User_email` (`email`),
	INDEX `idx_merchant_User_mobile` (`mobile`)
);

CREATE TABLE `merchant_role` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(50) NOT NULL,
	`name` varchar(255) NOT NULL,
	`description` varchar(50),
	`create_by` varchar(50) NOT NULL,
	`update_by` varchar(50) NOT NULL,
	`create_time` datetime NOT NULL,
	`update_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `idx_merchant_Role_code` (`code`),
	INDEX `idx_merchant_Role_name` (`name`)
);

CREATE TABLE `merchant_user_role` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_id` bigint(20) NOT NULL,
	`role_id` bigint(20) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `idx_merchant_User_Role_user_id_role_id` (`user_id`, `role_id`)
);

CREATE TABLE `merchant_authority` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`code` varchar(50) NOT NULL,
	`name` varchar(255) NOT NULL,
	`link` varchar(255) NOT NULL,
	`method` varchar(255),
	`description` varchar(50),
	`create_by` varchar(50) NOT NULL,
	`update_by` varchar(50) NOT NULL,
	`create_time` datetime NOT NULL,
	`update_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `idx_merchant_Authority_code` (`code`),
	INDEX `idx_merchant_Authority_name` (`name`)
);

CREATE TABLE `merchant_role_authority` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`authority_id` bigint(20) NOT NULL,
	`role_id` bigint(20) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `idx_merchant_Role_Authority_authority_id_role_id` (`authority_id`, `role_id`),
	INDEX `idx_merchant_Role_Authority_role_id` (`role_id`)
);