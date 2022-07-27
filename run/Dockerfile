FROM jansimonek/caddy-maria-java:0.1.6

# Configure Caddy
RUN rm /etc/caddy/Caddyfile
COPY run/Caddyfile /etc/caddy/Caddyfile

# Build app
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME

# Define entrypoint
EXPOSE 80
COPY run/container_entrypoint.sh /usr/app/run/container_entrypoint.sh
ENTRYPOINT ["/bin/bash", "/usr/app/run/container_entrypoint.sh"]