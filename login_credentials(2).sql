-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Maj 01, 2026 at 12:18 PM
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
-- Baza danych: `login_credentials`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `permissions`
--

CREATE TABLE `permissions` (
  `permission_id` int NOT NULL,
  `name` varchar(30) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `permissions`
--

INSERT INTO `permissions` (`permission_id`, `name`) VALUES
(1, 'pracownik'),
(2, 'administrator');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `login` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `permission` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`user_id`, `login`, `password`, `permission`) VALUES
(1, 'aaa', '$2a$12$sK/KFRp.CzsxyZJsjt1FQuUgy4fCiXWKVdwb0v.8Do7p56FTI0Sma', 1),
(2, 'bbb', '$2a$12$0I3GOnC.wVLAoN1rgg6uiOiRcpG5n/MvF0aRUzc2tCoGwxMlEEXri', 1),
(3, 'bobek', '$2a$12$b9NoA5kdRdC2OtZk9BuUeul.JqqD27hXS2FcWUTNmlM6RjXgsoj7i', 1),
(4, 'jacek', '$2a$12$Pw6lCa4EOr93X6w43we/Guem4pIhkjARJdB.HYcY2LUat1RDC3ux6', 2),
(5, 'mietek', '1234', 1),
(6, 'radek', '$2a$12$lgKTJ7UDEDwYzsuD9xglJOa.TFEQbrHonMDdsecOMUFrCxBXtKGAm', 2),
(7, 'synek', '$2a$12$QDEiWtKZ5iGI0eedwW7wde7fsLCyEP3D213vuojlGU3wmH8oUOHpC', 1),
(8, 'trfdrbfd', '$2a$12$DbjQWljuToLcJAt1Wudf/.4U2rviFgiq75.NOa2DtDitmDXEvTjyu', 1),
(9, 'sss', '$2a$12$MG4OTtOxOwnRPEe8YZeLB.O5/SX0bg4NbYylqZ.0kRN21sj1VvjWq', 1);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `permissions`
--
ALTER TABLE `permissions`
  ADD PRIMARY KEY (`permission_id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `USER_UNIQUE` (`login`),
  ADD KEY `FK_PERMSSIONS` (`permission`) USING BTREE;

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `permissions`
--
ALTER TABLE `permissions`
  MODIFY `permission_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`permission`) REFERENCES `permissions` (`permission_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
