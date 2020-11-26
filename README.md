# Back

## Installation

### Required components :
- Curl
- Unzip 
- Cygwin / Git bash (Windows OS)
- Docker / Docker compose

### Running the script

To set up the dev environment in order to properly run the app, run ```./launchAppDev``` in a bash shell.

To check if the installation succeeded, run ```docker ps```, you should see 6 running containers named
- postgres-covid
- postgres-keycloak
- keycloak
- kafka
- zookeeper
- connect

## Setup Keycloak : 

Créer un realm pour l'application, 

Creer un client qui sert a l'application a s'identifier (Acces Type : confidential, service account enabled), 

Recuperer le secret client de ce client dans l'onglet credentials,

Creer un role admin (avec composite Roles ON, 
Client Roles -> realm-management -> ajouter view-user, query-user, manage-user )

Creer un user administrateur, lui ajouter le role admin et un mot de passe.

Realm-setting => Login => enable User Registration 

## Required Environement for Spring Boot:

KCK_SECRET= Secret of the keycloak client
KCK_CLIENT_ID= keycloak client Id

KCK_URL= Url of KeyCloak
KCK_REALM= Nom du realm keacloak

KCK_ADMIN_PWD = Password of admin user
KCK_ADMIN_USER = Password of admin user

DB_USERNAME = username of the Postrgres

EMAIL_ADDRESS = Email to send the mail with

EMAIL_PASSWORD = Email to send the mail with password

DB_PASSWORD = explicit

## Execution

Launch the Spring app

If spring can't resolve hostname kafka:9092, add the line
```127.0.0.1 kafka``` in your os hosts file.
(Windows : ```C:\Windows\System32\drivers\etc```)
