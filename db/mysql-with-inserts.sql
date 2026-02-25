DROP DATABASE IF EXISTS `daaexample`;
CREATE DATABASE `daaexample`;

CREATE TABLE `daaexample`.`people` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `surname` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `daaexample`.`users` (
    `login` varchar(100) NOT NULL,
    `password` varchar(64) NOT NULL,
    `role` varchar(10) NOT NULL,
    PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `daaexample`.`pet` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `breed` varchar(100) NOT NULL,
    `birth_year` int NOT NULL,
    `person_id` int NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`person_id`) REFERENCES `people`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE USER IF NOT EXISTS 'daa'@'localhost' IDENTIFIED WITH mysql_native_password BY 'daa';
GRANT ALL ON `daaexample`.* TO 'daa'@'localhost';

INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Antón','Pérez');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Manuel','Martínez');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Laura','Reboredo');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Perico','Palotes');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Ana','María');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'María','Nuevo');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Alba','Fernández');
INSERT INTO `daaexample`.`people` (`id`,`name`,`surname`) VALUES (0,'Asunción','Jiménez');

-- The password for each user is its login suffixed with "pass". For example, user "admin" has the password "adminpass".
INSERT INTO `daaexample`.`users` (`login`,`password`,`role`)
VALUES ('admin', '713bfda78870bf9d1b261f565286f85e97ee614efe5f0faf7c34e7ca4f65baca','ADMIN');
INSERT INTO `daaexample`.`users` (`login`,`password`,`role`)
VALUES ('normal', '7bf24d6ca2242430343ab7e3efb89559a47784eea1123be989c1b2fb2ef66e83','USER');

-- Pets for Antón (id=1): 2 pets
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Rex','Labrador',2019,1);
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Bella','Golden Retriever',2021,1);

-- Pets for Manuel (id=2): 1 pet
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Whiskers','Persa',2020,2);

-- Pets for Laura (id=3): 3 pets
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Max','Pastor Alemán',2018,3);
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Luna','Gato Siamés',2019,3);
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Tweety','Canario',2022,3);

-- Pets for Perico (id=4): 0 pets

-- Pets for Ana (id=5): 2 pets
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Buddy','Bulldog',2020,5);
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Miau','Gato Común',2021,5);

-- Pets for María (id=6): 1 pet
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Fluffy','Maine Coon',2019,6);

-- Pets for Alba (id=7): 0 pets

-- Pets for Asunción (id=8): 2 pets
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Sandy','Dálmata',2017,8);
INSERT INTO `daaexample`.`pet` (`id`,`name`,`breed`,`birth_year`,`person_id`) VALUES (0,'Nemo','Pez Betta',2022,8);