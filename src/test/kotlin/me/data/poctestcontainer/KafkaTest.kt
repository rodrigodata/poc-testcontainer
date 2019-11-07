package me.data.poctestcontainer

import org.junit.jupiter.api.Test

import org.testcontainers.junit.jupiter.Testcontainers
import org.assertj.core.api.Assertions
import java.io.File

@Testcontainers
class KafkaTest : ContainersTestBase() {
    // 1 -> Configurar container Kafka.
    // 2 -> Criar tópicos.
    // 3 -> Configurar container Zookeeper.
    // 4 -> Fazer seed de informações dentro do tópico do passo 2.

    // network que será compartilhada entre os containers


    @Test
    fun `should kafka container be healthy`(){

        Assertions.assertThat(zookeeperContainer.isRunning).isTrue()

        println("\n\n")
        println("kafkaContainer...")
        println("kafkacontainer network -> ${kafkaContainer.network}")
        println("kafkacontainer networkAliases -> ${kafkaContainer.networkAliases}")
        println("zookeepercontainer network -> ${zookeeperContainer.network}")
        println("zookeepercontainer networkAliases -> ${zookeeperContainer.networkAliases}")
        println("zookeepercontainer hosts.. -> ${zookeeperContainer.extraHosts}")
        println("\n\n")

        // logs containers
        File("logs_zookeeper.txt").writeText(zookeeperContainer.getLogs())
        File("logs_init_kafka.txt").writeText(kafkaContainer.getLogs())

        Assertions.assertThat(kafkaContainer.isRunning).isTrue()

        kafkaContainer.execInContainer(
                "/kafka/bin/kafka-topics.sh",
                "--create",
                "--zookeeper",
                "zookeeper",
                "--replication-factor",
                "1",
                "--partitions",
                "1",
                "--topic",
                "breitner.public.entries")



        File("logs_create_topic_kafka.txt").writeText(kafkaContainer.getLogs())
        File("logs_create_topic_zookeeper.txt").writeText(zookeeperContainer.getLogs())



    }
}