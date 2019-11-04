package me.data.poctestcontainer.utils

import java.io.BufferedReader
import java.io.FileReader

fun readFile(path: String): String {
    val br = BufferedReader(FileReader(path))
    var lines = br.readLines()
    return lines.joinToString(separator = "")
}