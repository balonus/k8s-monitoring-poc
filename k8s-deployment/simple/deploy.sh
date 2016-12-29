#!/bin/bash

kubectl create -f ./prometheus.yaml
kubectl create -f ./influxdb.yaml
kubectl create -f ./telegraf.yaml
kubectl create -f ./grafana.yaml
kubectl create -f ./test-static-app.yaml

kubectl get pods