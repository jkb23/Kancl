# TODO

1. Include DB in minimalist example.
2. Figure out how to easily run the stack locally (and update readme).
   1. Modify template and see result immediately.
   2. Fast to start and stop.
   3. DB in container (no need for caddy).
   4. Run Cypress on top of it.
   5. Use testrig/zoom.js in a CMD app to emulate zoom for local development?
3. Refactor `controller`.
4. More refactoring? Feedback from others.
5. Redeploy DB on prod only when DB was changed?

# How to install and use

Install docker. In Fedora:

	dnf install docker
	systemctl enable docker
	systemctl start docker

In Linux run [these post-installation steps](https://docs.docker.com/engine/install/linux-postinstall/)
to avoid using root.

Usage:

    # Create persistent volumes
	docker volume create --name=sql_data
    docker volume create --name=caddy_data

    # Build and start
    cd run
    docker-compose -f docker-compose.yml up --build

	# Forcing DB to be re-created:
	docker volume rm sql_data && docker volume create --name=sql_data

    # Running end to end tests:
    cd run
    docker-compose -f docker-compose.yml -f docker-compose-test.yml up --build --exit-code-from cypress

# Examples of Zoom calling the web hook

Here is the app that I tried out: [https://marketplace.zoom.us/develop/apps/xGNy_ZHYT2alQ98bgjfrGQ/information]()

```
{
    "event": "meeting.started",
    "payload": {
        "account_id": "PX5yQAcnT5azdXF5ma2Apw",
        "object": {
            "duration": 0,
            "start_time": "2022-03-23T12:33:26Z",
            "timezone": "",
            "topic": "Jan Šimonek's Zoom Meeting",
            "id": "86431910364",
            "type": 1,
            "uuid": "hwkAADUySwyG3GyUkqc0vA==",
            "host_id": "tUo7WohjTs6ELvl9WR3JAA"
        }
    },
    "event_ts": 1648038806658
}

{
    "payload": {
        "account_id": "PX5yQAcnT5azdXF5ma2Apw",
        "object": {
            "uuid": "hwkAADUySwyG3GyUkqc0vA==",
            "participant": {
                "user_id": "16778240",
                "user_name": "Jan Šimonek",
                "registrant_id": null,
                "participant_user_id": "tUo7WohjTs6ELvl9WR3JAA",
                "id": "tUo7WohjTs6ELvl9WR3JAA",
                "join_time": "2022-03-23T12:33:26Z",
                "email": "jan.simonek@gmail.com"
            },
            "id": "86431910364",
            "type": 1,
            "topic": "Jan Šimonek's Zoom Meeting",
            "host_id": "tUo7WohjTs6ELvl9WR3JAA",
            "duration": 0,
            "start_time": "2022-03-23T12:33:26Z",
            "timezone": ""
        }
    },
    "event_ts": 1648038809439,
    "event": "meeting.participant_joined"
}

{
    "event": "meeting.ended",
    "payload": {
        "account_id": "PX5yQAcnT5azdXF5ma2Apw",
        "object": {
            "duration": 0,
            "start_time": "2022-03-23T12:33:26Z",
            "timezone": "",
            "end_time": "2022-03-23T12:33:39Z",
            "topic": "Jan Šimonek's Zoom Meeting",
            "id": "86431910364",
            "type": 1,
            "uuid": "hwkAADUySwyG3GyUkqc0vA==",
            "host_id": "tUo7WohjTs6ELvl9WR3JAA"
        }
    },
    "event_ts": 1648038819111
}

{
    "payload": {
        "account_id": "PX5yQAcnT5azdXF5ma2Apw",
        "object": {
            "uuid": "hwkAADUySwyG3GyUkqc0vA==",
            "participant": {
                "leave_time": "2022-03-23T12:33:39Z",
                "user_id": "16778240",
                "user_name": "Jan Šimonek",
                "registrant_id": "",
                "participant_user_id": "tUo7WohjTs6ELvl9WR3JAA",
                "id": "tUo7WohjTs6ELvl9WR3JAA",
                "leave_reason": "left the meeting. Reason : Host ended the meeting.",
                "email": "jan.simonek@gmail.com"
            },
            "id": "86431910364",
            "type": 1,
            "topic": "Jan Šimonek's Zoom Meeting",
            "host_id": "tUo7WohjTs6ELvl9WR3JAA",
            "duration": 0,
            "start_time": "2022-03-23T12:33:26Z",
            "timezone": ""
        }
    },
    "event_ts": 1648038821536,
    "event": "meeting.participant_left"
}
```

# Production server set-up

## 1. Install docker, generate key
Follow installation [instructions for Docker](https://docs.docker.com/engine/install/debian/).
Enable Docker daemon and install docker-compose:
```
sudo systemctl enable docker
sudo systemctl start docker
sudo apt-get install docker-compose
```

## 2. Set-up environment variables

Add the following to `/etc/environment`
```
DOMAIN=kancl.online
```

Run `sudo visudo /etc/sudoers.d/preserve_server_env_variables`
Add the following content. This will preserve the env variables
when running sudo.
```
Defaults env_keep += "DOMAIN"
```

## 3. Create deployer user and directory for the app

Generate private+public key locally using:
```
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

```
sudo mkdir -p /opt/kancl.online
sudo adduser deployer --disabled-password

sudo su - deployer
mkdir .ssh
chmod 700 .ssh
touch .ssh/authorized_keys
chmod 600 .ssh/authorized_keys
# put the generated public key to .ssh/authorized_keys
exit
sudo chown deployer:deployer /opt/kancl.online
sudo usermod -aG docker deployer

# Install rsync
sudo apt-get install rsync

# Create persistent volume for Caddy data
sudo docker volume create --name=caddy_data
```

Upload the private key to GitLab > Repository Settings > CI/CD > Variables with name `SSH_PRIVATE_KEY`.
