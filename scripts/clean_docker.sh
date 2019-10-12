docker stop "$(docker container ls -a -q -f name=poctestcontainer)"

docker rm "$(docker container ls -a -q -f name=poctestcontainer)"