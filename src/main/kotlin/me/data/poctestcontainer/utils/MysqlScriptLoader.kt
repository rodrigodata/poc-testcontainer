package me.data.poctestcontainer.utils

private const val createTableScriptPath = "scripts/sql/mysql_table_creation.sql"
private const val insertValuesScriptPath = "scripts/sql/mysql_table_insert.sql"

fun createTableScript(): String = readFile(createTableScriptPath)

fun insertValuesScript(): String = readFile(insertValuesScriptPath)