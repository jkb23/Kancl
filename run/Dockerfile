FROM jansimonek/caddy-maria-java:0.1.1

# Configure Caddy
RUN rm /etc/caddy/Caddyfile
COPY run/Caddyfile /etc/caddy/Caddyfile

# Prepare MariaDB
ENV MYSQL_USER="user" \
	MYSQL_PASSWORD="password" \
	MYSQL_ROOT_PASSWORD="rootpassword" \
	MYSQL_DATABASE="kanclOnline"
RUN /scripts/prepare-maria-db.sh

# Build app
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME

# Make app available
ENV ZOOM_VERIFICATION_TOKEN="foobar"
EXPOSE 80
ENTRYPOINT ["/bin/bash", "/usr/app/run/run.sh"]

