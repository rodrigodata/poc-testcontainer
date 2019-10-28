package me.data.poctestcontainer

import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.assertj.core.api.Assertions
import org.testcontainers.containers.Network
import java.io.File

@Testcontainers
class KafkaTest {
    // 1 -> Configurar container Kafka.
    // 2 -> Criar tópicos.
    // 3 -> Configurar container Zookeeper.
    // 4 -> Fazer seed de informações dentro do tópico do passo 2.

    val network = Network.newNetwork()

    @Container
    private val zookeeperContainer: GenericContainer<Nothing> =
            GenericContainer<Nothing>("zookeeper").apply {
                withExposedPorts(2181)
                withNetwork(network)
                withNetworkAliases("zookeepercontainer")
            }

    @Container
    private val kafkaContainer = GenericContainer<KafkaContainer>("debezium/kafka").apply {
        withExposedPorts(9092)
        withNetwork(network)
        withNetworkAliases("kafkacontainer")
        withEnv("ZOOKEEPER_CONNECT", "zookeepercontainer:2181") // ver necessidade deste arg
    }

//    @Container
//    private val kafkaContainer = KafkaContainer("debezium/kafka").apply {
//        withExposedPorts(9092)
//        withNetworkAliases("kafkacontainer")
//        withEnv("ZOOKEEPER_CONNECT", "zookeepercontainer:2181") // ver necessidade deste arg
//        withExternalZookeeper("zookeepercontainer:2181") // ver necessidade deste arg
//        withNetwork(network)
//        start()
//    }

    @Test
    fun `should kafka container be healthy`(){
        // esse teste ira verificar se o container do kafka está com status healthy baseado
        // na configuração de check que configuramos.
        zookeeperContainer.start()
        kafkaContainer.start()
        Assertions.assertThat(zookeeperContainer.isRunning).isTrue()
        Assertions.assertThat(kafkaContainer.isRunning).isTrue()
        File("logs_zookeeper.txt").writeText(zookeeperContainer.getLogs())
        File("logs_kafka.txt").writeText(kafkaContainer.getLogs())
    }
}