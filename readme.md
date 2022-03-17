# How to install


	sudo dnf install docker docker-compose
	sudo systemctl enable docker
	sudo systemctl start docker

	sudo docker volume create --name=sql_data
	sudo docker-compose up --build

	# Removing images
	sudo docker-compose down

	# Forcing SQL DB to be re-created
	sudo docker volume rm sql_data && sudo docker volume create --name=sql_data

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

### 2. Clone

```
git clone git@github.com:Strix-CZ/kancl-online.git
``` 

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
