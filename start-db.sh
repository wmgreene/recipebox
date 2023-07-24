#!/bin/bash
sudo docker run --name spicebox_db \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=spice_box \
  -e MYSQL_USER=user \
  -p 3307:3307 \
  --network=host \
  -d \
    mysql
    #-v ./sql/:/docker-entrypoint-initdb.d