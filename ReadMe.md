# LOG ACCESS PARSER
This is a tool for parsing access log into MySQL database. This project has been written with Java 8 using Eclipse Neon.3

## 1. Folder docs
This folder contains SQL scripts for creating database and testing the program. Please execute `create-db.sql` to create database first.

## 2. Configure database connection
The file `database.properties` contains parameter to make connection to MySql Server, please change these parameters according to your environment. Note that file `database.properties` must be in the same folder as parser.jar
    
    database.host=localhost
    database.user=root
    database.password=
    database.port=3306
    database.name=requests

## 3. Run the parser
Change directory to folder `bin`, this folder contains executable file parser.jar. Please execute following command:
    
    cd bin
    java -jar parser.jar --accessFile=<log_file> --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
