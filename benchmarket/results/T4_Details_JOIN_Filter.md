# T4 - Détails par endpoint (scénario JOIN-filter)

## GET /items?categoryId=

| Variante | RPS | p95 (ms) | Err % | Observations (JOIN, N+1, projection) |
|----------|-----|----------|-------|-------------------------------------|
| A : Jersey |     |          |       |                                     |
| C : @RestController |     |          |       |                                     |
| D : Spring Data REST |     |          |       |                                     |

## GET /categories/{id}/items

| Variante | RPS | p95 (ms) | Err % | Observations (JOIN, N+1, projection) |
|----------|-----|----------|-------|-------------------------------------|
| A : Jersey |     |          |       |                                     |
| C : @RestController |     |          |       |                                     |
| D : Spring Data REST |     |          |       |                                     |

## Analyse des requêtes JOIN
- [À remplir après exécution des tests]
- Détection des problèmes N+1
- Efficacité des jointures
- Impact des projections
- Comparaison des stratégies de récupération
