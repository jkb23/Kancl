This project contains a working skeleton and infrastructure for a 1-week simulation
of real-life software development for college students - "Summer camp".
Participants will contribute to this repo when developing the product.
The project contains a GitLab CI/CD pipeline that automates builds, unit tests,
end-to-end tests, and deployment to a production server [kancl.online](https://kancl.online).

The project uses Maven to build a Java server, H2 database embedded into the server,
Cypress framework for end-to-end tests, and Docker for easy deployment.

# Usage

To use the application on your local computer, the app doesn't need any external program. The database
is also embedded into it. To run the application, use working directory as the root directory of the project
and then just run method `main` in class `Main` from within your IDE. Without an IDE, you can build the app
with `mvn package` and run it `java -jar target/server-1.0-SNAPSHOT.jar`. The running app is available
at [localhost:8081](http://localhost:8081/).

In production, there is also an HTTP server running alongside our Java app - [Caddy](https://caddyserver.com/).
Caddy translates HTTP traffic to and from our app into HTTPS (it acts like a reverse proxy).
In production, our Java app and Caddy are configured to run inside Docker containers.

### Tests

To support excellent quality of the application, we expect at least two different categories of tests to be created
and maintained as the application is developed - **unit tests** and **end-to-end tests**.

Unit tests are written in Java and can be executed in the IDE or by running `mvn test`.
You can find them in directory `src/test/java`.

End-to-end tests exercise the application as a whole in its Docker containers. We have prepared
[Cypress](https://www.cypress.io/) testing framework for you. Cypress tests are written in
JavaScript or TypeScript and you can find them in directory `src/test/cypress`.
There are two ways that you can execute them.

The first way is to run end-to-end tests in the terminal by running `mvn integration-test`. You can
also invoke Maven phase `integration-test` from your IDE (sometimes it is also called `verify`).
You can inspect test failures by investigating the log, looking at screenshots and videos that capture
what the test saw. You can find them in directories `src/test/cypress/screenshots` and `src/test/cypress/videos`.

The second option is to work in Cypress UI, which will give you a faster feedback loop and
more interactive way to investigate test failures. To install Cypress run `npm install && npx cypress install`.
Make sure the app is running and run:

```shell
npx cypress open
```

This will open Cypress UI. Wait a bit, click on E2E Testing, choose a browser and a test 'Spec'.
The test will be executed within the selected browser, you can replay individual steps and inspect
failures. The test is executed again whenever the source file changes.

### Continuous integration / Continuous deployment (CI/CD)

To make tests useful, you need to run them often. And the best way to do that is to automate it. We have prepared
a place that does just that - [CI/CD pipeline in GitLab](https://gitlab.com/jan.simonek/kancl-online/-/pipelines).
After you push to GitLab repository, the following steps are done:

1. Stage `build` executes Maven which builds Java, runs unit tests and creates `.jar` file.
2. Stage `end-to-end-tests` starts a container, runs our Java app and Caddy and executes Cypress tests.
3. If you push to branch `main` there's also stage `deploy`. This stages updates
   production server [kancl.online](https://kancl.online/).

The CI/CD pipeline is configured in file `.gitlab-ci.yml`.

### Working with DB

We have prepared [H2 Database](http://h2database.com). H2 is a native to Java and can run "embedded" into another
Java program (as opposed to running it in "server" mode as a standalone process).

There are no production users of our application (yet? :-) so we can afford to occasionally throw away all data in the
DB
and re-create it from scratch. This is done by set of scripts in directory `src/main/resources/sql`. Use these scripts
to create DB schema for your application. Scripts need to have extension `.sql` and are executed in alphabetical order.

When you start the application a class `SchemaCreator` within the container will throw away the DB
and re-create it every time content of directory `src/main/resources/sql` is changed.

The DB content is stored in directory `db`. You can use your IDE or other database tools to inspect the content of the
DB,
but please note only a single process can access the DB at any time. This means you will not be able to connect your
when our Java app is running and vice versa.

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

# Parts that participants may struggle with

- Missing `Router`. URLs everywhere
- Form validation
- Misuse of `var`
