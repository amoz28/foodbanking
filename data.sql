-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2021 at 04:58 AM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `foodehli_fdbnkapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `auth`
--

CREATE TABLE `auth` (
  `id` int(11) NOT NULL,
  `user_id` int(11) UNSIGNED NOT NULL,
  `source` varchar(255) NOT NULL,
  `source_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `auth`
--

INSERT INTO `auth` (`id`, `user_id`, `source`, `source_id`) VALUES
(5, 34, 'facebook', '5244589852221598'),
(6, 36, 'facebook', '10222500793687195'),
(7, 37, 'facebook', '3472652122815203'),
(8, 39, 'facebook', '10160780604928298'),
(9, 17, 'facebook', '10219170185070747'),
(10, 41, 'facebook', '10158111514723089'),
(11, 43, 'facebook', '895330577967861'),
(12, 47, 'facebook', '4179082295455887'),
(13, 48, 'facebook', '10220568393498184'),
(14, 50, 'apple', '001813.0fab7a46109045eaa4039445a6faacf7.1854'),
(15, 51, 'apple', '000934.0d060180d33044d888b4121f535822d2.2050'),
(16, 52, 'apple', '000214.c46c53d5fa3e4366803195d09196b466.0627');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`id`, `user_id`, `item_id`, `quantity`) VALUES
(95, 14, 2, 1),
(96, 14, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `deposit`
--

CREATE TABLE `deposit` (
  `id` int(11) NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `type` tinyint(4) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount_paid` double NOT NULL,
  `credits_received` int(11) NOT NULL,
  `created_at` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `deposit`
--

INSERT INTO `deposit` (`id`, `user_id`, `type`, `description`, `amount_paid`, `credits_received`, `created_at`) VALUES
(24, 53, 1, 'Subscription For the month of june', 200, 20000, NULL),
(25, 53, 1, 'Subscription For the month of june', 200, 20000, NULL),
(26, 53, 1, 'Subscription For the month of june', 200, 20000, NULL),
(27, 53, 1, 'Subscription For the month of june', 200, 20000, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_image` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
  `credit_price` int(10) UNSIGNED NOT NULL,
  `created_by` int(10) UNSIGNED NOT NULL,
  `created_at` int(11) NOT NULL,
  `updated_by` int(10) UNSIGNED NOT NULL,
  `updated_at` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`id`, `name`, `item_image`, `credit_price`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES
(1, 'Rice (5kg)', 'images/Rice (5kg).jpg', 22, 14, 0, 38, 1608089761),
(2, 'Beans (5kg)', 'images/Beans (5kg).jpg', 18, 14, 0, 38, 1608089766),
(3, 'Garri (5kg)', 'images/Garri (5kg).jpg', 12, 14, 0, 38, 1608089771),
(4, 'Vegetable Oil (3 litre)', 'images/Vegetable Oil (3 litre).jpg', 30, 14, 0, 14, 1604928252),
(5, 'Palm Oil (1.5 litre)', 'images/Palm Oil (1.5 litre).jpg', 12, 14, 0, 38, 1608089838),
(6, 'Semolina (1kg)', 'images/Semolina (1kg).jpg', 5, 14, 0, 38, 1608089872),
(7, 'Rice (10kg)', 'images/Rice (10kg).jpg', 44, 14, 2147483647, 38, 1608088788),
(8, 'Beans (10kg)', 'images/Beans (10kg).jpg', 42, 14, 2147483647, 38, 1608088817),
(9, 'Garri (10kg)', 'images/Garri (10kg).jpg', 20, 14, 2147483647, 38, 1608088873),
(10, 'Vegetable Oil (1.5 litre)', 'images/Vegetable Oil (1.5 litre).jpg', 15, 14, 2147483647, 38, 1608089835),
(11, 'Palm Oil (3 litre)', 'images/Palm Oil (3 litre).jpg', 18, 14, 2147483647, 38, 1608088926),
(12, 'Semolina (5kg)', 'images/Semolina (5kg).jpg', 50, 14, 2147483647, 38, 1608307537),
(13, 'Poundo Yam (3kg)', 'images/Poundo Yam (3kg).jpg', 50, 14, 2147483647, 14, 1604940579),
(14, 'Tomatoes (10 Sachet)', 'images/Tomatoes (10 Sachet).jpg', 14, 14, 2147483647, 38, 1608089700),
(15, 'Rice (25kg)', 'images/Rice (25kg).jpg', 140, 14, 2147483647, 38, 1608089530),
(16, 'Beans (20kg)', 'images/Beans (20kg).jpg', 82, 14, 2147483647, 38, 1608089563),
(17, 'Garri (20kg)', 'images/Garri (20kg).jpg', 40, 14, 2147483647, 38, 1608089645),
(18, 'Vegetable Oil (5 litre)', 'images/Vegetable Oil (5 litre).jpg', 45, 14, 2147483647, 38, 1608089596),
(19, 'Palm Oil (5 litre)', 'images/Palm Oil (5 litre).jpg', 45, 14, 2147483647, 38, 1608089613),
(20, 'Poundo Yam (5kg)', 'images/Poundo Yam (5kg).jpg', 50, 14, 2147483647, 14, 1604940621),
(21, 'Milo Refill (750gm)', 'images/Milo Refill (750gm).jpg', 18, 14, 2147483647, 38, 1608089329),
(22, 'Milk Refill (750gm)', 'images/Milk Refill (750gm).jpg', 15, 14, 2147483647, 38, 1608089464),
(23, 'Tomatoes (12 Sachet)', 'images/Tomatoes (12 Sachet).jpg', 50, 14, 2147483647, 14, 1604928523),
(27, 'Milo Tin (750g)', 'images/Milo Tin (750g).jpg', 18, 14, 1604932568, 38, 1608089227),
(30, 'Peak Milk Tin (900g)', 'images/Peak Milk Tin (900g).jpg', 15, 14, 1604933440, 38, 1608089364),
(31, 'Semolina (2kg)', 'images/Semolina (2kg).jpg', 10, 38, 1608089073, 38, 1608089073),
(32, 'Tomatoes (5 Sachet)', 'images/Tomatoes (5 Sachet).jpg', 7, 38, 1608089172, 38, 1608089172),
(33, 'Maggi Star (1 Pack)', 'images/Maggi Star (1 Pack).jpg', 5, 38, 1608090029, 38, 1608090029);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `recipient` int(11) NOT NULL DEFAULT 0,
  `subject` varchar(256) NOT NULL,
  `body` text NOT NULL,
  `created_by` int(11) NOT NULL,
  `created_at` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `message`
--

INSERT INTO `message` (`id`, `recipient`, `subject`, `body`, `created_by`, `created_at`) VALUES
(3, 38, 'FUND YOUR FOOD WALLET TODAY', 'Dear Paddler, \r\nwe noticed you are yet to fund your e-wallet', 38, 1608530204),
(4, 17, 'YOUR ITEMS ARE READY', 'Your items are ready for delivery to the address you provided in your profile.\r\n\r\n...be food secure', 38, 1608530598),
(5, 27, 'FUND WALLET', '<p>You are yet to fund you foodbanking wallet. Pls fund wallet to be FOOD-SECURE</p>\r\n', 38, 1616382702);

-- --------------------------------------------------------

--
-- Table structure for table `message_read`
--

CREATE TABLE `message_read` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `created_at` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `message_read`
--

INSERT INTO `message_read` (`id`, `user_id`, `message_id`, `created_at`) VALUES
(2, 38, 3, 1608530250),
(3, 17, 4, 1608534812),
(6, 27, 5, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `migration`
--

CREATE TABLE `migration` (
  `version` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apply_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migration`
--

INSERT INTO `migration` (`version`, `apply_time`) VALUES
('m000000_000000_base', 1599095590);

-- --------------------------------------------------------

--
-- Table structure for table `package`
--

CREATE TABLE `package` (
  `id` int(10) UNSIGNED NOT NULL,
  `package_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `package_price` double NOT NULL,
  `package_image` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL,
  `updated_by` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `package`
--

INSERT INTO `package` (`id`, `package_name`, `package_price`, `package_image`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'Starter Pack', 120, 'images/Starter Pack.jpg', 0, 0, 1617652493, 38),
(2, 'Combo Pack', 250, 'images/Combo Pack.jpg', 0, 0, 1601247717, 14),
(3, 'Super Combo Pack', 500, 'images/Super Combo Pack.jpg', 0, 0, 1601247735, 14);

-- --------------------------------------------------------

--
-- Table structure for table `package_item`
--

CREATE TABLE `package_item` (
  `id` int(11) NOT NULL,
  `package_id` int(10) UNSIGNED NOT NULL,
  `item_id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `package_item`
--

INSERT INTO `package_item` (`id`, `package_id`, `item_id`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(11, 1, 5),
(12, 1, 6),
(13, 1, 10),
(14, 2, 4),
(15, 2, 7),
(16, 2, 8),
(17, 2, 9),
(18, 2, 11),
(19, 2, 13),
(20, 2, 14),
(21, 3, 12),
(22, 3, 15),
(23, 3, 16),
(24, 3, 17),
(25, 3, 18),
(26, 3, 19),
(27, 3, 20),
(28, 3, 21),
(29, 3, 22),
(30, 3, 23),
(32, 3, 33);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_MODERATOR'),
(3, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `setting`
--

CREATE TABLE `setting` (
  `id` int(11) NOT NULL,
  `credit_conversion` int(11) NOT NULL,
  `delivery_days` varchar(255) NOT NULL,
  `withdrawals` int(11) NOT NULL,
  `updated_by` int(11) NOT NULL,
  `updated_at` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `setting`
--

INSERT INTO `setting` (`id`, `credit_conversion`, `delivery_days`, `withdrawals`, `updated_by`, `updated_at`) VALUES
(0, 100, '7,14,21,28', 1, 38, 1617652351);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(10) UNSIGNED NOT NULL,
  `firstname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sex` enum('Male','Female','','') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `age` tinyint(3) UNSIGNED DEFAULT NULL,
  `marital_status` enum('Single','Married','Divorced','Widowed') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `family_sum` tinyint(4) DEFAULT NULL,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth_key` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_reset_token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `package_id` int(10) UNSIGNED DEFAULT NULL,
  `package_expiry_date` date DEFAULT NULL,
  `status` tinyint(3) UNSIGNED DEFAULT NULL,
  `role` tinyint(3) UNSIGNED DEFAULT NULL,
  `credit_balance` int(10) UNSIGNED DEFAULT NULL,
  `created_at` int(11) DEFAULT NULL,
  `no_of_monthly_withdrawal` tinyint(4) NOT NULL DEFAULT 0,
  `updated_at` int(11) DEFAULT NULL,
  `verification_token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `token` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'null',
  `is_enabled` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `firstname`, `lastname`, `sex`, `age`, `marital_status`, `family_sum`, `username`, `auth_key`, `password_hash`, `password_reset_token`, `email`, `phone`, `address`, `city`, `state`, `package_id`, `package_expiry_date`, `status`, `role`, `credit_balance`, `created_at`, `no_of_monthly_withdrawal`, `updated_at`, `verification_token`, `token`, `is_enabled`) VALUES
(14, 'Sunnie', 'Bill', 'Male', 18, 'Married', 4, 'acken', 'MebhdGcHeb_6f6VfG2GHi9RttQC2_Hx-', '$2y$13$6GVI2qdj4XLCA/VH0hYpjO9PmNa9.TrPtIY6oiLHyGVb1geHfyhuS', 'veEsrQifsNCLB7ElG2dMHmJvUbFgFhvQ_1605872821', 'web@acken.org', '2348038857480', 'Plot 98 Kado-Life Camp Extention', 'Ikeja', 'Abuja', 1, NULL, 10, 2, 68000, 159800000, 0, 1616382581, 'jwO_zkjlau-qXSv79OmDraQ_6sxzYH9O_1599500139', 'null', 0),
(16, '', '', 'Male', 0, 'Single', 0, 'sunniebill', 'mx6M8klgM6oV0A7gjNgkR84b70pbgSIr', '$2y$13$xMunDbYhGM/m.PsRM5aSwekzC58hYCn6MvAvq4bbBXPf4/Raqd3Zq', NULL, 'castromiller007@gmail.com', '', '', '', '', 0, NULL, 10, 0, 0, 1602453169, 0, 1602454244, 'Eyk8H-9yEzTOZgHG-pvRBmFdG33bYaAP_1602453169', 'null', 0),
(17, '', '', 'Male', 0, 'Single', 0, 'Osigwe ahmed momoh', 'F3PS0qEusHYUMbgn44oSPCoxcT1k2AA2', '$2y$13$KEBLMoH/m1OglT17NIa1o.Jg95nuQXsn5fanIztW1hh7gCXrDyxrm', NULL, 'osigwemomoh@yahoo.co.uk', '', '', '', '', 0, NULL, 10, 0, 0, 1602667082, 0, 1602667221, '7rDSrPDL_7A2-FOagRi-Lw_98NFdV--2_1602667082', 'null', 0),
(27, '', '', 'Male', 0, 'Single', 0, 'Adams Yakubu', 'Mt3Iv_cKgwPTr7okQ_Imu9Z2v5w7_7MC', '$2y$13$AuxQ.YwglAW6uLUN91C35urPAu7bbWdbMydgMIKUXoYCKlt7J1DDi', NULL, 'Adamsonyakubu3@gmail.com', '2348060488571', '', '', '', 0, NULL, 10, 0, 0, 1605380031, 0, 1605380597, 'Bil7SKVtH5my1S3MWYWabWlwv-PiZoHB_1605380031', 'null', 0),
(34, '', '', 'Male', 0, 'Single', 0, 'winfredken', '', '', NULL, 'winfredabba@yahoo.com', '', '', '', '', 0, NULL, 10, 0, 0, 1605872463, 0, 1605872463, NULL, 'null', 0),
(35, '', '', 'Male', 0, 'Single', 0, 'Ibukun', 'UPD8X7kBTTUkMhGxhDQUIOiT68dJSUdm', '$2y$13$xD0y7elqOc/QjaX6rc/y/ejuUoIH.u.BP4mkCEFw3Dyt/pvAaCWym', NULL, 'greatsam300@gmail.com', '2348034632340', '', '', '', 0, NULL, 9, 0, 0, 1606034634, 0, 1606034634, 'zjWGKsUH8dtfTexEgjifMZRqmzMNRz-m_1606034634', 'null', 0),
(36, '', '', 'Male', 0, 'Single', 0, 'sunnieiji', '', '', NULL, 'firstkingskid@yahoo.com', '', '', '', '', 0, NULL, 10, 0, 0, 1606226817, 0, 1606226817, NULL, 'null', 0),
(37, '', '', 'Male', 0, 'Single', 0, 'balamurugan', '', '', NULL, 'balamurugan.aru@gmail.com', '', '', '', '', 0, NULL, 10, 0, 0, 1606311380, 0, 1606311380, NULL, 'null', 0),
(38, 'Bill', 'Iji', 'Male', 47, 'Married', 4, 'paddler', 'HZtA6pRUjGyGYM3UMbU_-ukcT73yjSor', '$2y$13$szsZSaUncBX7oS9J3GS51uiUFcetUvdTdV7h7smneV0Pb0qX9Hd7C', NULL, 'paddlerconsulting@gmail.com', '2348091135112', 'plot 965 aminu kano crescent, wuse 2, abuja', 'fct', 'Abuja', 2, NULL, 10, 2, 750, 1606416363, 0, 1617652056, 'BvVW4FY2n-84RyOU77qWips8U9j7nTnT_1606416363', 'null', 0),
(39, '', '', 'Male', 0, 'Single', 0, 'godwyniji', '', '', NULL, 'mellodee4g@yahoo.com', '', '', '', '', 0, NULL, 10, 0, 0, 1606419961, 0, 1606419961, NULL, 'null', 0),
(40, '', '', 'Male', 0, 'Single', 0, 'jolhd', 'b3GEm_Zq3-SzXMhx0xTqNlm3EW_jDHve', '$2y$13$mo6mtr/fl.v3BXowVyebFePLaNQPrWIKYtfWJgybxznhKkv2kK2K.', NULL, 'judeoyovbaire@gmail.com', '2348091112567', '', '', '', 0, NULL, 10, 0, 0, 1606584017, 0, 1606585072, 'hOvgeW4-tLVH_umEnYYh69ab8JWEpBKY_1606584017', 'null', 0),
(41, '', '', 'Male', 18, 'Single', 1, 'agnesiji', '', '', NULL, 'smashyaggy@yahoo.com', '', '', '', '', 0, NULL, 10, 0, 0, 1608494713, 0, 1608494713, NULL, 'null', 0),
(42, 'Ken', 'Rhymes', 'Male', 18, 'Single', 1, 'kenny5rhymes', 'Z_1F4Kclnzy_ZALotCdd2fdtrkV_vOl5', '$2y$13$1QQ5P4tE7ZsxaoEorE97iuEFK1qDYWPkVucfs9LLO1evQw.baoc/W', NULL, 'kenny5rhymes@gmail.com', '2348181891116', 'Plot 98 Kado-Life Camp Extention', 'Gwarinpa', 'Abuja', 0, NULL, 10, 0, 0, 1609427117, 0, 1615498169, 'xUIUlw2wij_Owcdc_mw3N9s9lo5QXRnJ_1609427117', 'null', 0),
(43, '', '', 'Male', 18, 'Single', 1, 'mahiahmed', '', '$2y$13$ZS2rxzfzc1IufnMRnAMAI.4U0MfWBu2RMQfTzl4TfApLUeAI4BYRq', NULL, 'farjanarahman701@gmail.com', '', '', '', '', 0, NULL, 10, 0, 0, 1614276012, 0, 1614276012, NULL, 'null', 0),
(44, '', '', 'Male', 18, 'Single', 1, 'Lakshmanna', 'hxIML3Ojp0QwwuArJBYOK02OMd6LiDhV', '$2y$13$clIwj9NhpJiLIPdgGXeQ8udKy6Q12TxT7MBnw9izSu1wG9bzTJa76', NULL, 'laxman.mallepuvvu@gmail.com', '2349912016940', '', '', '', 0, NULL, 9, 0, 0, 1615172710, 0, 1615172710, '8JFnBc5UqDCy3-lvPvt657sUAsI7URTY_1615172710', 'null', 0),
(45, '', '', 'Male', 18, 'Single', 1, 'Haymouz', 'kahIIYSQXcli_OOXUZZdVtP4oC6sNnFS', '$2y$13$pn3tILP9Py5ctO3hAmkoHeixW1g1oZPpE5SFQ1fizJp2EG4Oy70Iq', NULL, 'amoz4christh1@gmail.com', '2348168898018', '', '', '', 0, NULL, 10, 0, 0, 1617620412, 0, 1617652942, 'QuZ-SluZ5EqZPO9Uonm3VaCYIX35t30w_1617620412', 'null', 0),
(46, '', '', 'Male', 18, 'Single', 1, 'awesome', 'QFfJej82-SqB3kgAtTGcJ-QznIUZfybC', '$2y$13$jefolsgPPXuZGAlTcX5XYuPsEtGvNKHHJFCMAOGtJ95KT1HUM178.', NULL, 'akpanphilip1122@gmail.com', '2348133441949', '', '', '', 0, NULL, 10, 0, 0, 1617627463, 0, 1617640235, 'u0QQ--GzibN2KUyiDPl9g-ksttqzJk1e_1617627463', 'null', 0),
(47, '', '', 'Male', 18, 'Single', 1, 'okomayinamos', '', '$2y$13$0frQ7EYHOuOIPzb8kan11e671YT0XbeNA7YabZpjfwFwCxMCp2Z5.', NULL, 'amoz4christ@yahoo.com', '', '', '', '', 0, NULL, 10, 0, 0, 1617725797, 0, 1617725797, NULL, 'null', 0),
(48, '', '', 'Male', 18, 'Single', 1, 'ahumibekennedy', '', '$2y$13$B7DyBaJPfWGFnIgaSL5ePeurnJ9dvXLAmNeEGALsgXXGQZ5YWaMWq', NULL, 'kenny5rhymes@yahoo.com', '', '', '', '', 0, NULL, 10, 0, 0, 1617727896, 0, 1617727896, NULL, 'null', 0),
(49, '', '', 'Male', 18, 'Single', 1, 'Lydia', 'DjTKkx-igLdB8FFlUCFbKtyVRiVdFBGZ', '$2y$13$Rib/MzeC9v.YjRnaLkyuJ.4QZoWroh6DRsFyrAsufjqvAgWAKmqW6', NULL, 'lydiaacheneje@gmail.com', '2348165366620', '', '', '', 0, NULL, 10, 0, 0, 1617802438, 0, 1617806634, 'N-kPf7YhoqTheBirLXyqVLl7QoDKpPN7_1617802438', 'null', 0),
(50, 'Food', 'Banking', 'Male', 18, 'Single', 1, 'nb9dq55jjk', '', '$2y$13$7DKDkbJ/ot.nonH6.pKC0uxPQtNFeQMrSecSg.T8CqU0vxZR7ANx2', NULL, 'nb9dq55jjk@privaterelay.appleid.com', '', '', '', '', 0, NULL, 10, 0, 0, 1618088835, 0, 1618088835, NULL, 'null', 0),
(51, '', '', 'Male', 18, 'Single', 1, 'winfredabba', '', '$2y$13$eNf1n9/AYz9loYBAcedrWu/17nLma3gpFLWV2kvZ4CsS8JgT32TJm', NULL, 'winfredabba@gmail.com', '', '', '', '', 0, NULL, 10, 0, 0, 1618093802, 0, 1618093802, NULL, 'null', 0),
(52, 'Amos', 'Okomayin', 'Male', 18, 'Single', 1, 'amosokomayin', '', '$2y$13$ZXek6BYb3oPoUV39za6zs.cLOKEXjhmpi7YIX4kPuASe8w4FVmC..', NULL, 'amosokomayin@gmail.com', '', '', '', '', 0, NULL, 10, 0, 0, 1618122439, 0, 1618122439, NULL, 'null', 0),
(53, 'Oyinkansola', 'Okomayin', 'Male', 29, 'Single', 0, 'pen', NULL, '$2a$10$2Nf6JFXwepeDxJUEIdvIGO5eiQIZKbFS3NAL9s5T.FKc4NszH4Opm', NULL, 'penregal@gmail.com', '234816889887', 'CBQ 20 NA Cant. Ikj. Lag.', 'Ikeja', 'Lagos', 1, '2022-11-11', NULL, NULL, 9912, NULL, 0, NULL, NULL, '0274', 1),
(54, NULL, NULL, NULL, NULL, NULL, NULL, 'haymouz1', NULL, '$2a$10$c0taf11/bBWazWBpT.w4fu/DfE6fa/ebOBWMBjGY2zhv8rwgZJRNy', NULL, 'amoz4chris@gmail.com', '2348168898056', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '0899', 0),
(55, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, '111111', NULL, 'node@js.com', '8168898016', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 'null', 0),
(56, NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, '111111', NULL, 'node@js.com', '8168898016', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 'null', 0),
(57, 'node', 'js', NULL, NULL, NULL, NULL, 'qewr', NULL, '111111', NULL, 'node@js.com', '8168898016', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 'null', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_history`
--

CREATE TABLE `user_history` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_history`
--

INSERT INTO `user_history` (`id`, `user_id`, `description`, `created_at`) VALUES
(1, 53, 'pen -Made a withdrawal Request: null', '2021-05-10 11:04:46'),
(2, 53, 'pen -New subscription from  with Package ID: 1', '2021-05-10 11:23:23'),
(3, 53, 'pen -New subscription from  with Package ID: 1', '2021-05-10 11:44:36');

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(53, 1),
(54, 3);

-- --------------------------------------------------------

--
-- Table structure for table `withdraw`
--

CREATE TABLE `withdraw` (
  `id` int(11) NOT NULL,
  `created_at` bigint(15) NOT NULL,
  `created_by` int(11) UNSIGNED NOT NULL,
  `status` enum('Pending','Processing','Completed','Delivered') COLLATE utf8mb4_unicode_ci NOT NULL,
  `delivery_date` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` bigint(15) NOT NULL,
  `updated_by` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `withdraw`
--

INSERT INTO `withdraw` (`id`, `created_at`, `created_by`, `status`, `delivery_date`, `updated_at`, `updated_by`) VALUES
(31, 1612204764, 38, 'Completed', '1613278800', 1615295841, 38),
(32, 1612205987, 38, 'Pending', '1613278800', 1612205987, 38),
(33, 1617652056, 38, 'Pending', '1617768000', 1617652056, 38),
(34, 1619683388376, 53, 'Pending', '1617652056', 1619683388376, 53),
(35, 1619684130369, 53, 'Pending', '1617652056', 1619684130369, 53),
(36, 1619684230564, 53, 'Pending', '1617652056', 1619684230564, 53),
(37, 1619684625239, 53, 'Pending', '1617652056', 1619684625239, 53),
(38, 1619684665747, 53, 'Pending', '1617652056', 1619684665747, 53),
(39, 1619684737818, 53, 'Pending', '1617652056', 1619684737818, 53),
(40, 1619684760797, 53, 'Pending', '1617652056', 1619684760797, 53),
(41, 1620644306350, 53, 'Pending', '2', 1620644306350, 53),
(42, 1620644686232, 53, 'Pending', '2', 1620644686232, 53);

-- --------------------------------------------------------

--
-- Table structure for table `withdrawal_item`
--

CREATE TABLE `withdrawal_item` (
  `id` bigint(20) NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `quantity` bigint(20) DEFAULT NULL,
  `withdrawal_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `withdrawal_item`
--

INSERT INTO `withdrawal_item` (`id`, `item_id`, `quantity`, `withdrawal_id`) VALUES
(1, 10, 1, 53),
(2, 1, 1, 53),
(3, 10, 1, 53);

-- --------------------------------------------------------

--
-- Table structure for table `withdraw_item`
--

CREATE TABLE `withdraw_item` (
  `id` int(11) NOT NULL,
  `withdraw_id` int(11) NOT NULL,
  `item_id` int(10) UNSIGNED NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `withdraw_item`
--

INSERT INTO `withdraw_item` (`id`, `withdraw_id`, `item_id`, `quantity`) VALUES
(134, 31, 4, 1),
(135, 31, 7, 1),
(136, 31, 8, 1),
(137, 31, 9, 1),
(138, 31, 11, 1),
(139, 31, 13, 1),
(140, 31, 14, 1),
(141, 32, 4, 1),
(142, 32, 7, 1),
(143, 32, 8, 1),
(144, 32, 9, 1),
(145, 32, 11, 1),
(146, 32, 13, 1),
(147, 32, 14, 1),
(148, 33, 4, 1),
(149, 33, 7, 1),
(150, 33, 8, 1),
(151, 33, 9, 1),
(152, 33, 11, 1),
(153, 33, 13, 1),
(154, 33, 14, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `auth`
--
ALTER TABLE `auth`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_auth_user_id_user` (`user_id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `deposit`
--
ALTER TABLE `deposit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `deposit_user` (`user_id`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `message_read`
--
ALTER TABLE `message_read`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk-message` (`message_id`);

--
-- Indexes for table `migration`
--
ALTER TABLE `migration`
  ADD PRIMARY KEY (`version`);

--
-- Indexes for table `package`
--
ALTER TABLE `package`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `package_item`
--
ALTER TABLE `package_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `package` (`package_id`),
  ADD KEY `item` (`item_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `setting`
--
ALTER TABLE `setting`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`,`password_reset_token`,`email`),
  ADD KEY `user_package` (`package_id`);

--
-- Indexes for table `user_history`
--
ALTER TABLE `user_history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- Indexes for table `withdraw`
--
ALTER TABLE `withdraw`
  ADD PRIMARY KEY (`id`),
  ADD KEY `withdraw_user` (`created_by`);

--
-- Indexes for table `withdrawal_item`
--
ALTER TABLE `withdrawal_item`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `withdraw_item`
--
ALTER TABLE `withdraw_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `item_withdrew` (`item_id`),
  ADD KEY `withdrawal` (`withdraw_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `auth`
--
ALTER TABLE `auth`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=97;

--
-- AUTO_INCREMENT for table `deposit`
--
ALTER TABLE `deposit`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `message_read`
--
ALTER TABLE `message_read`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `package`
--
ALTER TABLE `package`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `package_item`
--
ALTER TABLE `package_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT for table `user_history`
--
ALTER TABLE `user_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `withdraw`
--
ALTER TABLE `withdraw`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `withdrawal_item`
--
ALTER TABLE `withdrawal_item`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `withdraw_item`
--
ALTER TABLE `withdraw_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=155;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `auth`
--
ALTER TABLE `auth`
  ADD CONSTRAINT `fk_auth_user_id_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `deposit`
--
ALTER TABLE `deposit`
  ADD CONSTRAINT `deposit_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `message_read`
--
ALTER TABLE `message_read`
  ADD CONSTRAINT `fk-message` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `package_item`
--
ALTER TABLE `package_item`
  ADD CONSTRAINT `item` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `package` FOREIGN KEY (`package_id`) REFERENCES `package` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

--
-- Constraints for table `withdraw`
--
ALTER TABLE `withdraw`
  ADD CONSTRAINT `withdraw_user` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`);

--
-- Constraints for table `withdraw_item`
--
ALTER TABLE `withdraw_item`
  ADD CONSTRAINT `item_withdrew` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `withdrawal` FOREIGN KEY (`withdraw_id`) REFERENCES `withdraw` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
