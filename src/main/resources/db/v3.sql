--liquibase formatted sql

--changeset admin:3

CREATE TABLE `merchant_verification` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`merchant_id` bigint(20) NOT NULL,
	`name` varchar(50) NOT NULL,
	`type` varchar(50) NOT NULL,
	`status` varchar(50) NOT NULL,
	`number` varchar(50) NOT NULL,
	`photo` varchar(255) NOT NULL,
	`contact` varchar(50),
	`phone` varchar(50),
	`address` varchar(255),
	`description` varchar(255),
	`reason` varchar(255),
	`create_by` varchar(50) NOT NULL,
	`update_by` varchar(50) NOT NULL,
	`create_time` datetime NOT NULL,
	`update_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE `idx_Merchant_Verification_merchant_id` (`merchant_id`),
	INDEX `idx_Merchant_Verification_name` (`name`),
	INDEX `idx_Merchant_Verification_type` (`type`),
	INDEX `idx_Merchant_Verification_status` (`status`)
);
