FROM openjdk:18-jdk-alpine
COPY target/note-service-*.jar /note-service.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /note-service.jar $SPRING_PROPERTY_OVERRIDE
