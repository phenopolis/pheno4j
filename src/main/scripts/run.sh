#!/bin/bash

JAVA_OPTS=${JAVA_OPTS}' -classpath .:../lib/*:../conf/'

java ${JAVA_OPTS} com.graph.db.Dispatcher "$@"

exit $?
