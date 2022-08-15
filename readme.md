# TODOs

0. mvn customCommand na spusteni e2e testu, stacku, db ...
1. Introduce persisted form field, refactor DB access, introduce DB test, DAO
2. Replace lib for parsing JSON
3. Feedback from others
4. Use testrig/zoom.js in a CMD app to emulate zoom for local development?
5. GitLab instance? Repo user limit? Our own CI/CD runner?

# Installing pre-requisites

**TODO** pre-install and update accordingly

1. Install [Maven](https://maven.apache.org/).
2. Install [Docker](https://docs.docker.com/engine/install/). In Fedora you can run `sudo dnf install docker && sudo systemctl enable docker && sudo systemctl start docker`
3. On Linux run [these post-installation steps](https://docs.docker.com/engine/install/linux-postinstall/)
to avoid using root.

# Usage

All dependencies for our application and the application itself are packaged in a Docker container.

If you have never used a Docker container before, don't worry. The idea is similar to a Virtual Machine.
We have prepared an "image" that contains a lightweight operating system and all dependencies that our app needs.
Docker will download this image and use it to run our application in an isolated environment.
A running image is called "container".

To run the app on your computer follow these steps:

1. When running it for the first time:
   `docker volume create --name=sql_data && docker volume create --name=caddy_data`
2. `mvn package` builds our Java application using Maven into a `.jar` file
3. `cd run && docker-compose up --build` This will automatically:
   - Download the docker image
   - Copy `.jar` file into it
   - Re-create DB as necessary (see below)
   - Run the application
4. The app is now available at [localhost:8080](http://localhost:8080/)

### Tests

To support excellent quality of the application, we expect at least two different categories of tests to be created
and maintained as the application is developed - **unit tests** and **end-to-end tests**.

Unit tests are written in Java and can be executed in the IDE or by running `mvn test`.
You can find them in directory `src/test/java`.

End-to-end tests exercise the application as a whole in its environment. We have prepared
[Cypress](https://www.cypress.io/) testing framework for you. Cypress tests are written in
JavaScript or TypeScript and you can find them in directory `src/test/cypress/e2e`
To run them you can either install Cypress locally or you can use Docker image to run them
from the command line:

```sh
cd run
docker-compose -f docker-compose.yml -f docker-compose-test.yml up --build --exit-code-from cypress
```

You can inspect test failures by investigating the log, looking at screenshots and videos
that capture what the test saw. You can also install and run Cypress locally.

### Continuous integration / Continuous deployment (CI/CD)

To make tests useful, you need to run them often. And the best way to do that is to automate it. We have prepared
a place that does just that - [CI/CD pipeline in GitLab](https://gitlab.com/jan.simonek/kancl-online/-/pipelines).
After you push to GitLab repository, the following steps are done:

1. Stage `build` executes Maven which builds Java, runs unit tests and creates `.jar` file.
2. Stage `end-to-end-tests` starts a container, creates a DB and executes Cypress tests.
3. If you push to branch `main` there's also stage `deploy`. This stages updates
   production server [kancl.online](https://kancl.online/).

### Working with DB

There are no production users of our application (yet? :-) so we can afford to occasionally throw away all data in the DB
and re-create it from scratch. This is done by set of scripts in the `sql` directory. Use these scripts to create DB schema
for your application. Scripts need to have extension `.sql` and are executed in alphabetical order.

When you start the container a script `/scripts/prepare-maria-db.sh` within the container will throw away the DB
and re-create it every time content of `sql` directory is changed.

To persist the DB between restarts of the container persistent volumes are used. That's why you had to manually create them
before starting the container for the first time with `docker volume create`.

### Local development

When you are developing you want to run the Java application from within your IDE.
Running the app in the container would make it hard to re-run and debug.
To be able to run the app locally, you need to set environment variables to same values as in file `run/.env`.
In IntelliJ Idea, you can copy&paste the following into run configuration:

```
ZOOM_VERIFICATION_TOKEN=foobar; MYSQL_USER=user; MYSQL_PASSWORD=password; MYSQL_DATABASE=kanclOnline
```

And then start the DB in Docker:

```sh
cd run
docker-compose -f docker-compose.yml -f docker-compose-db-only.yml up --build
```


# Examples of Zoom calling the web hook

Here is the app that I tried out: [https://marketplace.zoom.us/develop/apps/xGNy_ZHYT2alQ98bgjfrGQ/information]()

```json
[
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
    },
    
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
    },
    
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
    },
    
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
]
```

# Production server set-up

## 1. Install docker, generate key
Follow installation [instructions for Docker](https://docs.docker.com/engine/install/debian/).
Enable Docker daemon and install docker-compose:
```sh
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
```sh
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

```sh
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
