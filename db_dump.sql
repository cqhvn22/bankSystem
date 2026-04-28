-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: mbbank_db
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account_registration_requests`
--

DROP TABLE IF EXISTS `account_registration_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_registration_requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `cccd` varchar(255) NOT NULL,
  `desired_username` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `loaitk` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ngay_sinh` date DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `reject_reason` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `reviewed_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `submitted_at` datetime(6) DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  `reviewed_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k6knnvppy0rs9vel95lp99k8t` (`cccd`),
  UNIQUE KEY `UK_btt2avcuy8swm7f06wc8y9rgp` (`desired_username`),
  KEY `FK5s56lx3myjif1ukudk679q9uk` (`branch_id`),
  KEY `FKfedh7lfxnl6ufnphqpwo04ors` (`reviewed_by`),
  CONSTRAINT `FK5s56lx3myjif1ukudk679q9uk` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `FKfedh7lfxnl6ufnphqpwo04ors` FOREIGN KEY (`reviewed_by`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_registration_requests`
--

LOCK TABLES `account_registration_requests` WRITE;
/*!40000 ALTER TABLE `account_registration_requests` DISABLE KEYS */;
INSERT INTO `account_registration_requests` VALUES (1,'fsfwe','43143141312','0359217469',NULL,'fwfwe','THANH_TOAN',NULL,'123','0359217462',NULL,'2026-04-27 09:12:40.000000','APPROVED','2026-04-27 09:12:24.000000',1,5),(2,'Hà Nội','001206036716','0359217470',NULL,'CHU QUANG HUNG','THANH_TOAN',NULL,'Cqhvn22072006.','0359217470',NULL,'2026-04-28 03:08:57.000000','APPROVED','2026-04-28 02:19:55.000000',1,5),(3,'Thái Hòa - Ba Vì','001206036711','0359217465',NULL,'CHU QUANG HUNG','THANH_TOAN',NULL,'Cqhvn22072006.','0359217465',NULL,'2026-04-28 03:18:16.000000','APPROVED','2026-04-28 03:17:58.000000',1,5);
/*!40000 ALTER TABLE `account_registration_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_number` varchar(255) DEFAULT NULL,
  `balance` decimal(38,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `loai_tk` varchar(50) DEFAULT 'THANH_TOAN' COMMENT 'Loai tai khoan: THANH_TOAN, TIET_KIEM...',
  `ngay_mo` date DEFAULT NULL COMMENT 'Ngay mo tai khoan',
  `loaitk` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm507upg2gtg1crodqaw55oegd` (`account_number`),
  KEY `FKn6x8pdp50os8bq5rbb792upse` (`customer_id`),
  CONSTRAINT `FKn6x8pdp50os8bq5rbb792upse` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'0359217462',54000.00,'HOAT_DONG',1,'THANH_TOAN','2026-04-26',NULL),(3,'237504',1486402.00,'HOAT_DONG',4,'THANH_TOAN','2026-04-26',NULL),(4,'146012',1000000.00,'HOAT_DONG',6,'THANH_TOAN','2026-04-26',NULL),(5,'MB4181179394',0.00,'HOAT_DONG',7,'THANH_TOAN','2026-04-26','THANH_TOAN'),(6,'MB1146683841',1376.00,'HOAT_DONG',9,'THANH_TOAN','2026-04-27','THANH_TOAN'),(7,'MB5025839494',0.00,'HOAT_DONG',10,'THANH_TOAN','2026-04-27','THANH_TOAN'),(8,'0359217470',0.00,'HOAT_DONG',11,'THANH_TOAN','2026-04-28','THANH_TOAN'),(9,'0359217465',51000.00,'HOAT_DONG',12,'THANH_TOAN','2026-04-28','THANH_TOAN');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS `branches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branches` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_address` varchar(255) DEFAULT NULL,
  `branch_name` varchar(255) DEFAULT NULL,
  `ma_chi_nhanh` varchar(50) DEFAULT NULL COMMENT 'Ma chi nhanh nghiep vu',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ma_chi_nhanh` (`ma_chi_nhanh`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branches`
--

LOCK TABLES `branches` WRITE;
/*!40000 ALTER TABLE `branches` DISABLE KEYS */;
INSERT INTO `branches` VALUES (1,'','Hoàn Kiếm','CN001'),(2,'','Cầu Giấy','CN002');
/*!40000 ALTER TABLE `branches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customers` (
  `address` varchar(255) DEFAULT NULL,
  `cccd` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `ma_kh` varchar(50) DEFAULT NULL COMMENT 'Ma khach hang nghiep vu',
  `ngay_sinh` date DEFAULT NULL COMMENT 'Ngay sinh',
  `makh` varchar(255) DEFAULT NULL,
  `branch_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ma_kh` (`ma_kh`),
  UNIQUE KEY `UK_5dweqfv1fd3dna8p76o22ofaa` (`makh`),
  KEY `FKod9w7vyxwx64aep8bbj0cg9fw` (`branch_id`),
  CONSTRAINT `FKod9w7vyxwx64aep8bbj0cg9fw` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `FKpog72rpahj62h7nod9wwc28if` FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES ('Hanoi','001206036716','0359217462',1,NULL,'2006-07-22','KH414171',1),(NULL,NULL,NULL,4,NULL,NULL,'KH410319',1),(NULL,NULL,NULL,6,NULL,NULL,'KH410412',2),('HN',NULL,'0359217460',7,NULL,NULL,'KH023871',2),(NULL,'0012874831','0359217466',9,NULL,'2006-07-22','KH117239',1),('fsfwe','43143141312','0359217462',10,NULL,NULL,'KH410446',1),('Hà Nội','001206036716','0359217470',11,NULL,NULL,'KH896840',1),('Thái Hòa - Ba Vì','001206036711','0359217465',12,NULL,NULL,'KH341864',1);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employees` (
  `position` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `branch_id` bigint(20) DEFAULT NULL,
  `ma_nv` varchar(50) DEFAULT NULL COMMENT 'Ma nhan vien nghiep vu',
  `bo_phan` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'Bo phan cong tac',
  `luong` decimal(38,2) DEFAULT NULL,
  `manv` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ma_nv` (`ma_nv`),
  UNIQUE KEY `UK_sw8o9nidawbk83tm6fame9vps` (`manv`),
  KEY `FKmef7fp4oyblw7d2y3g1whac3o` (`branch_id`),
  CONSTRAINT `FKd6th9xowehhf1kmmq1dsseq28` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKmef7fp4oyblw7d2y3g1whac3o` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES ('Tổng quản lý',2,NULL,NULL,NULL,100000000.00,NULL),('Giao dich vien',5,1,NULL,NULL,50000000.00,NULL),(NULL,8,2,NULL,'Phòng GD',15000000.00,'NV9709');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saved_recipients`
--

DROP TABLE IF EXISTS `saved_recipients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `saved_recipients` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_number` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdumct2bqr8sybix430jwffqrp` (`customer_id`),
  CONSTRAINT `FKdumct2bqr8sybix430jwffqrp` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saved_recipients`
--

LOCK TABLES `saved_recipients` WRITE;
/*!40000 ALTER TABLE `saved_recipients` DISABLE KEYS */;
INSERT INTO `saved_recipients` VALUES (1,'0359217465','CHU QUANG HUNG',NULL,1);
/*!40000 ALTER TABLE `saved_recipients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(38,2) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `from_account_id` bigint(20) DEFAULT NULL,
  `to_account_id` bigint(20) DEFAULT NULL,
  `ma_gd` varchar(100) DEFAULT NULL COMMENT 'Ma giao dich nghiep vu (tu dong sinh)',
  `magd` varchar(255) DEFAULT NULL,
  `performed_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ma_gd` (`ma_gd`),
  UNIQUE KEY `UK_juabak9bfcmmvprld75cfxfts` (`magd`),
  KEY `FK7i7kboanveneetad7jyhbr0a7` (`from_account_id`),
  KEY `FKra0an432c5wjo76mojluk0v28` (`to_account_id`),
  CONSTRAINT `FK7i7kboanveneetad7jyhbr0a7` FOREIGN KEY (`from_account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `FKra0an432c5wjo76mojluk0v28` FOREIGN KEY (`to_account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (8,10000.00,'0359217462 chuyen tien','2026-04-26 14:24:55.000000','TRANSFER',1,3,NULL,'CK20260426212455732',NULL),(9,100000.00,'0359217462 chuyen tien','2026-04-26 14:28:59.000000','TRANSFER',1,3,NULL,'CK20260426212859393',NULL),(10,1.00,'0359217462 chuyen tien','2026-04-26 15:07:00.000000','TRANSFER',1,3,NULL,'CK20260426220700211',NULL),(11,100000.00,'0359217462 chuyen tien','2026-04-27 12:54:14.000000','TRANSFER',1,6,NULL,'CK20260427195414233',NULL),(12,99000.00,'0359217466 chuyen tien','2026-04-27 12:57:26.000000','TRANSFER',6,1,NULL,'CK20260427195726181',NULL),(13,376.78,'0359217462 chuyen tien','2026-04-28 02:11:35.000000','TRANSFER',1,6,NULL,'CK20260428091135180',NULL),(14,11111.00,'0359217462 chuyen tien','2026-04-28 02:17:30.000000','TRANSFER',1,3,NULL,'CK20260428091730021',NULL),(15,365290.00,'0359217462 chuyen tien','2026-04-28 02:17:49.000000','TRANSFER',1,3,NULL,'CK20260428091749879',NULL),(16,50000.00,'0359217465 chuyen tien','2026-04-28 03:18:58.000000','TRANSFER',9,1,NULL,'CK20260428101858423',NULL),(17,1000.00,'0359217462 chuyen tien','2026-04-28 08:59:46.000000','TRANSFER',1,9,NULL,'CK20260428155946017',NULL),(18,100000.00,'WITHDRAW','2026-04-28 09:53:06.000000','WITHDRAW',1,NULL,NULL,'RUT20260428165306588','employee01'),(19,15000.00,'WITHDRAW','2026-04-28 09:55:27.000000','WITHDRAW',1,NULL,NULL,'RUT20260428165527252','employee01');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'0359217462@gmail.com','Chu Quang Hưng','$2a$10$2t780T.rv7QQRsqg492aiOUJP6ha0vA7SC3tu1pgnfnBn11qzE43q','ROLE_CUSTOMER','0359217462','ACTIVE'),(2,'admin@mbbank.com','Tổng giám đốc','$2a$10$JDrIm7iJJTvHdObNC6.lJOjsp3Ys1bfTj3t4oXvOoGY77jZQkLV4e','ROLE_SYSADMIN','admin','ACTIVE'),(4,'test@mbbank.com','Test User','$2a$10$2t780T.rv7QQRsqg492aiOUJP6ha0vA7SC3tu1pgnfnBn11qzE43q','ROLE_CUSTOMER','testuser','ACTIVE'),(5,'employee01@mbbank.com','Nguyen Van A','$2a$10$JDrIm7iJJTvHdObNC6.lJOjsp3Ys1bfTj3t4oXvOoGY77jZQkLV4e','ROLE_EMPLOYEE','employee01','ACTIVE'),(6,'12431213@gmail.com','Khách hàng 12431213','$2a$10$2t780T.rv7QQRsqg492aiOUJP6ha0vA7SC3tu1pgnfnBn11qzE43q','ROLE_CUSTOMER','12431213','ACTIVE'),(7,NULL,'Chu Hưng','$2a$10$isKgMkbN.8nIZHu5JDubLeFfWvcT6BsvfkJkB0Lw90EnNqJcXCTCG','ROLE_CUSTOMER','cqhvn','ACTIVE'),(8,'nvb@gmail.com','Nguyễn Văn B','$2a$10$hG0pcBtZAVbfLOGOnQj7AuFowzyajnmEDDUPkz4BJDMIcBEsJ4A/a','ROLE_EMPLOYEE','employee02','ACTIVE'),(9,NULL,'Cqh','$2a$10$SJw2lGnTcQWzODeO/Vt9w.1iPQWTFdcoz11FxHHir5vUJi.Vf12Va','ROLE_CUSTOMER','0359217466','ACTIVE'),(10,NULL,'fwfwe','$2a$10$QZr3QnTHjFCJbCWvA3DH/Odvwm4bXIMfExLd7VphModjIHS4lSbt.','ROLE_CUSTOMER','0359217469','ACTIVE'),(11,NULL,'CHU QUANG HUNG','$2a$10$dOlTQaj3ISASfkw07gy28OwWWDmHCZrSxf5U4cze4SOM6CbSBNr8G','ROLE_CUSTOMER','0359217470','ACTIVE'),(12,NULL,'CHU QUANG HUNG','$2a$10$qHC8kRk3AdJNoAvNX34kGuD2jDCzQL19np.jWE8m73LqDHNfWXB7u','ROLE_CUSTOMER','0359217465','ACTIVE');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-28 20:29:12
