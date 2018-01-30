-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: szkola
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `klasy`
--

DROP TABLE IF EXISTS `klasy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `klasy` (
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `numer` tinyint(1) DEFAULT NULL,
  `litera` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_index` (`numer`,`litera`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `klasy`
--

LOCK TABLES `klasy` WRITE;
/*!40000 ALTER TABLE `klasy` DISABLE KEYS */;
INSERT INTO `klasy` VALUES (2,1,'A'),(4,1,'B'),(5,2,'A'),(6,2,'B'),(7,3,'A'),(8,3,'B');
/*!40000 ALTER TABLE `klasy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lekcje`
--

DROP TABLE IF EXISTS `lekcje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lekcje` (
  `data_godzina` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id_przedmiotu` mediumint(9) NOT NULL,
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`data_godzina`,`id_przedmiotu`),
  KEY `fk_id_przedmiotu` (`id_przedmiotu`),
  CONSTRAINT `fk_id_przedmiotu` FOREIGN KEY (`id_przedmiotu`) REFERENCES `przedmioty_w_planach` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lekcje`
--

LOCK TABLES `lekcje` WRITE;
/*!40000 ALTER TABLE `lekcje` DISABLE KEYS */;
INSERT INTO `lekcje` VALUES ('2018-01-09 11:30:00',5,1),('2018-01-09 13:30:00',6,2),('2018-01-27 13:12:36',9,3),('2018-01-28 14:35:21',3,4),('2018-01-28 14:36:10',9,5),('2018-01-28 14:36:43',3,6),('2018-01-28 14:36:46',3,7);
/*!40000 ALTER TABLE `lekcje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `obecnosci`
--

DROP TABLE IF EXISTS `obecnosci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `obecnosci` (
  `id_lekcji` mediumint(9) NOT NULL,
  `czy_obecny` tinyint(1) NOT NULL DEFAULT '0',
  `id_ucznia` mediumint(9) NOT NULL,
  PRIMARY KEY (`id_lekcji`,`id_ucznia`),
  KEY `fk_obecnosci_osoby` (`id_ucznia`),
  CONSTRAINT `fk_obecnosci_osoby` FOREIGN KEY (`id_ucznia`) REFERENCES `osoby` (`id`),
  CONSTRAINT `fk_osoby_lekcje` FOREIGN KEY (`id_lekcji`) REFERENCES `lekcje` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `obecnosci`
--

LOCK TABLES `obecnosci` WRITE;
/*!40000 ALTER TABLE `obecnosci` DISABLE KEYS */;
INSERT INTO `obecnosci` VALUES (3,1,1),(3,0,7),(3,1,8),(4,1,1),(4,0,7),(4,0,8),(5,0,1),(5,0,7),(5,0,8),(6,0,1),(6,0,7),(6,0,8),(7,0,1),(7,0,7),(7,0,8);
/*!40000 ALTER TABLE `obecnosci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oceny`
--

DROP TABLE IF EXISTS `oceny`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oceny` (
  `nazwa_przedmiotu` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `id_osoby` mediumint(9) NOT NULL,
  `stopien` mediumint(9) NOT NULL,
  `id_oceny` mediumint(9) NOT NULL AUTO_INCREMENT,
  `data` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_oceny`),
  KEY `fk_przedmioty` (`nazwa_przedmiotu`),
  CONSTRAINT `fk_przedmioty` FOREIGN KEY (`nazwa_przedmiotu`) REFERENCES `przedmioty` (`nazwa`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oceny`
--

LOCK TABLES `oceny` WRITE;
/*!40000 ALTER TABLE `oceny` DISABLE KEYS */;
INSERT INTO `oceny` VALUES ('biologia',1,4,1,'2018-01-26 23:04:59'),('biologia',1,2,2,'2018-01-26 23:04:59'),('biologia',7,3,3,'2018-01-26 23:04:59'),('biologia',1,4,4,'2018-01-27 01:23:21'),('biologia',8,5,5,'2018-01-27 01:23:46'),('biologia',7,2,6,'2018-01-27 01:26:19'),('biologia',7,1,7,'2018-01-27 13:11:47'),('wychowanie fizyczne',1,2,8,'2018-01-27 15:01:25'),('wychowanie fizyczne',7,2,9,'2018-01-28 15:37:21'),('wychowanie fizyczne',7,4,10,'2018-01-30 00:08:57');
/*!40000 ALTER TABLE `oceny` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `osoby`
--

DROP TABLE IF EXISTS `osoby`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `osoby` (
  `imie` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `nazwisko` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `czy_rodzic` tinyint(1) NOT NULL DEFAULT '0',
  `czy_nauczyciel` tinyint(1) NOT NULL DEFAULT '0',
  `czy_uczen` tinyint(1) NOT NULL DEFAULT '0',
  `id_klasy` mediumint(9) DEFAULT NULL,
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_osoby` (`imie`,`nazwisko`),
  KEY `fk_klasy` (`id_klasy`),
  CONSTRAINT `fk_klasy` FOREIGN KEY (`id_klasy`) REFERENCES `klasy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `osoby`
--

LOCK TABLES `osoby` WRITE;
/*!40000 ALTER TABLE `osoby` DISABLE KEYS */;
INSERT INTO `osoby` VALUES ('jan','kowalski',0,0,1,2,1),('joanna','kownacka',0,1,0,NULL,3),('krzysztof','tabor',0,1,0,NULL,4),('krzesimir','pajo',0,1,0,NULL,5),('wiktor','grzyb',0,1,0,NULL,6),('stefan','stopa',0,0,1,2,7),('szymon','kownacki',0,0,1,2,8);
/*!40000 ALTER TABLE `osoby` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `przedmioty`
--

DROP TABLE IF EXISTS `przedmioty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `przedmioty` (
  `nazwa` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`nazwa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `przedmioty`
--

LOCK TABLES `przedmioty` WRITE;
/*!40000 ALTER TABLE `przedmioty` DISABLE KEYS */;
INSERT INTO `przedmioty` VALUES ('biologia'),('chemia'),('jezyk polski'),('matematyka'),('wychowanie fizyczne');
/*!40000 ALTER TABLE `przedmioty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `przedmioty_w_planach`
--

DROP TABLE IF EXISTS `przedmioty_w_planach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `przedmioty_w_planach` (
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `dzien_tygodnia` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `godzina_lekcyjna` tinyint(1) NOT NULL,
  `nazwa` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id_klasy` mediumint(9) NOT NULL,
  `id_nauczyciela` mediumint(9) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `p_uniq_id` (`dzien_tygodnia`,`godzina_lekcyjna`,`id_klasy`),
  KEY `fk_przedmioty_klasy` (`id_klasy`),
  KEY `osoby_fk` (`id_nauczyciela`),
  KEY `fk_przedmioty_nazwa` (`nazwa`),
  CONSTRAINT `fk_przedmioty_klasy` FOREIGN KEY (`id_klasy`) REFERENCES `klasy` (`id`),
  CONSTRAINT `fk_przedmioty_nazwa` FOREIGN KEY (`nazwa`) REFERENCES `przedmioty` (`nazwa`),
  CONSTRAINT `osoby_fk` FOREIGN KEY (`id_nauczyciela`) REFERENCES `osoby` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `przedmioty_w_planach`
--

LOCK TABLES `przedmioty_w_planach` WRITE;
/*!40000 ALTER TABLE `przedmioty_w_planach` DISABLE KEYS */;
INSERT INTO `przedmioty_w_planach` VALUES (1,'poniedzialek',1,'biologia',2,3),(3,'poniedzialek',2,'biologia',2,3),(5,'poniedzialek',3,'jezyk polski',2,4),(6,'poniedzialek',4,'matematyka',2,6),(7,'wtorek',1,'biologia',2,3),(8,'wtorek',2,'chemia',2,5),(9,'środa',1,'wychowanie fizyczne',2,3);
/*!40000 ALTER TABLE `przedmioty_w_planach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wiadomosci`
--

DROP TABLE IF EXISTS `wiadomosci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wiadomosci` (
  `id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `id_nadawcy` mediumint(9) NOT NULL,
  `id_odbiorcy` mediumint(9) NOT NULL,
  `tresc` varchar(300) COLLATE utf8_unicode_ci NOT NULL,
  `data` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tytul` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_osoby_nadawca` (`id_nadawcy`),
  KEY `fk_osoby_odbiorca` (`id_odbiorcy`),
  CONSTRAINT `fk_osoby_nadawca` FOREIGN KEY (`id_nadawcy`) REFERENCES `osoby` (`id`),
  CONSTRAINT `fk_osoby_odbiorca` FOREIGN KEY (`id_odbiorcy`) REFERENCES `osoby` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wiadomosci`
--

LOCK TABLES `wiadomosci` WRITE;
/*!40000 ALTER TABLE `wiadomosci` DISABLE KEYS */;
INSERT INTO `wiadomosci` VALUES (1,1,8,'Czesc szymon,\n tu pisze jan','2018-01-07 20:18:00','');
/*!40000 ALTER TABLE `wiadomosci` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-30 20:54:57
