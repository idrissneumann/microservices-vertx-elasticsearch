# Exemple elasticsearch

## Table des matières

[[_TOC_]]

## Démarrer l'application

Cette commande permet de lancer ou relancer tous l'environnement (et le recréer complètement depuis 0).

```shell
docker-compose up -d --build --force-recreate
```

## Initialiser la base de données PostgreSQL

```shell
cd elasticsearch-example
docker exec -i bblvertx_postgres psql -U bblvertx bblvertx < src/main/resources/db/postgresql/0_schema_pgsql.sql
docker exec -i bblvertx_postgres psql -U bblvertx bblvertx < src/main/resources/db/postgresql/1_data_pgsql.sql 
```

## Initialiser la base de données Cassandra

```shell
docker exec -i bblvertx_cassandra cqlsh < src/main/resources/db/cassandra/0_bblvertx_create_keyspace.cql 
```
