-- ----------------------------------------------------------------------------
-- MySQL Workbench Migration
-- Migrated Schemata: rehab_test
-- Source Schemata: rehab
-- Created: Fri Apr 30 08:31:11 2021
-- Workbench Version: 8.0.24
-- ----------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------------
-- Schema rehab_test
-- ----------------------------------------------------------------------------
DROP SCHEMA IF EXISTS `rehab_test` ;
CREATE SCHEMA IF NOT EXISTS `rehab_test` ;

-- ----------------------------------------------------------------------------
-- Table rehab_test.degens
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`degens` (
  `DEGENID` INT NOT NULL AUTO_INCREMENT,
  `DISCORDID` BIGINT NOT NULL,
  `WINS` INT NULL DEFAULT '0',
  `LOSSES` INT NULL DEFAULT '0',
  `NAME` VARCHAR(20) NULL DEFAULT '',
  `IRONMAN` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`DEGENID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.farms
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`farms` (
  `DEGENID` INT NULL DEFAULT NULL,
  `FARMATTEMPTS` INT NOT NULL DEFAULT '3',
  `TIMESFARMED` INT NULL DEFAULT '0',
  INDEX `FARMATT_DEGEN_FK` (`DEGENID` ASC) VISIBLE,
  CONSTRAINT `FARMATT_DEGEN_FK`
    FOREIGN KEY (`DEGENID`)
    REFERENCES `rehab_test`.`degens` (`DEGENID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.funds
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`funds` (
  `DEGENID` INT NULL DEFAULT NULL,
  `FUNDS` BIGINT NULL DEFAULT '10000000',
  `PEAK` BIGINT NULL DEFAULT '0',
  INDEX `FUNDS_DEGEN_FK` (`DEGENID` ASC) VISIBLE,
  CONSTRAINT `FUNDS_DEGEN_FK`
    FOREIGN KEY (`DEGENID`)
    REFERENCES `rehab_test`.`degens` (`DEGENID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.highlowstate
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`highlowstate` (
  `DEGENID` INT NULL DEFAULT NULL,
  `FARMS` INT NOT NULL,
  INDEX `DEGENID` (`DEGENID` ASC) VISIBLE,
  CONSTRAINT `highlowstate_ibfk_1`
    FOREIGN KEY (`DEGENID`)
    REFERENCES `rehab_test`.`degens` (`DEGENID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.items
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`items` (
  `ITEMID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(100) NOT NULL,
  `IMAGENAME` VARCHAR(100) NOT NULL,
  `TYPE` INT NULL DEFAULT NULL,
  PRIMARY KEY (`ITEMID`),
  INDEX `TYPE` (`TYPE` ASC) VISIBLE,
  CONSTRAINT `items_ibfk_1`
    FOREIGN KEY (`TYPE`)
    REFERENCES `rehab_test`.`types` (`TYPEID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.properties
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`properties` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `PROP_VALUE` VARCHAR(10) NULL DEFAULT NULL,
  `PROP_KEY` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.services
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`services` (
  `SERVICEID` INT NOT NULL AUTO_INCREMENT,
  `FARMS` INT NULL DEFAULT '0',
  `LENGTHHOURS` DECIMAL(10,2) NULL DEFAULT '0.00',
  `NAME` VARCHAR(100) NOT NULL,
  `RATEMINUTES` INT NOT NULL,
  `CHANNELID` BIGINT NULL DEFAULT NULL,
  `DEGENID` INT NOT NULL,
  `FORSALE` TINYINT(1) NULL DEFAULT '0',
  `ACTIVE` TINYINT(1) NULL DEFAULT '0',
  `PRICE` BIGINT NULL DEFAULT NULL,
  `PREDETERMINED` TINYINT(1) NULL DEFAULT '0',
  `BIDDABLE` TINYINT(1) NULL DEFAULT '0',
  PRIMARY KEY (`SERVICEID`),
  INDEX `SERVICES_DEGEN_FK` (`DEGENID` ASC) VISIBLE,
  CONSTRAINT `SERVICES_DEGEN_FK`
    FOREIGN KEY (`DEGENID`)
    REFERENCES `rehab_test`.`degens` (`DEGENID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.shop
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`shop` (
  `ITEMID` INT NOT NULL AUTO_INCREMENT,
  `ITEMNAME` VARCHAR(50) NOT NULL,
  `DEGENID` INT NOT NULL,
  `PRICE` BIGINT NOT NULL,
  `FORSALE` TINYINT(1) NOT NULL DEFAULT '1',
  `VALUE` BIGINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`ITEMID`),
  INDEX `SHOP_DEGEN_FK` (`DEGENID` ASC) VISIBLE,
  CONSTRAINT `SHOP_DEGEN_FK`
    FOREIGN KEY (`DEGENID`)
    REFERENCES `rehab_test`.`degens` (`DEGENID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Table rehab_test.types
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `rehab_test`.`types` (
  `TYPEID` INT NOT NULL AUTO_INCREMENT,
  `TYPENAME` VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY (`TYPEID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------------------------
-- Trigger rehab_test.NEW_DEGEN_FUNDS_TRIGGER
-- ----------------------------------------------------------------------------
DELIMITER $$
USE `rehab_test`$$
CREATE TRIGGER `NEW_DEGEN_FUNDS_TRIGGER` AFTER INSERT ON `degens` FOR EACH ROW INSERT INTO FUNDS(DEGENID,FUNDS)VALUES(NEW.DEGENID,(SELECT PROP_VALUE FROM PROPERTIES WHERE PROP_KEY = 'IN_FUNDS'));

-- ----------------------------------------------------------------------------
-- Trigger rehab_test.NEW_DEGEN_FARM_TRIGGER
-- ----------------------------------------------------------------------------
DELIMITER $$
USE `rehab_test`$$
CREATE TRIGGER `NEW_DEGEN_FARM_TRIGGER` AFTER INSERT ON `degens` FOR EACH ROW INSERT INTO FARMS(DEGENID,FARMATTEMPTS,TIMESFARMED)VALUES(NEW.DEGENID,(SELECT PROP_VALUE FROM PROPERTIES WHERE PROP_KEY = 'IN_FRM_ATT'),0);

-- ----------------------------------------------------------------------------
-- Trigger rehab_test.PEAK_TRIGGER
-- ----------------------------------------------------------------------------
DELIMITER $$
USE `rehab_test`$$
CREATE TRIGGER `PEAK_TRIGGER` BEFORE UPDATE ON `funds` FOR EACH ROW BEGIN 
	IF NEW.FUNDS>OLD.PEAK THEN
		SET NEW.PEAK = NEW.FUNDS;
	END IF;
END;
SET FOREIGN_KEY_CHECKS = 1;
