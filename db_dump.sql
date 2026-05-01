-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 28, 2026 lúc 03:17 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `mbbank_db`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `accounts`
--

CREATE TABLE `accounts` (
  `id` bigint(20) NOT NULL,
  `account_number` varchar(255) DEFAULT NULL,
  `balance` decimal(38,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `loai_tk` varchar(50) DEFAULT 'THANH_TOAN' COMMENT 'Loai tai khoan: THANH_TOAN, TIET_KIEM...',
  `ngay_mo` date DEFAULT NULL COMMENT 'Ngay mo tai khoan',
  `loaitk` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Đang đổ dữ liệu cho bảng `accounts`
--

INSERT INTO `accounts` (`id`, `account_number`, `balance`, `status`, `customer_id`, `loai_tk`, `ngay_mo`, `loaitk`) VALUES
(1, '0359217462', 54000.00, 'HOAT_DONG', 1, 'THANH_TOAN', '2026-04-26', NULL),
(3, '237504', 1486402.00, 'HOAT_DONG', 4, 'THANH_TOAN', '2026-04-26', NULL),
(4, '146012', 1000000.00, 'HOAT_DONG', 6, 'THANH_TOAN', '2026-04-26', NULL),
(5, 'MB4181179394', 0.00, 'HOAT_DONG', 7, 'THANH_TOAN', '2026-04-26', 'THANH_TOAN'),
(6, 'MB1146683841', 1376.00, 'HOAT_DONG', 9, 'THANH_TOAN', '2026-04-27', 'THANH_TOAN'),
(7, 'MB5025839494', 0.00, 'HOAT_DONG', 10, 'THANH_TOAN', '2026-04-27', 'THANH_TOAN'),
(8, '0359217470', 0.00, 'HOAT_DONG', 11, 'THANH_TOAN', '2026-04-28', 'THANH_TOAN'),
(9, '0359217465', 51000.00, 'HOAT_DONG', 12, 'THANH_TOAN', '2026-04-28', 'THANH_TOAN');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account_registration_requests`
--

CREATE TABLE `account_registration_requests` (
  `id` bigint(20) NOT NULL,
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
  `reviewed_by` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `account_registration_requests`
--

INSERT INTO `account_registration_requests` (`id`, `address`, `cccd`, `desired_username`, `email`, `full_name`, `loaitk`, `ngay_sinh`, `password`, `phone`, `reject_reason`, `reviewed_at`, `status`, `submitted_at`, `branch_id`, `reviewed_by`) VALUES
(1, 'fsfwe', '43143141312', '0359217469', NULL, 'fwfwe', 'THANH_TOAN', NULL, '123', '0359217462', NULL, '2026-04-27 09:12:40.000000', 'APPROVED', '2026-04-27 09:12:24.000000', 1, 5),
(2, 'Hà Nội', '001206036716', '0359217470', NULL, 'CHU QUANG HUNG', 'THANH_TOAN', NULL, 'Cqhvn22072006.', '0359217470', NULL, '2026-04-28 03:08:57.000000', 'APPROVED', '2026-04-28 02:19:55.000000', 1, 5),
(3, 'Thái Hòa - Ba Vì', '001206036711', '0359217465', NULL, 'CHU QUANG HUNG', 'THANH_TOAN', NULL, 'Cqhvn22072006.', '0359217465', NULL, '2026-04-28 03:18:16.000000', 'APPROVED', '2026-04-28 03:17:58.000000', 1, 5);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `branches`
--

CREATE TABLE `branches` (
  `id` bigint(20) NOT NULL,
  `branch_address` varchar(255) DEFAULT NULL,
  `branch_name` varchar(255) DEFAULT NULL,
  `ma_chi_nhanh` varchar(50) DEFAULT NULL COMMENT 'Ma chi nhanh nghiep vu'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `branches`
--

INSERT INTO `branches` (`id`, `branch_address`, `branch_name`, `ma_chi_nhanh`) VALUES
(1, '', 'Hoàn Kiếm', 'CN001'),
(2, '', 'Cầu Giấy', 'CN002');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customers`
--

CREATE TABLE `customers` (
  `address` varchar(255) DEFAULT NULL,
  `cccd` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `ma_kh` varchar(50) DEFAULT NULL COMMENT 'Ma khach hang nghiep vu',
  `ngay_sinh` date DEFAULT NULL COMMENT 'Ngay sinh',
  `makh` varchar(255) DEFAULT NULL,
  `branch_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `customers`
--

INSERT INTO `customers` (`address`, `cccd`, `phone`, `id`, `ma_kh`, `ngay_sinh`, `makh`, `branch_id`) VALUES
('Hanoi', '001206036716', '0359217462', 1, NULL, '2006-07-22', 'KH414171', 1),
(NULL, NULL, NULL, 4, NULL, NULL, 'KH410319', 1),
(NULL, NULL, NULL, 6, NULL, NULL, 'KH410412', 2),
('HN', NULL, '0359217460', 7, NULL, NULL, 'KH023871', 2),
(NULL, '0012874831', '0359217466', 9, NULL, '2006-07-22', 'KH117239', 1),
('fsfwe', '43143141312', '0359217462', 10, NULL, NULL, 'KH410446', 1),
('Hà Nội', '001206036716', '0359217470', 11, NULL, NULL, 'KH896840', 1),
('Thái Hòa - Ba Vì', '001206036711', '0359217465', 12, NULL, NULL, 'KH341864', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `employees`
--

CREATE TABLE `employees` (
  `position` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `branch_id` bigint(20) DEFAULT NULL,
  `ma_nv` varchar(50) DEFAULT NULL COMMENT 'Ma nhan vien nghiep vu',
  `bo_phan` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'Bo phan cong tac',
  `luong` decimal(38,2) DEFAULT NULL,
  `manv` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `employees`
--

INSERT INTO `employees` (`position`, `id`, `branch_id`, `ma_nv`, `bo_phan`, `luong`, `manv`) VALUES
('Tổng quản lý', 2, NULL, NULL, NULL, 100000000.00, NULL),
('Giao dich vien', 5, 1, NULL, NULL, 50000000.00, NULL),
(NULL, 8, 2, NULL, 'Phòng GD', 15000000.00, 'NV9709');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `saved_recipients`
--

CREATE TABLE `saved_recipients` (
  `id` bigint(20) NOT NULL,
  `account_number` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `saved_recipients`
--

INSERT INTO `saved_recipients` (`id`, `account_number`, `full_name`, `nickname`, `customer_id`) VALUES
(1, '0359217465', 'CHU QUANG HUNG', NULL, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `transactions`
--

CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `from_account_id` bigint(20) DEFAULT NULL,
  `to_account_id` bigint(20) DEFAULT NULL,
  `ma_gd` varchar(100) DEFAULT NULL COMMENT 'Ma giao dich nghiep vu (tu dong sinh)',
  `magd` varchar(255) DEFAULT NULL,
  `performed_by` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `transactions`
--

INSERT INTO `transactions` (`id`, `amount`, `content`, `timestamp`, `transaction_type`, `from_account_id`, `to_account_id`, `ma_gd`, `magd`, `performed_by`) VALUES
(8, 10000.00, '0359217462 chuyen tien', '2026-04-26 14:24:55.000000', 'TRANSFER', 1, 3, NULL, 'CK20260426212455732', NULL),
(9, 100000.00, '0359217462 chuyen tien', '2026-04-26 14:28:59.000000', 'TRANSFER', 1, 3, NULL, 'CK20260426212859393', NULL),
(10, 1.00, '0359217462 chuyen tien', '2026-04-26 15:07:00.000000', 'TRANSFER', 1, 3, NULL, 'CK20260426220700211', NULL),
(11, 100000.00, '0359217462 chuyen tien', '2026-04-27 12:54:14.000000', 'TRANSFER', 1, 6, NULL, 'CK20260427195414233', NULL),
(12, 99000.00, '0359217466 chuyen tien', '2026-04-27 12:57:26.000000', 'TRANSFER', 6, 1, NULL, 'CK20260427195726181', NULL),
(13, 376.78, '0359217462 chuyen tien', '2026-04-28 02:11:35.000000', 'TRANSFER', 1, 6, NULL, 'CK20260428091135180', NULL),
(14, 11111.00, '0359217462 chuyen tien', '2026-04-28 02:17:30.000000', 'TRANSFER', 1, 3, NULL, 'CK20260428091730021', NULL),
(15, 365290.00, '0359217462 chuyen tien', '2026-04-28 02:17:49.000000', 'TRANSFER', 1, 3, NULL, 'CK20260428091749879', NULL),
(16, 50000.00, '0359217465 chuyen tien', '2026-04-28 03:18:58.000000', 'TRANSFER', 9, 1, NULL, 'CK20260428101858423', NULL),
(17, 1000.00, '0359217462 chuyen tien', '2026-04-28 08:59:46.000000', 'TRANSFER', 1, 9, NULL, 'CK20260428155946017', NULL),
(18, 100000.00, 'WITHDRAW', '2026-04-28 09:53:06.000000', 'WITHDRAW', 1, NULL, NULL, 'RUT20260428165306588', 'employee01'),
(19, 15000.00, 'WITHDRAW', '2026-04-28 09:55:27.000000', 'WITHDRAW', 1, NULL, NULL, 'RUT20260428165527252', 'employee01');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `email`, `full_name`, `password`, `role`, `username`, `status`) VALUES
(1, '0359217462@gmail.com', 'Chu Quang Hưng', '$2a$10$2t780T.rv7QQRsqg492aiOUJP6ha0vA7SC3tu1pgnfnBn11qzE43q', 'ROLE_CUSTOMER', '0359217462', 'ACTIVE'),
(2, 'admin@mbbank.com', 'Tổng giám đốc', '$2a$10$JDrIm7iJJTvHdObNC6.lJOjsp3Ys1bfTj3t4oXvOoGY77jZQkLV4e', 'ROLE_SYSADMIN', 'admin', 'ACTIVE'),
(4, 'test@mbbank.com', 'Test User', '$2a$10$2t780T.rv7QQRsqg492aiOUJP6ha0vA7SC3tu1pgnfnBn11qzE43q', 'ROLE_CUSTOMER', 'testuser', 'ACTIVE'),
(5, 'employee01@mbbank.com', 'Nguyen Van A', '$2a$10$JDrIm7iJJTvHdObNC6.lJOjsp3Ys1bfTj3t4oXvOoGY77jZQkLV4e', 'ROLE_EMPLOYEE', 'employee01', 'ACTIVE'),
(6, '12431213@gmail.com', 'Khách hàng 12431213', '$2a$10$2t780T.rv7QQRsqg492aiOUJP6ha0vA7SC3tu1pgnfnBn11qzE43q', 'ROLE_CUSTOMER', '12431213', 'ACTIVE'),
(7, NULL, 'Chu Hưng', '$2a$10$isKgMkbN.8nIZHu5JDubLeFfWvcT6BsvfkJkB0Lw90EnNqJcXCTCG', 'ROLE_CUSTOMER', 'cqhvn', 'ACTIVE'),
(8, 'nvb@gmail.com', 'Nguyễn Văn B', '$2a$10$hG0pcBtZAVbfLOGOnQj7AuFowzyajnmEDDUPkz4BJDMIcBEsJ4A/a', 'ROLE_EMPLOYEE', 'employee02', 'ACTIVE'),
(9, NULL, 'Cqh', '$2a$10$SJw2lGnTcQWzODeO/Vt9w.1iPQWTFdcoz11FxHHir5vUJi.Vf12Va', 'ROLE_CUSTOMER', '0359217466', 'ACTIVE'),
(10, NULL, 'fwfwe', '$2a$10$QZr3QnTHjFCJbCWvA3DH/Odvwm4bXIMfExLd7VphModjIHS4lSbt.', 'ROLE_CUSTOMER', '0359217469', 'ACTIVE'),
(11, NULL, 'CHU QUANG HUNG', '$2a$10$dOlTQaj3ISASfkw07gy28OwWWDmHCZrSxf5U4cze4SOM6CbSBNr8G', 'ROLE_CUSTOMER', '0359217470', 'ACTIVE'),
(12, NULL, 'CHU QUANG HUNG', '$2a$10$qHC8kRk3AdJNoAvNX34kGuD2jDCzQL19np.jWE8m73LqDHNfWXB7u', 'ROLE_CUSTOMER', '0359217465', 'ACTIVE');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKm507upg2gtg1crodqaw55oegd` (`account_number`),
  ADD KEY `FKn6x8pdp50os8bq5rbb792upse` (`customer_id`);

--
-- Chỉ mục cho bảng `account_registration_requests`
--
ALTER TABLE `account_registration_requests`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_k6knnvppy0rs9vel95lp99k8t` (`cccd`),
  ADD UNIQUE KEY `UK_btt2avcuy8swm7f06wc8y9rgp` (`desired_username`),
  ADD KEY `FK5s56lx3myjif1ukudk679q9uk` (`branch_id`),
  ADD KEY `FKfedh7lfxnl6ufnphqpwo04ors` (`reviewed_by`);

--
-- Chỉ mục cho bảng `branches`
--
ALTER TABLE `branches`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma_chi_nhanh` (`ma_chi_nhanh`);

--
-- Chỉ mục cho bảng `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma_kh` (`ma_kh`),
  ADD UNIQUE KEY `UK_5dweqfv1fd3dna8p76o22ofaa` (`makh`),
  ADD KEY `FKod9w7vyxwx64aep8bbj0cg9fw` (`branch_id`);

--
-- Chỉ mục cho bảng `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma_nv` (`ma_nv`),
  ADD UNIQUE KEY `UK_sw8o9nidawbk83tm6fame9vps` (`manv`),
  ADD KEY `FKmef7fp4oyblw7d2y3g1whac3o` (`branch_id`);

--
-- Chỉ mục cho bảng `saved_recipients`
--
ALTER TABLE `saved_recipients`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdumct2bqr8sybix430jwffqrp` (`customer_id`);

--
-- Chỉ mục cho bảng `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ma_gd` (`ma_gd`),
  ADD UNIQUE KEY `UK_juabak9bfcmmvprld75cfxfts` (`magd`),
  ADD KEY `FK7i7kboanveneetad7jyhbr0a7` (`from_account_id`),
  ADD KEY `FKra0an432c5wjo76mojluk0v28` (`to_account_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `accounts`
--
ALTER TABLE `accounts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `account_registration_requests`
--
ALTER TABLE `account_registration_requests`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `branches`
--
ALTER TABLE `branches`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `saved_recipients`
--
ALTER TABLE `saved_recipients`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `FKn6x8pdp50os8bq5rbb792upse` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

--
-- Các ràng buộc cho bảng `account_registration_requests`
--
ALTER TABLE `account_registration_requests`
  ADD CONSTRAINT `FK5s56lx3myjif1ukudk679q9uk` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  ADD CONSTRAINT `FKfedh7lfxnl6ufnphqpwo04ors` FOREIGN KEY (`reviewed_by`) REFERENCES `employees` (`id`);

--
-- Các ràng buộc cho bảng `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `FKod9w7vyxwx64aep8bbj0cg9fw` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  ADD CONSTRAINT `FKpog72rpahj62h7nod9wwc28if` FOREIGN KEY (`id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `FKd6th9xowehhf1kmmq1dsseq28` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKmef7fp4oyblw7d2y3g1whac3o` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`);

--
-- Các ràng buộc cho bảng `saved_recipients`
--
ALTER TABLE `saved_recipients`
  ADD CONSTRAINT `FKdumct2bqr8sybix430jwffqrp` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

--
-- Các ràng buộc cho bảng `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `FK7i7kboanveneetad7jyhbr0a7` FOREIGN KEY (`from_account_id`) REFERENCES `accounts` (`id`),
  ADD CONSTRAINT `FKra0an432c5wjo76mojluk0v28` FOREIGN KEY (`to_account_id`) REFERENCES `accounts` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
