# Use a base image with OpenJDK
FROM openjdk:17-jdk-alpine

# Add a volume pointing to /tmp
VOLUME /tmp

# Copy the application JAR file
COPY target/dropbox-service.jar app.jar

# Run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]