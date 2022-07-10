#!/usr/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# exit script on first error
set -e

function onError()
{
  echo -e
  echo -e "${RED}Failed${NC}"
}
trap onError ERR

echo ""
echo "Deploying"
echo "-------------"
echo ""

docker stop $(docker ps -q) | echo ""
docker build -f run/Dockerfile --tag kancl-online .

docker run -dt -p 80 -p 443 \
  -e DOMAIN="kancl.online" \
  --name kancl-online \
  -v caddy_data:/data \
  kancl-online

docker container prune -f

echo -e ""
echo -e "${GREEN}Success${NC}"
