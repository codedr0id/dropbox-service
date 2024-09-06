# Dropbox-Like Service

This project provides a simplified Dropbox-like service implemented in Spring Boot. It includes features for file upload, retrieval, update, deletion, and listing through RESTful APIs.

## Features

- **Upload File API**: Upload files to the platform.
- **Read File API**: Retrieve a file based on a unique identifier.
- **Update File API**: Update an existing file or its metadata.
- **Delete File API**: Delete a file based on a unique identifier.
- **List Files API**: List all available files and their metadata.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker (for Docker setup)

### Running the Service with H2 Database

1. **Clone the Repository:**

   ```sh
   git clone <repository-url>
   cd <repository-directory>

   ```

2. **Build and Run the Project:**

   ```sh
   mvn clean install
   mvn spring-boot:run

   ```

3. **Access the Application and H2 console:**

   ```sh
   The application will be running on http://localhost:8080
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:testdb
   Username: sa
   Password: password
   ```

### Running the Service with Docker

1. **Uncomment the MySQL Database Configuration Properties and Comment the H2 ones:**

   ```sh
   spring.datasource.url=jdbc:mysql://localhost:3306/filedb
   spring.datasource.username=user
   spring.datasource.password=password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

   ```

2. **Package your application into a JAR:**

   ```sh
   mvn clean package

   ```

3. **Build the Docker image:**

   ```sh
   docker build -t dropbox-service .

   ```

4. **Run the Application with Docker Compose:**

   ```sh
   docker-compose up --build

   ```
