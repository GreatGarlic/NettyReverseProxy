#!/bin/sh -l

projectName="netty_reverse_Proxy"



echo "反向代理打包"

cd ${WORKSPACE}/

mvn clean

mvn package

echo "反向代理部署"

cd ${WORKSPACE}/docker

docker-compose -p ${projectName} down  --rmi all

docker-compose -p ${projectName} up  -d







