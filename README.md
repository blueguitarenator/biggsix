## BiggSix - seeded from (Spray-swagger-slick-seed)


### Debugging
- sbt -jvm-debug 9999 run
- Remote Run Config with port 9999
- UI is at ~/Documents/code/spray-jwt/example. Run with: > node service.js

Key features:

* Basic Http Authentication
* Slick3
* Swagger using webjars
* Db drivers loaded with configuration file H2/Postgres
* Integrations tests
* Typesafe config


Follow these steps to get started:

1. Git-clone this repository.

        $ git clone https://github.com/Gneotux/pfc-spray.git my-project

2. Change directory into your clone:

        $ cd my-project

3 (Optional). Create the database in postgres using the script in {DIRECTORY}/src/main/resources/schema.sql, modify the application.conf file


4. Run the application

        > sbt run

5. Browse to [http://localhost:8080](http://localhost:8080/)


