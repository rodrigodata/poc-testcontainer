package me.data.poctestcontainer

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class ContainersTestBase {

    constructor(){
        zookeeperContainer.start()
        kafkaContainer.start()
    }

    val network = Network.newNetwork()

    @Container
    val zookeeperContainer = GenericContainer<KafkaContainer>("zookeeper")
            .withExposedPorts(2181)
            .withNetwork(network)
            .withNetworkAliases("zookeeper.apache.org")
            .withEnv("ZOOKEEPER_CLIENT_PORT", "2181")


    @Container
    val kafkaContainer = GenericContainer<KafkaContainer>("debezium/kafka")
                .withExposedPorts(9092)
                .withNetwork(network)
                .withNetworkAliases("kafkacontainer")
                .withEnv("ZOOKEEPER_CONNECT", "zookeeper.apache.org:2181")


}