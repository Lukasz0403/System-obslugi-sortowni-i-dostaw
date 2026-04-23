-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Apr 23, 2026 at 07:49 PM
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
  `region_id` int NOT NULL,
  `region_name` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `courier_regions`
--

INSERT INTO `courier_regions` (`region_id`, `region_name`) VALUES
(1, 'BIA'),
(2, 'BYD'),
(3, 'CZE'),
(4, 'GDA'),
(5, 'GDY'),
(6, 'KAT'),
(7, 'KIE'),
(8, 'KRK'),
(9, 'LOD'),
(10, 'LUB'),
(11, 'OLS'),
(12, 'OPL'),
(13, 'POZ'),
(14, 'RZE'),
(15, 'SOP'),
(16, 'SZC'),
(17, 'TOR'),
(18, 'WAW'),
(19, 'WRO'),
(20, 'ZIE');

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
  `package_region` int NOT NULL,
  `package_dest_region` int NOT NULL,
  `package_format` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `package_rack` int DEFAULT NULL,
  `label` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `packages`
--

INSERT INTO `packages` (`package_id`, `package_sender`, `package_recipient`, `width`, `height`, `depth`, `weight`, `package_region`, `package_dest_region`, `package_format`, `package_rack`, `label`) VALUES
(1, 3, 3, 10, 40, 60, 2, 3, 17, 'A', NULL, NULL),
(2, 4, 4, 20, 40, 60, 5, 2, 16, 'B', NULL, NULL),
(3, 2, 5, 20, 40, 60, 5, 8, 17, 'C', NULL, NULL);

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
('A', 10, 40, 60, 20, 1),
('B', 20, 40, 60, 20, 2),
('C', 40, 40, 60, 20, 4);

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
  ADD KEY `FK_FROMAT` (`package_format`),
  ADD KEY `packages_ibfk_6` (`package_rack`),
  ADD KEY `packages_ibfk_5` (`package_region`),
  ADD KEY `packages_ibfk_7` (`package_dest_region`);

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
-- Indeksy dla tabeli `zones`
--
ALTER TABLE `zones`
  ADD PRIMARY KEY (`zone_id`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `courier_regions`
--
ALTER TABLE `courier_regions`
  MODIFY `region_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT dla tabeli `packages`
--
ALTER TABLE `packages`
  MODIFY `package_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

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
  ADD CONSTRAINT `packages_ibfk_5` FOREIGN KEY (`package_region`) REFERENCES `courier_regions` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_6` FOREIGN KEY (`package_rack`) REFERENCES `racks` (`rack_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_7` FOREIGN KEY (`package_dest_region`) REFERENCES `courier_regions` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ograniczenia dla tabeli `racks`
--
ALTER TABLE `racks`
  ADD CONSTRAINT `racks_ibfk_1` FOREIGN KEY (`zone`) REFERENCES `zones` (`zone_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
