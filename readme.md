# How to install


	sudo dnf install docker docker-compose
	sudo systemctl enable docker
	sudo systemctl start docker

	sudo docker volume create --name=caddy_data
	sudo docker-compose up --build

	# Removing images
	sudo docker-compose down

	# Forcing SQL DB to be re-created
	sudo docker volume rm sql_data && sudo docker volume create --name=caddy_data
