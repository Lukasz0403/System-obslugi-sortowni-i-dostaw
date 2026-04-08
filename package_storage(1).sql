-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Apr 08, 2026 at 12:22 PM
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
  `region_id` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `courier_regions`
--

INSERT INTO `courier_regions` (`region_id`) VALUES
('CZ1'),
('DE1'),
('GA2'),
('PL1'),
('PL2'),
('PL3'),
('RB4'),
('WA8');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `packages`
--

CREATE TABLE `packages` (
  `package_id` int NOT NULL,
  `package_sender` int NOT NULL,
  `package_recipient` int NOT NULL,
  `package_region` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `package_dest_region` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `package_format` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `package_rack` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `packages`
--

INSERT INTO `packages` (`package_id`, `package_sender`, `package_recipient`, `package_region`, `package_dest_region`, `package_format`, `package_rack`) VALUES
(1, 1, 1, 'RB4', 'WA8', 'A', NULL),
(2, 1, 1, 'PL1', 'PL3', 'S', 101),
(3, 2, 2, 'PL2', 'PL1', 'M', 102),
(4, 3, 3, 'PL3', 'DE1', 'L', 201),
(5, 1, 2, 'PL1', 'PL2', 'M', 202),
(6, 2, 3, 'PL2', 'CZ1', 'S', 301);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `package_formats`
--

CREATE TABLE `package_formats` (
  `format_id` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `max_format_width` int UNSIGNED NOT NULL,
  `max_format_height` int UNSIGNED NOT NULL,
  `max_format_depth` int UNSIGNED NOT NULL,
  `max_wage` int UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `package_formats`
--

INSERT INTO `package_formats` (`format_id`, `max_format_width`, `max_format_height`, `max_format_depth`, `max_wage`) VALUES
('A', 53, 63, 23, 2),
('L', 100, 100, 100, 30),
('M', 60, 60, 60, 10),
('S', 30, 30, 30, 2);

-- --------------------------------------------------------

--
-- Zastąpiona struktura widoku `PACKAGE_VIEW`
-- (See below for the actual view)
--
CREATE TABLE `PACKAGE_VIEW` (
`package_dest_region` char(3)
,`package_id` int
,`package_region` char(3)
,`recipient_city` varchar(30)
,`recipient_email` varchar(30)
,`recipient_name` varchar(40)
,`recipient_phone` varchar(20)
,`recipient_postcode` char(6)
,`recipient_street` varchar(30)
,`sender_city` varchar(30)
,`sender_email` varchar(20)
,`sender_name` varchar(40)
,`sender_phone` varchar(20)
,`sender_postcode` char(6)
,`sender_street` varchar(30)
);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `recipients`
--

CREATE TABLE `recipients` (
  `recipient_id` int NOT NULL,
  `recipient_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `recipient_city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `recipient_street` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `recipient_postcode` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `recipient_email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `recipient_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL
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
  `sender_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sender_city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sender_street` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sender_postcode` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sender_email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sender_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL
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
  `zone` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `shelves`
--

INSERT INTO `shelves` (`shelf_id`, `zone`) VALUES
(101, 1),
(102, 1),
(201, 2),
(202, 2),
(301, 3);

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
(3);

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
  ADD KEY `packages_ibfk_5` (`package_dest_region`),
  ADD KEY `packages_ibfk_6` (`package_rack`);

--
-- Indeksy dla tabeli `package_formats`
--
ALTER TABLE `package_formats`
  ADD PRIMARY KEY (`format_id`);

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
  ADD KEY `ZONE_FK` (`zone`);

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
  MODIFY `package_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

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
  MODIFY `shelf_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=302;

--
-- AUTO_INCREMENT dla tabeli `zones`
--
ALTER TABLE `zones`
  MODIFY `zone_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

-- --------------------------------------------------------

--
-- Struktura widoku `PACKAGE_VIEW`
--
DROP TABLE IF EXISTS `PACKAGE_VIEW`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `PACKAGE_VIEW`  AS SELECT `p`.`package_id` AS `package_id`, `s`.`sender_name` AS `sender_name`, `s`.`sender_city` AS `sender_city`, `s`.`sender_street` AS `sender_street`, `s`.`sender_postcode` AS `sender_postcode`, `s`.`sender_email` AS `sender_email`, `s`.`sender_phone` AS `sender_phone`, `r`.`recipient_name` AS `recipient_name`, `r`.`recipient_city` AS `recipient_city`, `r`.`recipient_street` AS `recipient_street`, `r`.`recipient_postcode` AS `recipient_postcode`, `r`.`recipient_email` AS `recipient_email`, `r`.`recipient_phone` AS `recipient_phone`, `p`.`package_region` AS `package_region`, `p`.`package_dest_region` AS `package_dest_region` FROM ((`packages` `p` join `senders` `s` on((`p`.`package_sender` = `s`.`sender_id`))) join `recipients` `r` on((`p`.`package_recipient` = `r`.`recipient_id`))) ;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `packages`
--
ALTER TABLE `packages`
  ADD CONSTRAINT `packages_ibfk_1` FOREIGN KEY (`package_recipient`) REFERENCES `recipients` (`recipient_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `packages_ibfk_2` FOREIGN KEY (`package_region`) REFERENCES `courier_regions` (`region_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `packages_ibfk_3` FOREIGN KEY (`package_sender`) REFERENCES `senders` (`sender_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `packages_ibfk_4` FOREIGN KEY (`package_format`) REFERENCES `package_formats` (`format_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `packages_ibfk_5` FOREIGN KEY (`package_dest_region`) REFERENCES `courier_regions` (`region_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `packages_ibfk_6` FOREIGN KEY (`package_rack`) REFERENCES `shelves` (`shelf_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Ograniczenia dla tabeli `shelves`
--
ALTER TABLE `shelves`
  ADD CONSTRAINT `shelves_ibfk_1` FOREIGN KEY (`zone`) REFERENCES `zones` (`zone_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
