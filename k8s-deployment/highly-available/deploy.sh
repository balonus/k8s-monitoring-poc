#!/bin/bash

kubectl create -f ./influxdb.yaml
kubectl create -f ./prometheus-telegraf.yaml
kubectl create -f ./grafana.yaml
kubectl create -f ./test-static-app.yaml

kubectl get pods