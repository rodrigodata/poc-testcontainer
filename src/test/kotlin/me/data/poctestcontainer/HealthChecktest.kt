package  me.data.poctestcontainer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.MySQLContainer
import java.io.File
import java.nio.file.Paths
import java.sql.DriverManager
import java.util.*

// TODO: mover testes que nao tratam de api para outros arquivos.
@Testcontainers
class HealthChecktest {

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
    fun `should create table`(){
        try {
            mysqlContainer.waitingFor(Wait.forLogMessage(".*ready for connections.*", 1))
            mysqlContainer.withCommand("--default-authentication-plugin=mysql_native_password")
            mysqlContainer.start()

            Assertions.assertThat(mysqlContainer.isRunning).isTrue()

            DriverManager.getConnection(mysqlContainer.jdbcUrl, mysqlContainer.username, mysqlContainer.password).use { connection ->
                connection.createStatement().use { statement ->
                    // TODO: Mover sql para arquivo proprio
                    val sqlCreateTable = "CREATE TABLE testando_poc (\n" +
                            "  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  descricao VARCHAR(50) NOT NULL\n" +
                            ");"

                    // Criamos a tabela
                    statement.execute(sqlCreateTable)

                    statement.executeQuery("SELECT 1 as tabela FROM testando_poc;").use { resultSet ->
                        resultSet.last()
                        Assertions.assertThat("").isEqualTo(0)
                    }
                }
            }

        } finally {
            mysqlContainer.stop()
        }
    }

    @Test
    fun `should table size be equals 2`() {
        try {
            //TODO: Remover configuração de container dentro de teste
            // Ver issue https://github.com/testcontainers/testcontainers-java/issues/932 para iniciar container
            // com usuario e senha custom.
             //mysqlContainer.withDatabaseName("teste")
//             mysqlContainer.withUsername("rodrigo")
//             mysqlContainer.withPassword("data")
            mysqlContainer.waitingFor(Wait.forLogMessage(".*ready for connections.*", 1))
            mysqlContainer.withCommand("--default-authentication-plugin=mysql_native_password")
            mysqlContainer.start()

            Assertions.assertThat(mysqlContainer.isRunning).isTrue()

            DriverManager.getConnection(mysqlContainer.jdbcUrl, mysqlContainer.username, mysqlContainer.password).use { connection ->
                connection.createStatement().use { statement ->
                    // TODO: Mover sql para arquivo proprio
                    val sqlCreateTable = "CREATE TABLE testando_poc (\n" +
                            "  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  descricao VARCHAR(50) NOT NULL\n" +
                            ");"

                    // Criando tabela
                    statement.execute(sqlCreateTable)

                    // Inserindo dados iniciais
                    statement.execute("INSERT INTO testando_poc (descricao) VALUES (\"minha primeira descricao\");")
                    statement.execute("INSERT INTO testando_poc (descricao) VALUES (\"minha segunda descricao\");")

                    statement.executeQuery("SELECT * FROM testando_poc;").use { resultSet ->
                        while(resultSet.next()) {
                            println("Id: ${resultSet.getString("id")}")
                            println("Descricao: ${resultSet.getString("descricao")}\n")
                        }

                        // move cursor para ultimo item da lista de resultados
                        // https://stackoverflow.com/questions/192078/how-do-i-get-the-size-of-a-java-sql-resultset
                        resultSet.last()
                        Assertions.assertThat(resultSet.row).isEqualTo(2)
                    }
                }
            }
        } finally {
            mysqlContainer.stop()
        }
    }
}