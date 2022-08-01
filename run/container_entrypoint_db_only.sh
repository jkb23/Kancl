#!/bin/sh

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


/scripts/prepare-maria-db.sh
/scripts/run-maria-db.sh
