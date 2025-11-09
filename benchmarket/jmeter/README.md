
# JMeter Plans

Each `.jmx` plan uses `BASE_URL` (default `http://variant-c:8080`). Override per variant:

- Variant A (Jersey): `-JBASE_URL=http://variant-a:8080`
- Variant C (Spring MVC): `-JBASE_URL=http://variant-c:8080`
- Variant D (Spring Data REST): prefer the proxy `/api/items` added to align with `/items?categoryId=`.
  - Use: `-JBASE_URL=http://variant-d:8080/api`

InfluxDB v2 parameters (override with `-JINFLUX_URL`, `-JINFLUX_BUCKET`, `-JINFLUX_ORG`, `-JINFLUX_TOKEN`).

Run example:
```
jmeter -n -t READ-heavy.jmx -JBASE_URL=http://variant-a:8080 -JINFLUX_TOKEN=***
```
