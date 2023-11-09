# Serveur Java multi client


Nous allons tenter de comprendre les mécanismes de base de la communication par sockets via la construction d'un serveur Java multi client.
Ce serveur s'attachera à simplement communiquer avec ces clients via une connexion TCP.
En effet les sockets sont une couche suffisamment bas niveau pour être inclus dans la plupart des systèmes et pour être supportés
par une majorité des langages de programmation. Voici les fonctionnalités du serveur Java que nous allons programmer, 

ServeurMT : serveur en Java, donc multiplateforme, multithread avec la possibilité d'accepter plusieurs clients simultanément, 
le serveur est capable d’accepter trois requête : Consulter, créditer et débiter le compte selon le choix du client.

On s’intéressera aussi à programmer notre client. 

Le langage choisi ici est le Java, simplement parce qu'on pourra lancer le serveur aussi facilement sous Unix, que sous Windows ou que sous Mac (et aussi parce que les sockets sont assez simples à utiliser en Java).
