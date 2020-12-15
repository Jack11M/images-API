# Images API

SpringBoot application with REST

The technologies used:

- Java, Spring (Boot, Web MVC, Data JPA), Lombok
- MySQL 
- Swagger

# Usage

### Requirements

- [JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) `version 8`
- [MySQL](https://www.mysql.com/downloads/) `version 8`
- [maven](https://maven.apache.org/index.html) `version 3.6.+`
- MySQL scheme `image_storage` will be created after every local run

### Clone project from git: `git clone https://github.com/Jack11M/images-API.git`

### Before you run the project, change some properties in application.properties:

```
## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url=jdbc:mysql://localhost:3306/image_storage?createDatabaseIfNotExist=true&autoReconnect=true&serverTimezone=Europe/Kiev&characterEncoding=UTF-8&useSSL=false
spring.datasource.username=your_username
spring.datasource.password=your_password

```

### Configure the cron expression for refreshing the local cache (by default the DB refreshes at 12:00 AM every day)

This value is CRON_TIME in the AppConstants file 


### Installation
Use the following command to run the project if you fulfill the requirements.

```
mvn clean spring-boot:run

```

### API
Use Swagger-UI local URL: `http://localhost:8080/swagger-ui/`
