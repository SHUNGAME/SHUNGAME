CREATE DATABASE  IF NOT EXISTS `shun` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `shun`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: shun
-- ------------------------------------------------------
-- Server version	5.6.17-log

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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `USER_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_INFO` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(250) DEFAULT NULL,
  `CODED_PASSWORD` varchar(500) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_point_info`
--

DROP TABLE IF EXISTS `USER_POINT_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_POINT_INFO` (
  `USER_ID` int(11) NOT NULL,
  `MACHINE_ID` varchar(200) DEFAULT NULL,
  `DATE_YEAR` varchar(20) DEFAULT NULL,
  `DATE_MONTH` varchar(20) DEFAULT NULL,
  `DATE_DAY` varchar(20) DEFAULT NULL,
  `POINT_DATE` timestamp NULL DEFAULT NULL,
  `POINT_VALUE` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FK_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户积分';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_point_info`
--

LOCK TABLES `user_point_info` WRITE;
/*!40000 ALTER TABLE `user_point_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_point_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_reward`
--

DROP TABLE IF EXISTS `USER_REWARD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_REWARD` (
  `USER_ID` int(11) NOT NULL,
  `MACHINE_ID` varchar(200) DEFAULT NULL,
  `SHOOPING_CODE` varchar(200) DEFAULT NULL,
  `MOVIE_CODE` varchar(200) DEFAULT NULL,
  `OTHER_DATA` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  CONSTRAINT `FK_USER_REWARD_ID` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户奖励';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_reward`
--

LOCK TABLES `user_reward` WRITE;
/*!40000 ALTER TABLE `user_reward` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_reward` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-05-11 14:43:19
