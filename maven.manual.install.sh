#!/bin/bash
mvn install:install-file -Dfile=./resources/oracle_driver/ojdbc7.jar  -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2.0 -Dpackaging=jar
mvn install:install-file -Dfile=./resources/hutch_ws/hutchws.jar  -DgroupId=com.comverse_in -DartifactId=hutch-ws -Dversion=1.0 -Dpackaging=jar