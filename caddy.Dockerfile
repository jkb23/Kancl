FROM caddy:2.4.6

RUN rm /etc/caddy/Caddyfile
COPY Caddyfile /etc/caddy/Caddyfile
