FROM frolvlad/alpine-oraclejdk8
#WORKDIR /home/dev/code
COPY build/libs/product-catalogue-service-1.0.jar product-catalogue-service.jar
COPY src/main/resources/app-config.yml .

EXPOSE 8080 8001

CMD ["java", "-jar", "-Xms256m", "-Xmx768m", "product-catalogue-service.jar", "server", "app-config.yml"]
