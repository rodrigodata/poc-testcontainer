package  me.data.poctestcontainer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.*
import org.testcontainers.containers.PostgreSQLContainer
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Paths
import java.util.stream.Collectors

@Testcontainers
class HealthChecktest {

    private var log = LoggerFactory.getLogger(HealthChecktest::class.java)

//    @Container
//    private val healthCheckContainer: GenericContainer<Nothing> = GenericContainer<Nothing>(
//            ImageFromDockerfile()
//                    .withFileFromPath("./target/poctestcontainer-2.1.9.RELEASE.jar", Paths.get("./target/poctestcontainer-2.1.9.RELEASE.jar"))
//                    .withFileFromPath("Dockerfile", Paths.get("./Dockerfile"))
//    ).apply {
//        withExposedPorts(8080)
//        waitingFor(Wait.forLogMessage(".*Started PoctestcontainerApplicationKt.*", 1))
//        start()
//        followOutput(Slf4jLogConsumer(log))
//    }

    @Container
    private val mysqlContainer = MySQLContainer<Nothing>("mysql:5.7.22").apply {
    withDatabaseName("teste")
        withUsername("root")
        withPassword("root")
        withExposedPorts(3306)
        waitingFor(Wait.forLogMessage(".*ready for connections.*", 1))
    }

//    @Test
//    fun `should perform an api request healthcheck`() {
//
//        Assertions.assertThat(healthCheckContainer.isRunning).isTrue()
//
//        val url = URL("http://${healthCheckContainer.containerIpAddress}:${healthCheckContainer.firstMappedPort}/healthcheck")
//
//        val conn = url.openConnection() as HttpURLConnection
//
//        Assertions.assertThat(conn.responseCode).isEqualTo(200)
//
//        File("response_body.txt").writeText(BufferedReader(InputStreamReader(conn.getInputStream())).lines().collect(Collectors.joining()))
//
//    }

    @Test
    fun `should perform an query selecting records from database`() {
        try {
            mysqlContainer.start()
            Assertions.assertThat(mysqlContainer.isRunning).isTrue()
            println("Logado na base de dados ${mysqlContainer.databaseName}, com o usuario ${mysqlContainer.username} e senha ${mysqlContainer.password}.")
        } finally {
            mysqlContainer.stop()
        }
    }
}