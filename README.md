# Back

## Installation

### Required components :
- Curl
- Unzip 
- Cygwin / Git bash (Windows OS)
- Docker / Docker compose

### Running the script

To set up the dev environment in order to properly run the app, run ```./launchAppDev``` in a bash shell.

If spring can't resolve hostname kafka:9092, add the line
```127.0.0.1 kafka``` in your os hosts file.

To check if the installation succeeded, run ```docker ps```, you should see 4 running containers named
- postgres-covid
- kafka
- zookeeper
- connect

## Required Environement:

KCK_SECRET= Secret of the keycloak client
KCK_URL= Url of KeyCloak
KCK_ADMIN_PWD = Password of admin user

## Setup Keycloak : 
Créer un realm pour l'application, 

Creer un client qui sert a l'application a s'identifier (Acces Type : confidential, service account enabled), 

Recuperer le secret client de ce client,

Creer un role admin (avec composite Roles ON, 
Client Roles -> realm-management -> ajouter view-user, query-user, manage-user )

Creer un user administrateur, lui ajouter le role admin et un mot de passe. 
