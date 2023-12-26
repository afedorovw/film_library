FROM openjdk:17
ARG jarFile=target/filmLibrary-0.1.war
WORKDIR /opt/app
COPY ${jarFile} library.war
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "library.war"]
