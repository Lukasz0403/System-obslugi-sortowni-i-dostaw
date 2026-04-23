-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Apr 23, 2026 at 05:55 PM
-- Wersja serwera: 9.6.0
-- Wersja PHP: 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `package_storage`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `courier_regions`
--

CREATE TABLE `courier_regions` (
  `region_id` char(3) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `courier_regions`
--

INSERT INTO `courier_regions` (`region_id`) VALUES
('BIA'),
('BYD'),
('CZE'),
('GDA'),
('GDY'),
('KAT'),
('KIE'),
('KRK'),
('LOD'),
('LUB'),
('OLS'),
('OPL'),
('POZ'),
('RZE'),
('SOP'),
('SZC'),
('TOR'),
('WAW'),
('WRO'),
('ZIE');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `packages`
--

CREATE TABLE `packages` (
  `package_id` bigint UNSIGNED NOT NULL,
  `package_sender` int NOT NULL,
  `package_recipient` int NOT NULL,
  `width` int UNSIGNED NOT NULL,
  `height` int UNSIGNED NOT NULL,
  `depth` int UNSIGNED NOT NULL,
  `weight` int UNSIGNED NOT NULL,
  `package_region` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `package_dest_region` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `package_format` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `package_shelf` int DEFAULT NULL,
  `label` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `packages`
--

INSERT INTO `packages` (`package_id`, `package_sender`, `package_recipient`, `width`, `height`, `depth`, `weight`, `package_region`, `package_dest_region`, `package_format`, `package_shelf`, `label`) VALUES
(1, 3, 3, 15, 30, 25, 2, 'BIA', 'SZC', 'M', NULL, NULL),
(2, 2, 7, 5, 40, 40, 5, 'GDA', 'TOR', 'S', NULL, NULL);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `package_formats`
--

CREATE TABLE `package_formats` (
  `format_id` varchar(3) COLLATE utf8mb4_general_ci NOT NULL,
  `max_format_width` int UNSIGNED NOT NULL,
  `max_format_height` int UNSIGNED NOT NULL,
  `max_format_depth` int UNSIGNED NOT NULL,
  `max_weight` int UNSIGNED NOT NULL,
  `slot_coverage` int UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `package_formats`
--

INSERT INTO `package_formats` (`format_id`, `max_format_width`, `max_format_height`, `max_format_depth`, `max_weight`, `slot_coverage`) VALUES
('L', 42, 40, 65, 20, 4),
('M', 20, 40, 65, 20, 2),
('S', 9, 40, 65, 20, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `racks`
--

CREATE TABLE `racks` (
  `rack_id` int NOT NULL,
  `zone` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `racks`
--

INSERT INTO `racks` (`rack_id`, `zone`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 3),
(10, 3),
(11, 3),
(12, 3),
(13, 4),
(14, 4),
(15, 4),
(16, 4),
(17, 5),
(18, 5),
(19, 5),
(20, 5);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `recipients`
--

CREATE TABLE `recipients` (
  `recipient_id` int NOT NULL,
  `recipient_name` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
  `recipient_city` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `recipient_street` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `recipient_postcode` char(6) COLLATE utf8mb4_general_ci NOT NULL,
  `recipient_email` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `recipient_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `recipients`
--

INSERT INTO `recipients` (`recipient_id`, `recipient_name`, `recipient_city`, `recipient_street`, `recipient_postcode`, `recipient_email`, `recipient_phone`) VALUES
(1, 'OdbiorcaFirma', 'Toszek', 'Foliowa', '99-999', NULL, NULL),
(2, 'Piotr Zieliński', 'Gdańsk', 'Morska 7', '80-004', 'piotr@example.com', '222333444'),
(3, 'Katarzyna Wiśniewska', 'Wrocław', 'Słoneczna 12', '50-005', 'kasia@example.com', '333444555'),
(4, 'Firma XYZ', 'Berlin', 'Hauptstrasse 20', '10115', 'kontakt@xyz.de', '444555666'),
(5, 'Piotr Zieliński', 'Gdańsk', 'Morska 7', '80-004', 'piotr@example.com', '222333444'),
(6, 'Katarzyna Wiśniewska', 'Wrocław', 'Słoneczna 12', '50-005', 'kasia@example.com', '333444555'),
(7, 'Firma XYZ', 'Berlin', 'Hauptstrasse 20', '10115', 'kontakt@xyz.de', '444555666');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `senders`
--

CREATE TABLE `senders` (
  `sender_id` int NOT NULL,
  `sender_name` varchar(40) COLLATE utf8mb4_general_ci NOT NULL,
  `sender_city` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `sender_street` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sender_postcode` char(6) COLLATE utf8mb4_general_ci NOT NULL,
  `sender_email` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sender_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `senders`
--

INSERT INTO `senders` (`sender_id`, `sender_name`, `sender_city`, `sender_street`, `sender_postcode`, `sender_email`, `sender_phone`) VALUES
(1, 'MojaFrima', 'Choroszcz', 'Chlebowa', '69-666', NULL, NULL),
(2, 'Jan Kowalski', 'Warszawa', 'Kwiatowa 1', '00-001', 'jan@example.com', '123456789'),
(3, 'Anna Nowak', 'Kraków', 'Lipowa 5', '30-002', 'anna@example.com', '987654321'),
(4, 'Firma ABC', 'Poznań', 'Przemysłowa 10', '60-003', 'abc@firma.com', '111222333'),
(5, 'Jan Kowalski', 'Warszawa', 'Kwiatowa 1', '00-001', 'jan@example.com', '123456789'),
(6, 'Anna Nowak', 'Kraków', 'Lipowa 5', '30-002', 'anna@example.com', '987654321'),
(7, 'Firma ABC', 'Poznań', 'Przemysłowa 10', '60-003', 'abc@firma.com', '111222333');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `shelves`
--

CREATE TABLE `shelves` (
  `shelf_id` int NOT NULL,
  `rack` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `shelves`
--

INSERT INTO `shelves` (`shelf_id`, `rack`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 2),
(12, 2),
(13, 2),
(14, 2),
(15, 2),
(16, 2),
(17, 2),
(18, 2),
(19, 2),
(20, 2),
(21, 3),
(22, 3),
(23, 3),
(24, 3),
(25, 3),
(26, 3),
(27, 3),
(28, 3),
(29, 3),
(30, 3),
(31, 4),
(32, 4),
(33, 4),
(34, 4),
(35, 4),
(36, 4),
(37, 4),
(38, 4),
(39, 4),
(40, 4),
(41, 5),
(42, 5),
(43, 5),
(44, 5),
(45, 5),
(46, 5),
(47, 5),
(48, 5),
(49, 5),
(50, 5),
(51, 6),
(52, 6),
(53, 6),
(54, 6),
(55, 6),
(56, 6),
(57, 6),
(58, 6),
(59, 6),
(60, 6),
(61, 7),
(62, 7),
(63, 7),
(64, 7),
(65, 7),
(66, 7),
(67, 7),
(68, 7),
(69, 7),
(70, 7),
(71, 8),
(72, 8),
(73, 8),
(74, 8),
(75, 8),
(76, 8),
(77, 8),
(78, 8),
(79, 8),
(80, 8),
(81, 9),
(82, 9),
(83, 9),
(84, 9),
(85, 9),
(86, 9),
(87, 9),
(88, 9),
(89, 9),
(90, 9),
(91, 10),
(92, 10),
(93, 10),
(94, 10),
(95, 10),
(96, 10),
(97, 10),
(98, 10),
(99, 10),
(100, 10),
(101, 11),
(102, 11),
(103, 11),
(104, 11),
(105, 11),
(106, 11),
(107, 11),
(108, 11),
(109, 11),
(110, 11),
(111, 12),
(112, 12),
(113, 12),
(114, 12),
(115, 12),
(116, 12),
(117, 12),
(118, 12),
(119, 12),
(120, 12),
(121, 13),
(122, 13),
(123, 13),
(124, 13),
(125, 13),
(126, 13),
(127, 13),
(128, 13),
(129, 13),
(130, 13),
(131, 14),
(132, 14),
(133, 14),
(134, 14),
(135, 14),
(136, 14),
(137, 14),
(138, 14),
(139, 14),
(140, 14),
(141, 15),
(142, 15),
(143, 15),
(144, 15),
(145, 15),
(146, 15),
(147, 15),
(148, 15),
(149, 15),
(150, 15),
(151, 16),
(152, 16),
(153, 16),
(154, 16),
(155, 16),
(156, 16),
(157, 16),
(158, 16),
(159, 16),
(160, 16),
(161, 17),
(162, 17),
(163, 17),
(164, 17),
(165, 17),
(166, 17),
(167, 17),
(168, 17),
(169, 17),
(170, 17),
(171, 18),
(172, 18),
(173, 18),
(174, 18),
(175, 18),
(176, 18),
(177, 18),
(178, 18),
(179, 18),
(180, 18),
(181, 19),
(182, 19),
(183, 19),
(184, 19),
(185, 19),
(186, 19),
(187, 19),
(188, 19),
(189, 19),
(190, 19),
(191, 20),
(192, 20),
(193, 20),
(194, 20),
(195, 20),
(196, 20),
(197, 20),
(198, 20),
(199, 20),
(200, 20);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `zones`
--

CREATE TABLE `zones` (
  `zone_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `zones`
--

INSERT INTO `zones` (`zone_id`) VALUES
(1),
(2),
(3),
(4),
(5);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `courier_regions`
--
ALTER TABLE `courier_regions`
  ADD PRIMARY KEY (`region_id`);

--
-- Indeksy dla tabeli `packages`
--
ALTER TABLE `packages`
  ADD PRIMARY KEY (`package_id`),
  ADD KEY `FK_SENDER` (`package_sender`),
  ADD KEY `FK_RECIPIENT` (`package_recipient`),
  ADD KEY `FK_REGION` (`package_region`),
  ADD KEY `FK_FROMAT` (`package_format`),
  ADD KEY `packages_ibfk_6` (`package_shelf`),
  ADD KEY `packages_ibfk_4` (`package_dest_region`);

--
-- Indeksy dla tabeli `package_formats`
--
ALTER TABLE `package_formats`
  ADD PRIMARY KEY (`format_id`);

--
-- Indeksy dla tabeli `racks`
--
ALTER TABLE `racks`
  ADD PRIMARY KEY (`rack_id`),
  ADD KEY `zone_id` (`zone`);

--
-- Indeksy dla tabeli `recipients`
--
ALTER TABLE `recipients`
  ADD PRIMARY KEY (`recipient_id`);

--
-- Indeksy dla tabeli `senders`
--
ALTER TABLE `senders`
  ADD PRIMARY KEY (`sender_id`);

--
-- Indeksy dla tabeli `shelves`
--
ALTER TABLE `shelves`
  ADD PRIMARY KEY (`shelf_id`),
  ADD KEY `RACK_FK` (`rack`);

--
-- Indeksy dla tabeli `zones`
--
ALTER TABLE `zones`
  ADD PRIMARY KEY (`zone_id`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `packages`
--
ALTER TABLE `packages`
  MODIFY `package_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT dla tabeli `racks`
--
ALTER TABLE `racks`
  MODIFY `rack_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT dla tabeli `recipients`
--
ALTER TABLE `recipients`
  MODIFY `recipient_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT dla tabeli `senders`
--
ALTER TABLE `senders`
  MODIFY `sender_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT dla tabeli `shelves`
--
ALTER TABLE `shelves`
  MODIFY `shelf_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=256;

--
-- AUTO_INCREMENT dla tabeli `zones`
--
ALTER TABLE `zones`
  MODIFY `zone_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `packages`
--
ALTER TABLE `packages`
  ADD CONSTRAINT `packages_ibfk_1` FOREIGN KEY (`package_sender`) REFERENCES `senders` (`sender_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_2` FOREIGN KEY (`package_recipient`) REFERENCES `recipients` (`recipient_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_3` FOREIGN KEY (`package_format`) REFERENCES `package_formats` (`format_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_4` FOREIGN KEY (`package_shelf`) REFERENCES `shelves` (`shelf_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_5` FOREIGN KEY (`package_region`) REFERENCES `courier_regions` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_6` FOREIGN KEY (`package_dest_region`) REFERENCES `courier_regions` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ograniczenia dla tabeli `racks`
--
ALTER TABLE `racks`
  ADD CONSTRAINT `racks_ibfk_1` FOREIGN KEY (`zone`) REFERENCES `zones` (`zone_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ograniczenia dla tabeli `shelves`
--
ALTER TABLE `shelves`
  ADD CONSTRAINT `shelves_ibfk_1` FOREIGN KEY (`rack`) REFERENCES `racks` (`rack_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
