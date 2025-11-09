# T1 - Scénarios de charge

## Scénario 1: READ-heavy
- **Objectif**: Tester les performances en lecture
- **Distribution des requêtes**:
  - GET /categories?page=0&size=20: 20%
  - GET /categories/{id}: 20%
  - GET /items?page=0&size=20: 20%
  - GET /items/{id}: 20%
  - GET /categories/{id}/items?page=0&size=20: 20%
- **Charge**: 100 utilisateurs simultanés
- **Durée**: 5 minutes

## Scénario 2: JOIN-filter
- **Objectif**: Tester les performances des requêtes avec jointures
- **Distribution des requêtes**:
  - GET /items?categoryId={id}&page=0&size=20: 50%
  - GET /categories/{id}/items?page=0&size=20: 50%
- **Charge**: 50 utilisateurs simultanés
- **Durée**: 5 minutes

## Scénario 3: MIXED
- **Objectif**: Tester un mélange représentatif des opérations CRUD
- **Distribution des requêtes**:
  - GET /items: 40%
  - POST /items: 20%
  - PUT /items/{id}: 20%
  - DELETE /items/{id}: 10%
  - GET /categories: 10%
- **Charge**: 75 utilisateurs simultanés
- **Durée**: 5 minutes

## Scénario 4: HEAVY-body
- **Objectif**: Tester les performances avec payloads volumineux
- **Distribution des requêtes**:
  - POST /items (payload ~5KB): 50%
  - PUT /items/{id} (payload ~5KB): 50%
- **Charge**: 25 utilisateurs simultanés
- **Durée**: 5 minutes

## Paramètres communs
- **Ramp-up time**: 30 secondes
- **Think time**: 100-500ms entre requêtes
- **Assertion**: Response time < 2000ms, HTTP 200/201/204
- **Backend listener**: InfluxDB pour métriques temps réel
