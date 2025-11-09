# T0 - Configuration

## Environnement de test
- **OS**: Windows 11
- **Java**: OpenJDK 17
- **PostgreSQL**: 14+
- **Docker**: Version récente
- **JMeter**: Version 5.5+
- **Prometheus**: Version 2.40+
- **Grafana**: Version 9.0+

## Configuration des variantes

### Variante A: JAX-RS (Jersey) + JPA/Hibernate
- **Port**: 8080
- **Framework**: Jersey 3.x
- **ORM**: Hibernate 6.1
- **Pool de connexions**: HikariCP (maxPoolSize=20, minIdle=10)

### Variante C: Spring Boot + @RestController
- **Port**: 8081
- **Framework**: Spring Boot 3.1
- **ORM**: Hibernate 6.1
- **Pool de connexions**: HikariCP (maxPoolSize=20, minIdle=10)

### Variante D: Spring Boot + Spring Data REST
- **Port**: 8082
- **Framework**: Spring Boot 3.1 + Spring Data REST
- **ORM**: Hibernate 6.1
- **Pool de connexions**: HikariCP (maxPoolSize=20, minIdle=10)

## Configuration JMeter
- **Threads**: 10-100 (selon scénario)
- **Ramp-up**: 30 secondes
- **Duration**: 5 minutes par test
- **Backend Listener**: InfluxDB v2

## Métriques collectées
- **JMeter**: Latence (p50/p95/p99), débit, taux d'erreur
- **JVM**: CPU, RAM, GC, threads actifs
- **Base de données**: Connexions actives, temps de requête
