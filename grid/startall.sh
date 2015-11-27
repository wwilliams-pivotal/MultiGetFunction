#!/bin/bash

M2_REPO="/Users/wwilliams"
DEPENDENCY_JARS="${M2_REPO}/.m2/repository/org/slf4j/slf4j-api/./1.7.12/slf4j-api-1.7.12.jar"

# Issue commands to gfsh to start locator and launch a server
echo "Starting locator and server..."
gfsh <<!
connect

start locator --name=locator1 --port=10334 --properties-file=config/locator.properties --load-cluster-configuration-from-dir=true --initial-heap=256m --max-heap=256m

start server --name=server1 --server-port=0 --initial-heap=1g --max-heap=1g --properties-file=config/gemfire.properties --classpath=${DEPENDENCY_JARS}
start server --name=server2 --server-port=0 --initial-heap=1g --max-heap=1g --properties-file=config/gemfire.properties --classpath=${DEPENDENCY_JARS}
start server --name=server3 --server-port=0 --initial-heap=1g --max-heap=1g --properties-file=config/gemfire.properties --classpath=${DEPENDENCY_JARS}

# change the name/ location of this jar file depending on where you launch this script
undeploy --jar=.domain-0.0.2-SNAPSHOT.jar
deploy --jar=../target/multiget-0.0.1-SNAPSHOT.jar


# initialize the brands region
#put --region=Brands --key='JVH' --value=('brandId':'JVH','brandName':'James Village Homes') --value-class=com.wyndham.domain.Brand
#put --region=Brands --key='NWF' --value=('brandId':'NWF','brandName':'Northwest Florida') --value-class=com.wyndham.domain.Brand

list members;
list regions;
exit;
!
