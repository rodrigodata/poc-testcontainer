package me.data.poctestcontainer

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet
import javax.sql.DataSource
import org.testcontainers.containers.JdbcDatabaseContainer
import org.junit.After
import java.util.HashSet

abstract class AbstractContainerDatabaseTest {

    private val datasourcesForCleanup = HashSet<HikariDataSource>()

    fun performQuery(container: JdbcDatabaseContainer<*>, sql: String): ResultSet {
        val ds = getDataSource(container)
        val statement = ds.getConnection().createStatement()
        statement.execute(sql)
        val resultSet = statement.getResultSet()

        resultSet.next()
        return resultSet
    }

    fun getDataSource(container: JdbcDatabaseContainer<*>): DataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = container.getJdbcUrl()
        hikariConfig.username = container.getUsername()
        hikariConfig.password = container.getPassword()
        hikariConfig.driverClassName = container.getDriverClassName()

        val dataSource = HikariDataSource(hikariConfig)
        datasourcesForCleanup.add(dataSource)

        return dataSource
    }

    @After
    fun teardown() {
        datasourcesForCleanup.forEach(HikariDataSource::close)
    }
}