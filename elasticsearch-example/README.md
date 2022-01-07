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

## Lancer les routes d'indexation

Observer le fichier [`routes.properties`](./src/main/resources/routes.properties)

Lancez toutes les routes `JdbcIndexingUserRoute` et `CassandraIndexingUserRoute`

```shell
$ docker exec -i bblvertx_postgres psql -U bblvertx bblvertx -c "SELECT index_all()" # ré-initialiser l'index
$ curl localhost:8071/bblvertx/api/userService/jdbc/index
<html><body><div>Indexation en cours...</div></body></html>
$ curl localhost:8071/bblvertx/api/userService/cassandra/index
<html><body><div>Indexation en cours...</div></body></html>
```
