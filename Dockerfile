FROM openjdk:11-jdk
ENV APP_HOME=/opt/app/
WORKDIR $APP_HOME
COPY ./build/libs/* ./app.jar
EXPOSE 3180
CMD ["java", "-jar", "app.jar"]
