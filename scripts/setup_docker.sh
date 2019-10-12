docker build . -t poctestcontainer

docker run -m512M --cpus 2 --rm -d -p 8080:8080 --name poctestcontainer poctestcontainer