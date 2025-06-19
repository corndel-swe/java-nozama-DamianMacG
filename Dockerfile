# Use a base image with Java installed
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the .env file into the container
COPY .env .env

COPY nozama.db /app/nozama.db

# Copy the nozama jar file into the container
COPY target/nozama-*.jar app.jar

# Expose the port the application will run on (default is 8080, based on the README)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]