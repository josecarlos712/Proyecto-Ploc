-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: proyecto_ploc
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `drones`
--

DROP TABLE IF EXISTS `drones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drones` (
  `ID_DRON` int NOT NULL,
  `PESO_SOPORTADO` int DEFAULT NULL,
  `BATERIA` int DEFAULT NULL,
  `ESTADO` varchar(45) NOT NULL,
  `PARKING_PATH` varchar(45) NOT NULL,
  `ID_SENSOR` int DEFAULT NULL,
  `ID_RUTA` int DEFAULT NULL,
  PRIMARY KEY (`ID_DRON`),
  KEY `ID_RUTA_idx` (`ID_RUTA`),
  KEY `ID_SENSOR_idx` (`ID_SENSOR`),
  CONSTRAINT `ID_RUTA` FOREIGN KEY (`ID_RUTA`) REFERENCES `rutas` (`ID_RUTA`),
  CONSTRAINT `ID_SENSOR` FOREIGN KEY (`ID_SENSOR`) REFERENCES `sensores` (`ID_SENSOR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drones`
--

LOCK TABLES `drones` WRITE;
/*!40000 ALTER TABLE `drones` DISABLE KEYS */;
INSERT INTO `drones` VALUES (1,50,100,'GARAJE','4F,R,2F',1,1),(2,1,98,'GARAJE','4F,R,2F',1,1);
/*!40000 ALTER TABLE `drones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rutas`
--

DROP TABLE IF EXISTS `rutas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rutas` (
  `ID_RUTA` int NOT NULL AUTO_INCREMENT,
  `TIEMPO_RUTA` bigint DEFAULT NULL,
  `PATH` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID_RUTA`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='En esta tabla se almacenarán todas las rutas disponibles para que puedan seguirlas los drones';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rutas`
--

LOCK TABLES `rutas` WRITE;
/*!40000 ALTER TABLE `rutas` DISABLE KEYS */;
INSERT INTO `rutas` VALUES (1,120,'5F,2R,5F'),(2,140,'5F-R-4F-L-2F');
/*!40000 ALTER TABLE `rutas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensores`
--

DROP TABLE IF EXISTS `sensores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensores` (
  `ID_SENSOR` int NOT NULL AUTO_INCREMENT,
  `TIPO` varchar(20) NOT NULL,
  `TIEMPO_ACT` int NOT NULL,
  PRIMARY KEY (`ID_SENSOR`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='En esta tabla, podremos encontrar todos los sensores de proximidad que poseemos, normalmente habrá uno por cada vehículo, aunque podemos acceder a varios para hacer un reconocimiento de una forma mucho más rápida';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensores`
--

LOCK TABLES `sensores` WRITE;
/*!40000 ALTER TABLE `sensores` DISABLE KEYS */;
INSERT INTO `sensores` VALUES (1,'Prox',5),(2,'Prox',15),(3,'Prox',8),(4,'Proxx',2),(5,'Proximidad',3);
/*!40000 ALTER TABLE `sensores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `valores`
--

DROP TABLE IF EXISTS `valores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `valores` (
  `ID_VALOR` int NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` bigint DEFAULT NULL,
  `OBS` tinyint DEFAULT NULL,
  `ID_SENSOR` int DEFAULT NULL,
  PRIMARY KEY (`ID_VALOR`),
  KEY `ID_SENSOR_idx` (`ID_SENSOR`),
  CONSTRAINT `ID_SENSOR2` FOREIGN KEY (`ID_SENSOR`) REFERENCES `sensores` (`ID_SENSOR`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `valores`
--

LOCK TABLES `valores` WRITE;
/*!40000 ALTER TABLE `valores` DISABLE KEYS */;
INSERT INTO `valores` VALUES (1,0,0,1),(2,1,0,1),(3,1,1,1),(4,1,1,1),(5,1,1,1);
/*!40000 ALTER TABLE `valores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'proyecto_ploc'
--

--
-- Dumping routines for database 'proyecto_ploc'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-06 16:51:38
