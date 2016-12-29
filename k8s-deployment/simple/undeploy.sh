#!/bin/bash

kubectl delete -f ./prometheus.yaml
kubectl delete -f ./influxdb.yaml
kubectl delete -f ./telegraf.yaml
kubectl delete -f ./grafana.yaml
kubectl delete -f ./test-static-app.yaml

kubectl get pods