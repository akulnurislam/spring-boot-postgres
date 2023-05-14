# spring-boot-postgres
Demo project for Spring Boot with Postgres

### requirement
- docker version 23
- java version 17
- postgres version 15

### app specification
- JWT with alg `RS256`
  - asymmetric keys for JWT Sign is inside `keys` folder
    - `app-private-key.pem`
    - `app-public-key.pem`
- password hash using `BCryptPasswordEncoder` with salt
  - salt in this project placed in the same data source as the hashed password (only different column), to be more
    secure, the salt should be placed in a different source than the password
- unit test
- swagger (OpenAPI)

### postgres specification
if using the default postgres this project
- connection only for ssl `verify-full`
```
database : dsp
username : dsp-user
password : dsp-password
```
- asymmetric keys is inside `keys` folder
  - server keys (used for running postgres server)
    - `ca.crt` root certificate for generate `server.crt` and client certificates (`dsp-user.crt`)
    - `server.crt`
    - `server.key`
  - client keys (used for client connection using ssl `verify-full`)
    - `ca.crt`
    - `dsp-user.crt`
    - `dsp-user.key`
    - `dsp-user.pk8` version **pkcs8** of `dsp-user.key` used for java postgresql client

### run application
we put configuration for application is inside `config` folder use `application.yaml`

#### start
- `$ docker compose up`
- then open `http://localhost:8080/swagger-ui/index.html`

#### unit test
- `$ ./mvnw test`

#### other start
using maven
- `$ ./mvnw spring-boot:run`

using java jar
- package application into jar, generating jar file into `target` folder `dsp-0.0.1-SNAPSHOT.jar`
  - `$ ./mvnw package -DskipTests`
- run with java jar, make sure the version of app, check `pom.xml`
  - `$ java -jar target/dsp-0.0.1-SNAPSHOT.jar --spring.config.location=file:config/application.yaml`
