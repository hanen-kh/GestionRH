
FROM openjdk:17

WORKDIR /app
COPY target/GestionRH-0.0.1-SNAPSHOT.jar GestionRH.jar
EXPOSE 8000
ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT [ "java", "-jar" , "GestionRH.jar"]
