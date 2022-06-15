# How to install

Install docker. In Fedora:

	dnf install docker docker-compose
	systemctl enable docker
	systemctl start docker

In Linux run [these post-installation steps](https://docs.docker.com/engine/install/linux-postinstall/)
to avoid using root.

Usage:

    #Create volume to store DB data:
	docker volume create --name=sql_data

    # Re-build and start:
	docker-compose up --build

	# Turn off:
	docker-compose down

	# Forcing DB to be re-created:
	docker volume rm sql_data && docker volume create --name=sql_data

    # Running end to end tests:
    docker-compose -f docker-compose.yml -f test/docker-compose.test.yml up --build --exit-code-from cypress

You can also set environment variables `HTTP_PORT` and `HTTPS_PORT`
to set on which ports the server will listen. The defaults are specified in file `.env`

## Production server set-up

### 1. Install docker, generate key
```
sudo apt install docker docker-compose git
sudo systemctl enable docker
sudo systemctl start docker
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```
Add the generated public key as
[GitHub deploy key](https://github.com/Strix-CZ/kancl-online/settings/keys).

### 2. Set-up environment variables

Add the following to `/etc/environment`
```
HTTP_PORT=80
HTTPS_PORT=443
DOMAIN=kancl.online
```

Run `sudo visudo /etc/sudoers.d/preserve_server_env_variables`
Add the following content. This will preserve the env variables
when running sudo.
```
Defaults env_keep += "HTTP_PORT HTTPS_PORT DOMAIN"
```

### 3. Clone and set-up git hooks
The following steps will allow the production server to automatically
test and deploy the code whenever git repository receives a new commit
in branch `main`.

```
# clone repo and copy post-receive script out of it
cd ~
git clone git@github.com:Strix-CZ/kancl-online.git kancl-online
cp kancl-online/git_hooks/post-receive post-receive
rm -rf kancl-online

# clone repo as bare and move post-receive script into the hooks dir
git clone --bare git@github.com:Strix-CZ/kancl-online.git kancl-online
mv post-receive kancl-online/hooks/
```

**TODO**: put the repo in a shared folder, create a user group with access to the repo, create users with access to the repo 

To be able to push to the bare repo easily (`git push production`) add it as a new remote:
```
git remote add production ssh://USER@URL/~/kancl-online
```


## Examples of Zoom calling the web hook

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
