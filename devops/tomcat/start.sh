#!/bin/bash
cp -avT $CATALINA_HOME/webapps.dist/manager $CATALINA_HOME/webapps/manager
cp -f /tmp/context.xml $CATALINA_HOME/webapps/manager/META-INF/context.xml
catalina.sh run
