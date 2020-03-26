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
  `PESO_SOPORTADO` int NOT NULL,
  `BATERÍA` int NOT NULL,
  `ESTADO` varchar(15) NOT NULL,
  `PARKING_PATH` varchar(45) NOT NULL,
  `ID_SENSOR` int DEFAULT NULL,
  `ID_RUTA` int DEFAULT NULL,
  PRIMARY KEY (`ID_DRON`),
  KEY `ID_SENSOR_idx` (`ID_SENSOR`),
  KEY `ID_RUTA_idx` (`ID_RUTA`),
  CONSTRAINT `ID_RUTA` FOREIGN KEY (`ID_RUTA`) REFERENCES `rutas` (`ID_RUTA`),
  CONSTRAINT `ID_SENSOR` FOREIGN KEY (`ID_SENSOR`) REFERENCES `sensores_proximidad` (`ID_SENSOR_PROX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Aquí podremos encontrar todos los drones que se encuentrran a nuestra merced y controlar el estado que tienen';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drones`
--

LOCK TABLES `drones` WRITE;
/*!40000 ALTER TABLE `drones` DISABLE KEYS */;
INSERT INTO `drones` VALUES (1,5,100,'GARAJE','2F,2L,4F',1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='En esta tabla se almacenarán todas las rutas disponibles para que puedan seguirlas los drones';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rutas`
--

LOCK TABLES `rutas` WRITE;
/*!40000 ALTER TABLE `rutas` DISABLE KEYS */;
INSERT INTO `rutas` VALUES (1,120,'5F,2R,5F');
/*!40000 ALTER TABLE `rutas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensores_proximidad`
--

DROP TABLE IF EXISTS `sensores_proximidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensores_proximidad` (
  `ID_SENSOR_PROX` int NOT NULL AUTO_INCREMENT,
  `TIMESTAMP` bigint NOT NULL,
  `OBSTACULO` tinyint NOT NULL,
  PRIMARY KEY (`ID_SENSOR_PROX`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='En esta tabla, podremos encontrar todos los sensores de proximidad que poseemos, normalmente habrá uno por cada vehículo, aunque podemos acceder a varios para hacer un reconocimiento de una forma mucho más rápida';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensores_proximidad`
--

LOCK TABLES `sensores_proximidad` WRITE;
/*!40000 ALTER TABLE `sensores_proximidad` DISABLE KEYS */;
INSERT INTO `sensores_proximidad` VALUES (1,0,0),(2,1,0),(3,2,0),(4,3,1);
/*!40000 ALTER TABLE `sensores_proximidad` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-20 16:47:20
