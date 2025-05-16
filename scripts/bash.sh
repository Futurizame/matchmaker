#!/bin/bash

## this script creates a docker run for redis
docker run \
  --name redis \
  -p 6379:6379 \
  -d \
  redis:latest
