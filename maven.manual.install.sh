#!/bin/bash
mvn install:install-file -Dfile=./resources/oracle_driver/ojdbc7.jar  -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2.0 -Dpackaging=jar