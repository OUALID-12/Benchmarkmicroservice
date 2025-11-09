# Benchmark de Performances des Web Services REST

Ce projet évalue les performances de différentes stacks REST sur un même domaine métier et base de données PostgreSQL. Les variantes implémentées sont :

- **A** : JAX-RS (Jersey) + JPA/Hibernate
- **C** : Spring Boot + @RestController (Spring MVC) + JPA/Hibernate
- **D** : Spring Boot + Spring Data REST (repositories exposés)

## Modèle de Données

- **Category** : id, code, name, updated_at
- **Item** : id, sku, name, price, stock, category_id, updated_at
- Relations : Category (1) — Item (N)

## Endpoints Communs

### Category
- GET /categories?page=&size=
- GET /categories/{id}
- POST /categories
- PUT /categories/{id}
- DELETE /categories/{id}

### Item
- GET /items?page=&size=
- GET /items/{id}
- GET /items?categoryId=&page=&size=
- POST /items
- PUT /items/{id}
- DELETE /items/{id}

### Relations
- GET /categories/{id}/items?page=&size=

## Jeu de Données
- 2000 catégories
- 100 000 items (~50 par catégorie)

## Environnement
- Java 17
- PostgreSQL 14+
- HikariCP (maxPoolSize=20, minIdle=10)
- Prometheus + JMX Exporter
- Grafana
- JMeter avec InfluxDB v2

## Scénarios de Charge (JMeter)
1. READ-heavy
2. JOIN-filter
3. MIXED
4. HEAVY-body

## Structure du Projet
- `db/` : Scripts SQL pour schéma et données
- `jmeter/` : Plans de test JMeter et données CSV
- `variants/` : Code source des variantes A, C, D
- `monitoring/` : Configurations Prometheus/Grafana
- `docker-compose.yml` : Services pour l'environnement de test

## Installation et Exécution
1. Démarrer les services : `docker-compose up -d`
2. Initialiser la DB : Les scripts dans `db/` sont exécutés automatiquement
3. Lancer une variante : `cd variants/A-jaxrs-jersey && mvn spring-boot:run` (adapter pour chaque variante)
4. Exécuter les tests JMeter : `jmeter -n -t jmeter/read-heavy.jmx`

## Livrables
- Code des variantes
- Fichiers JMeter
- Dashboards Grafana
- Tableaux de résultats (T0-T7)
- Analyse et recommandations
