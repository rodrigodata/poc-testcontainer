package  me.data.poctestcontainer

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
// import org.junit.jupiter.api.Test

import java.sql.DriverManager

@Testcontainers
class HealthChecktest {

    @Container
    private val container = PostgreSQLContainer<Nothing>("postgres:11")


    @Test
    fun `should perform simple query`() {

        container.start()

        DriverManager.getConnection(container.jdbcUrl, container.username, container.password).use { conn ->

            conn.createStatement().use { stmt ->

                stmt.executeQuery("SELECT 1").use { resultSet ->

                    resultSet.next()

                    assertThat(resultSet.getInt(1)).isEqualTo(1)
                }
            }
        }
    }


}