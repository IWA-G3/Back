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
