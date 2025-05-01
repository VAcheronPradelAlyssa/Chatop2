# ChaTop

ChâTop est une application de location immobilière qui facilite la connexion des locataires potentiels avec les propriétaires en fournissant l'authentification, la gestion de location et la messagerie entre les utilisateurs.

## Caractéristiques

- **Authentification de l’utilisateur (JWT)**
- **Opérations CRUD pour les locations et les messages**
- **Enregistrement des Images dans le serveur**
- **Documentation API avec Swagger**

## Prérequis

Assurez-vous que les éléments suivants sont installés sur votre machine :

- **Java 17 ou supérieur**
- **Maven** (pour construire le backend)
- **MySQL** (pour la configuration de la base de données)
- **Node.js et npm** (pour la configuration du frontend si nécessaire)
- **Angular CLI** (pour exécuter le frontend Angular)

## Mise en Place de la Base de Données

Pour configurer la base de données, exécutez les scripts SQL suivants :

```sql
-- Créez la base de données
CREATE DATABASE chatop;

-- Utilisez la base de données
USE chatop;

-- Créez les tables
CREATE TABLE `USERS` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `email` VARCHAR(255),
  `name` VARCHAR(255),
  `password` VARCHAR(255),
  `created_at` TIMESTAMP,
  `updated_at` TIMESTAMP
);

CREATE TABLE `RENTALS` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(255),
  `surface` DECIMAL(10, 2),
  `price` DECIMAL(10, 2),
  `picture` VARCHAR(255),
  `description` VARCHAR(2000),
  `owner_id` INT NOT NULL,
  `created_at` TIMESTAMP,
  `updated_at` TIMESTAMP,
  FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`)
);

CREATE TABLE `MESSAGES` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `rental_id` INT,
  `user_id` INT,
  `message` VARCHAR(2000),
  `created_at` TIMESTAMP,
  `updated_at` TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`),
  FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`)
);

-- Créez un index unique sur l'email des utilisateurs
CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

## Configuration de la base de données
spring.datasource.url=jdbc\:mysql://localhost:3306/chatop
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

## Répertoire pour les téléchargements de fichiers
file.upload-dir=uploads/

## Clé secrète pour JWT (à garder secrète)
jwt.secret=VotreCléSecrèteTrèsComplexe


# Accédez au répertoire du frontend (le cas échéant)
cd path/to/frontend

# Installer les dépendances
npm install

# Démarrer le frontend
npm run start

# Construire le backend
mvn clean install

# Exécuter le backend
mvn spring-boot\:run