FROM openjdk:8-jdk-alpine

VOLUME /tmp

WORKDIR /app

COPY . /app

COPY ./target/poctestcontainer-2.1.9.RELEASE.jar /app/poctestcontainer-2.1.9.RELEASE.jar

# The EXPOSE instruction does not actually publish the port.
# It functions as a type of documentation between the person who builds the image and the person who runs the container,
# about which ports are intended to be published. To actually publish the port when running the container,
# use the -p flag on docker run to publish and map one or more ports, or the -P flag to publish all exposed ports and
# map them to high-order ports.
EXPOSE 8080

CMD java -Xmx2g -Dspring.profiles.active=$ENVIRONMENT -jar poctestcontainer-2.1.9.RELEASE.jar