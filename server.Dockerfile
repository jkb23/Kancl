# Build the server in maven container
FROM maven:3.8.4-jdk-11 AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify --fail-never
ADD . $HOME
RUN mvn package

# Run the server in distroless container
# https://github.com/GoogleContainerTools/distroless

# The following base image containes just a bare minimum to run the app.
# It doesn't even have shell access.
#FROM gcr.io/distroless/java
# The following variation of the base image includes linux utilities (busybox)
# including shell. This is how to run it in the container:
# sudo docker run --entrypoint=sh -ti kancl-online_server
FROM gcr.io/distroless/java:debug

COPY --from=build /usr/app/target/server-1.0-SNAPSHOT.jar /usr/app/server-1.0-SNAPSHOT.jar
COPY web /usr/app/web
EXPOSE 8080
WORKDIR /usr/app
ENTRYPOINT ["java","-jar","/usr/app/server-1.0-SNAPSHOT.jar"]
