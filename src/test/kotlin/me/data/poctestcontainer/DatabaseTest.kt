package  me.data.poctestcontainer

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
    fun `should create table`(){
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
                    // TODO: Mover sql para arquivo proprio
                    val sqlCreateTable = "CREATE TABLE testando_poc (\n" +
                            "  id int NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                            "  descricao VARCHAR(50) NOT NULL\n" +
                            ");"

                    statement.execute(sqlCreateTable)

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
                        var listaDeValor = ArrayList<Any>()
                        while(resultSet.next()) {
                            println("Id: ${resultSet.getString("id")}")
                            println("Descricao: ${resultSet.getString("descricao")}\n")

                            listaDeValor.add("id:${resultSet.getString("id")},descricao:${resultSet.getString("descricao")}")
                        }

                        // move cursor para ultimo item da lista de resultados
                        // https://stackoverflow.com/questions/192078/how-do-i-get-the-size-of-a-java-sql-resultset
                        resultSet.last()
                        Assertions.assertThat(resultSet.row).isEqualTo(2)
                        println(listaDeValor[0])
                        //Assertions.assertThat()
                    }
                }
            }
        } finally {
            mysqlContainer.stop()
        }
    }

}
