# k8s-monitoring-poc

Proof of concept example for configuration of monitoring infrastructure on Kubernetes.

Components:
* Prometheus - scraping metrics (both k8s infrastructure and application level)
* Telegraf - pushing metrics data from Prometheus to InfluxDB
* InfluxDB - central metrics data storage
* Grafana - example metrics dashboards

Example setups:
* [k8s-deployment/simple](k8s-deployment/simple) - single instance of each component
* [k8s-deployment/highly-available](k8s-deployment/highly-available) - each component redundant
