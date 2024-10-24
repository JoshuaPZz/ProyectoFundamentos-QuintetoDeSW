FROM tomcat:10.0-jdk17
VOLUME [ "/usr/local/tomcat/webapp" ]
COPY start.sh /
RUN rm -f /usr/local/tomcat/conf/tomcat-users.xml
COPY tomcat-users.xml /usr/local/tomcat/conf/
RUN cp -avT $CATALINA_HOME/webapps.dist/manager $CATALINA_HOME/webapps/manager
COPY context.xml /tmp/
#RUN cp -f /tmp/context.xml $CATALINA_HOME/webapps/manager/META-INF/context.xml

#RUN chown -R 19830 /usr/local/tomcat/
#USER 19830
CMD ["/start.sh", "run"]
