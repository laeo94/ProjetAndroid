-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le :  sam. 23 mars 2019 à 13:37
-- Version du serveur :  5.7.8-rc
-- Version de PHP :  7.0.27-1+ubuntu14.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `u21505006`
--

-- --------------------------------------------------------

--
-- Structure de la table `account`
--

CREATE TABLE `account` (
  `aid` int(10) UNSIGNED NOT NULL,
  `title` varchar(40) NOT NULL,
  `description` text NOT NULL,
  `device` enum('€','$','£') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `account`
--

INSERT INTO `account` (`aid`, `title`, `description`, `device`) VALUES
(1, 'Week-end', 'week-end du 18 juillet à Londre', '£'),
(2, 'Fac Crous', 'Repas du crous', '€'),
(3, 'Plage', 'Plage Dauville', '€'),
(4, 'Montagne', 'Semaine du 15 janvier', '$');

-- --------------------------------------------------------

--
-- Structure de la table `depense`
--

CREATE TABLE `depense` (
  `did` int(20) NOT NULL,
  `aid` int(20) NOT NULL,
  `idfrom` int(20) NOT NULL,
  `idto` int(20) NOT NULL,
  `somme` int(20) UNSIGNED NOT NULL,
  `statut` enum('in_progress','paid') NOT NULL,
  `detail` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `depense`
--

INSERT INTO `depense` (`did`, `aid`, `idfrom`, `idto`, `somme`, `statut`, `detail`) VALUES
(1, 1, 1, 2, 10, 'in_progress', 'nourriture repas mid'),
(2, 2, 1, 2, 3, 'in_progress', 'crous 15 mars'),
(3, 2, 2, 1, 3, 'in_progress', 'crous 16 mars'),
(4, 2, 1, 2, 10, 'paid', 'crous 17 mars'),
(5, 2, 1, 2, 3, 'in_progress', 'crous 10 mars'),
(6, 2, 2, 1, 5, 'paid', 'crous 12 mars');

-- --------------------------------------------------------

--
-- Structure de la table `participations`
--

CREATE TABLE `participations` (
  `ppid` int(11) NOT NULL,
  `aid` int(11) NOT NULL,
  `uid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `participations`
--

INSERT INTO `participations` (`ppid`, `aid`, `uid`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 1, 2),
(5, 2, 2),
(6, 4, 2),
(7, 2, 3),
(8, 3, 4);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `uid` int(11) NOT NULL,
  `pseudo` varchar(40) NOT NULL,
  `mdp` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`uid`, `pseudo`, `mdp`) VALUES
(1, 'lea', '$2y$10$CmiwhpkKt9QMIK5R7O7pDu7t758HaHRLTgdzZP/qDlmuvV9QHGlxa'),
(2, 'nayani', '$2y$10$EUH7z.tLtVi1fNAbfYJjU.ZKdgd6LUJf9Dw.UT99WWgYt1PZBqXli'),
(3, 'user1', '$2y$10$RBqo7/MMNQWrcmT7nI8gPeoqoFz8qhTYllWubVfcRD1zTEOH2f33y'),
(4, 'user2', '$2y$10$NKzLET12ADOLfUD.R8QXmOLUWzas1jev/I4KGF6G4/hdAkNmQg2Eu');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`aid`);

--
-- Index pour la table `depense`
--
ALTER TABLE `depense`
  ADD PRIMARY KEY (`did`);

--
-- Index pour la table `participations`
--
ALTER TABLE `participations`
  ADD PRIMARY KEY (`ppid`),
  ADD KEY `aid` (`aid`),
  ADD KEY `uid` (`uid`) USING BTREE;

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `account`
--
ALTER TABLE `account`
  MODIFY `aid` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT pour la table `depense`
--
ALTER TABLE `depense`
  MODIFY `did` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT pour la table `participations`
--
ALTER TABLE `participations`
  MODIFY `ppid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
