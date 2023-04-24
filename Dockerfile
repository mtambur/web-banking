FROM sbo-saas-docker-release-local.artifactory-lvn.broadcom.net/broadcom-images-legacy/alpine/openjdk/tomcat:tomcat8-openjdk8-alpine3

USER root

RUN apk --no-cache add shadow

ENV USER_NAME cdd
ENV GROUP_NAME cdd
ENV USER_ID 1010
ENV GROUP_ID 1010
ENV USER_HOME /home/$USER_NAME
ENV CDD_HOME_FOLDER $USER_HOME/.cdd

RUN groupmod -n $GROUP_NAME `getent group $GROUP_ID | cut -d: -f1`
RUN usermod -l $USER_NAME -d $USER_HOME -m `getent passwd $USER_ID | cut -d: -f1`


ENV TOMCAT_HOME /usr/local/apache-tomcat
ENV REPOSITORY_URL https://cdd.testing:@artifactory-lvn.broadcom.net:443/artifactory
ENV CT_AGENT_URL esd-cdd-maven-integration-local/ct_agent/master/ct_agent/2.1-SNAPSHOT/ct_agent-2.1-SNAPSHOT.jar
ENV JAVA_OPTS=' -javaagent:$CDD_HOME_FOLDER/ct_agent.jar'

RUN mkdir -p $USER_HOME
RUN chown cdd:cdd $USER_HOME -R

USER cdd
COPY build/libs/dummy.war $TOMCAT_HOME/webapps/dummy.war

ARG WSE_URL=$REPOSITORY_URL/$CT_AGENT_URL
ADD $WSE_URL $CDD_HOME_FOLDER/ct_agent.jar

USER root
RUN chown cdd:cdd $TOMCAT_HOME -R
RUN chmod -R 777 $CDD_HOME_FOLDER

USER cdd

EXPOSE 8080
VOLUME $CDD_HOME_FOLDER
CMD ["catalina.sh", "run"]
