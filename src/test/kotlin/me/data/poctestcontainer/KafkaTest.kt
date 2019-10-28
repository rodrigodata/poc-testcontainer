package me.data.poctestcontainer

import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.assertj.core.api.Assertions
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.Network
import java.io.File

@Testcontainers
class KafkaTest {
    // 1 -> Configurar container Kafka.
    // 2 -> Criar tópicos.
    // 3 -> Configurar container Zookeeper.
    // 4 -> Fazer seed de informações dentro do tópico do passo 2.

    // network que será compartilhada entre os containers
    private val network = Network.newNetwork()

    @Container
    private val zookeeperContainer: GenericContainer<KafkaContainer> = GenericContainer("zookeeper")

    @Container
    private val kafkaContainer: GenericContainer<KafkaContainer>  = GenericContainer("debezium/kafka")

    @Test
    fun `should kafka container be healthy`(){
        // esse teste ira verificar se o container do kafka está com status healthy baseado
        // na configuração de check que configuramos.
        zookeeperContainer.withNetwork(network)
        zookeeperContainer.withNetworkAliases("zookeepercontainer")
        zookeeperContainer.withExposedPorts(2181)
        zookeeperContainer.waitingFor(Wait.forListeningPort())
        zookeeperContainer.start()
        println("\n\n")
        println("zookeepercontainer...")
        println(zookeeperContainer.networkAliases)
        println(zookeeperContainer.env)

        kafkaContainer.withEnv("ZOOKEEPER_CONNECT", "zookeepercontainer:2181")
        kafkaContainer.withNetwork(network)
        kafkaContainer.withNetworkAliases("kafkacontainer")
        kafkaContainer.withExposedPorts(9092)
        kafkaContainer.waitingFor(Wait.forListeningPort())
        kafkaContainer.withEnv("ZOOKEEPER_CONNECT", "zookeepercontainer:2181")
        kafkaContainer.start()


        println("\n\n")
        println("kafkaContainer...")
        Assertions.assertThat(kafkaContainer.isRunning).isTrue()

//        kafkaContainer.execInContainer(
//                "/kafka/bin/kafka-topics.sh",
//                "--create",
//                "--zookeeper",
//                "zookeeper",
//                "--replication-factor",
//                "1",
//                "--partitions",
//                "1",
//                "--topic",
//                "breitner.public.entries")


//
//
//        println("\n\n")
//        println("kafkaContainer...")
//        println(kafkaContainer.networkAliases)
//        println(kafkaContainer.network)
//        println(kafkaContainer.networkMode)

        //Assertions.assertThat(kafkaContainer.isRunning).isTrue()
        File("logs_zookeeper.txt").writeText(zookeeperContainer.getLogs())
        File("logs_kafka.txt").writeText(kafkaContainer.getLogs())
    }
}