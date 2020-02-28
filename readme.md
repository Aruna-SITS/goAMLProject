# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact

#### Maven Build ###
mvn clean install -DskipTests -Dbasedir=~/projects/iaml


#### please use follow as your VM parameter
apps.config.root=/usr/apps/iaml

### Executing the jar in command prompt 
java -jar iaml.jar --apps.config.root=absolute_path_of_your_config_folder
#Example : java -jar iaml.jar --apps.config.root=/home/dayalan/Desktop/JAR/config

### To preserve log entries 
java -jar iaml.jar --apps.config.root=absolute_path_of_your_config_folder >fileName.txt
