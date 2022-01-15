FROM lwieske/java-8:jdk-8u51
EXPOSE 8080
ADD target/logging-log4j2-vuln.jar logging-log4j2-vuln.jar
ENTRYPOINT ["java", "-jar","-Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector","/logging-log4j2-vuln.jar"]
