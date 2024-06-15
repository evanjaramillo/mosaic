FROM tomcat:jdk17

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ENV SPRING_PROFILES_ACTIVE tomcat

COPY . /mosaic

WORKDIR /mosaic

RUN apt-get update \
    && apt-get install maven -y \
    && mvn clean package -Ptomcat \
    && cp /mosaic/target/mosaic-*.war $CATALINA_HOME/webapps/ROOT.war

CMD ["catalina.sh", "run"]