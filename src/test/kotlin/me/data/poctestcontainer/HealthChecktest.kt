package  me.data.poctestcontainer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.MySQLContainer
import java.sql.DriverManager

@Testcontainers
class HealthChecktest : AbstractContainerDatabaseTest() {

    private var log = LoggerFactory.getLogger(HealthChecktest::class.java)
//
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
    private val mysqlContainer = MySQLContainer<Nothing>("mysql:5.7.22")

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

            //TODO: Remover configuração de container dentro de teste
            mysqlContainer.withDatabaseName("teste")
            mysqlContainer.withUsername("testeuser")
            mysqlContainer.withPassword("testepass")
            mysqlContainer.withExposedPorts(3306)
            mysqlContainer.waitingFor(Wait.forLogMessage(".*ready for connections.*", 1))
            mysqlContainer.withCommand("--default-authentication-plugin=mysql_native_password")
            mysqlContainer.start()

            Assertions.assertThat(mysqlContainer.isRunning).isTrue()
            println("Container configurado -> Database ${mysqlContainer.databaseName}, com o usuario ${mysqlContainer.username} e senha ${mysqlContainer.password}.")

            // Executar comando junto ao container
            //val resultSet = performQuery(mysqlContainer, "SELECT 1")
            println("\n\n\n\n\n\n\n\n ${mysqlContainer.jdbcUrl} \n\n\n\n\n\n\n\n ")
            DriverManager.getConnection(mysqlContainer.jdbcUrl, mysqlContainer.username, mysqlContainer.password).use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("SELECT 1").use { resultSet ->
                        resultSet.next()
                        Assertions.assertThat(resultSet.getInt(1)).isEqualTo(2)
                    }
                }
            }
        } finally {
            mysqlContainer.stop()
        }
    }
}