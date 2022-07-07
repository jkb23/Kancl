FROM jansimonek/caddy-maria-maven:0.1

# Configure Caddy
RUN rm /etc/caddy/Caddyfile
COPY run/Caddyfile /etc/caddy/Caddyfile
ENV HTTP_PORT="8080" \
	HTTPS_PORT="8081"

# Configure MariaDB
ENV MYSQL_USER="user" \
	MYSQL_PASSWORD="password" \
	MYSQL_ROOT_PASSWORD="rootpassword" \
	MYSQL_DATABASE="kanclOnline"

# Build app
ENV HOME=/usr/app
RUN mkdir -p /root/.m2
COPY run/maven-settings.xml /root/.m2/settings.xml
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify --fail-never
ADD . $HOME
RUN mvn package

# Make app available
ENV ZOOM_VERIFICATION_TOKEN="foobar"
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/target/server-1.0-SNAPSHOT.jar"]
