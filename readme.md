# How to install


	sudo dnf install docker docker-compose
	sudo systemctl enable docker
	sudo systemctl start docker

	sudo docker volume create --name=caddy_data
	sudo docker-compose up --build
