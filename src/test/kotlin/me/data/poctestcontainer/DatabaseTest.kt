package  me.data.poctestcontainer

import me.data.poctestcontainer.utils.createTableScript
import me.data.poctestcontainer.utils.insertValuesScript
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.containers.MySQLContainer
import java.sql.DriverManager

@Testcontainers
class DatabaseTest {

    @Container
    private val mysqlContainer = MySQLContainer<Nothing>("mysql:5.7.22")

    @Test
    fun `should create table`() {
        try {
            //TODO: Remover configuração de container dentro de teste
            // Ver issue https://github.com/testcontainers/testcontainers-java/issues/932 para iniciar container
            // com usuario e senha custom.
            mysqlContainer.waitingFor(Wait.forLogMessage(".*ready for connections.*", 1))
            mysqlContainer.withCommand("--default-authentication-plugin=mysql_native_password")
            mysqlContainer.start()

            Assertions.assertThat(mysqlContainer.isRunning).isTrue()

            DriverManager.getConnection(mysqlContainer.jdbcUrl, mysqlContainer.username, mysqlContainer.password).use { connection ->
                connection.createStatement().use { statement ->

                    statement.execute(createTableScript())

                    statement.executeQuery("SELECT 1 as tabela FROM testando_poc;").use { resultSet ->
                        // nesse caso, como estamos apenas criando a tabela e nao estamos inserindo nenhum dado nela
                        // o simples fato de conseguirmos executar uma query sobre a tabela criada já conseguimos
                        // testar que a criação da mesma foi feita com sucesso.
                        Assertions.assertThat(true).isTrue()
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
            mysqlContainer.waitingFor(Wait.forLogMessage(".*ready for connections.*", 1))
            mysqlContainer.withCommand("--default-authentication-plugin=mysql_native_password")
            mysqlContainer.start()

            Assertions.assertThat(mysqlContainer.isRunning).isTrue()

            DriverManager.getConnection(mysqlContainer.jdbcUrl, mysqlContainer.username, mysqlContainer.password).use { connection ->
                connection.createStatement().use { statement ->

                    statement.execute(createTableScript())
                    statement.execute(insertValuesScript())

                    statement.executeQuery("SELECT * FROM testando_poc;").use { resultSet ->
                        var listaDeValor = ArrayList<Any>()
                        while (resultSet.next()) {
                            listaDeValor.add("id:${resultSet.getString("id")},descricao:${resultSet.getString("descricao")}")
                        }

                        Assertions.assertThat(listaDeValor.size).isEqualTo(2)

                    }
                }
            }
        } finally {
            mysqlContainer.stop()
        }
    }

}