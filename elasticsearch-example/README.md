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
docker exec -i bblvertx_cassandra cqlsh -k bblvertx < src/main/resources/db/cassandra/1_bblvertx_create_tables.cql
docker exec -i bblvertx_cassandra cqlsh -k bblvertx < src/main/resources/db/cassandra/2_bblvertx_insert_dataset.cql
```

## Indexation des données de PostgreSQL

### Lancer les routes d'indexation

Observer le fichier [`routes.properties`](./src/main/resources/routes.properties)

Lancez toutes la routes `JdbcIndexingUserRoute`:

```shell
$ docker exec -i bblvertx_postgres psql -U bblvertx bblvertx -c "SELECT index_all()" # ré-initialiser l'index
$ curl localhost:8071/bblvertx/api/userService/jdbc/index
<html><body><div>Indexation en cours...</div></body></html>
```

### Vérifier le résultat de l'indexation sur Elasticsearch

```shell
$ curl localhost:9200/_cat/indices
green  open .geoip_databases 5wjRQfVASHCmD5q0yhtaXQ 1 0 43 0 40.1mb 40.1mb
yellow open user             Gz3G15HdSne3ukR-aBjdLg 1 1  8 0   11kb   11kb
```

```shell
$ curl localhost:9200/user/_search?size=100|jq .
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100  2442  100  2442    0     0   170k      0 --:--:-- --:--:-- --:--:--  170k
{
  "took": 5,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 8,
      "relation": "eq"
    },
    "max_score": 1,
    "hits": [
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "1",
        "_score": 1,
        "_source": {
          "name": "Dujardin",
          "firstname": "Jean",
          "email": "jean.dujardin@domain.com",
          "id": "1",
          "skill": "Cinema",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "c9a7493c-5ff9-496b-af93-2eadd66379a9"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "2",
        "_score": 1,
        "_source": {
          "name": "Dupont",
          "firstname": "Michel",
          "email": "michel.dupont@domain.com",
          "id": "2",
          "skill": null,
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "0cdc6be4-4f57-401e-a144-1cd2003cf7ca"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "3",
        "_score": 1,
        "_source": {
          "name": "Durand",
          "firstname": "Bertrand",
          "email": "bertrand.durand@domain.com",
          "id": "3",
          "skill": "Computing",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "0a30923e-9b4d-4eff-ad8e-156a3fd67210"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "4",
        "_score": 1,
        "_source": {
          "name": "Ahmed",
          "firstname": "Ben Mansour",
          "email": "ahmed.benmansour@domain.com",
          "id": "4",
          "skill": "Computing",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "7b9c8df6-b523-449b-830b-9504e31d77f5"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "5",
        "_score": 1,
        "_source": {
          "name": "Liu",
          "firstname": "Chia-lang",
          "email": "liu@domain.com",
          "id": "5",
          "skill": "Martial art",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "4a878ac9-b32a-46b4-96b7-92f9e796a8e6"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "6",
        "_score": 1,
        "_source": {
          "name": "Morgan",
          "firstname": "Freeman",
          "email": "morgan.freeman@domain.com",
          "id": "6",
          "skill": "Cinema",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "b1bf729c-e0bc-4ecb-816b-2fe332f061d5"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "7",
        "_score": 1,
        "_source": {
          "name": "Bruce",
          "firstname": "Lee",
          "email": "bruce.lee@domain.com",
          "id": "7",
          "skill": "Martial art",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "52ece40c-952d-48c9-b680-6c444648c25b"
        }
      },
      {
        "_index": "user",
        "_type": "_doc",
        "_id": "8",
        "_score": 1,
        "_source": {
          "name": "Samuel Leeroy",
          "firstname": "Jackon",
          "email": "sam.jackson@domain.com",
          "id": "8",
          "skill": "Cinema",
          "dateConnect": 1641513600000,
          "dateUpdate": 1641513600000,
          "rsSearch": 1,
          "randomId": "bc4b8b48-7cc8-44cd-b4af-2c806b6b86a9"
        }
      }
    ]
  }
}
```

## Indexation des données de Cassandra

Faites les mêmes opérations et vérifications mais avec la route `CassandraIndexingUserRoute`.

Vous devriez retrouver plus d'utilisateurs dans l'index.
