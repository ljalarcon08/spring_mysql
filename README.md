# spring_mysql

Project made with spring tool suite

Database connection, hibernate index location: application.properties

Generate WAR:

1. Open terminal and access to project folder
2. In terminal execute: gradlew assemble
3. WAR in /build/libs

To access:

1. Open in browser http://localhost:8080/{NameOFWAR}/list

To add movies:

1. In the page, press new, or access to http://localhost:8080/{NameOFWAR}/find
2. Select id or name (can be find in http://www.imdb.com).
3. Press "Buscar" and Press "Agregar".

Note: tomcat and project must use same jre base version (1.8).
