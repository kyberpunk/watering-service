FROM adoptopenjdk:14-jre-hotspot
RUN addgroup --system spring && adduser --system spring && usermod -a -G spring spring
RUN mkdir /data && chmod 777 /data
USER spring:spring
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /home/spring/watering-service.jar
ARG PROPERTIES_FILE=build/resources/main/application.properties
COPY ${PROPERTIES_FILE} /home/spring/application.properties
ENTRYPOINT ["java","-jar","/home/spring/watering-service.jar","--spring.config.location=file:/docker/application.properties"]
EXPOSE 8080/tcp
