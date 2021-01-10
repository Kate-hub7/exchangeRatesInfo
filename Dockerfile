FROM java:8
ADD build/libs/exchangeRatesInfo-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar exchangeRatesInfo-1.0-SNAPSHOT.jar
