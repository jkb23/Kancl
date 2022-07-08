#!/bin/sh

/scripts/run-maria-db.sh &
caddy run --config /etc/caddy/Caddyfile &
java -jar /usr/app/target/server-1.0-SNAPSHOT.jar
