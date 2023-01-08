import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

object Util {
    /** Read all lines from the file under given [filePath] in a `resources` folder. */
    fun readInputLines(filePath: String): List<String> {
        val path = javaClass.classLoader.getResource(filePath)?.path ?: error("File not found")
        return File(path).readLines()
    }

    /** Read the puzzle input from file [fileName] for year [adventOfCodeYear] and day [adventOfCodeDay]. */
    fun readInputLines(adventOfCodeYear: Int, adventOfCodeDay: Int, fileName: String): List<String> =
        readInputLines("puzzleInputs/$adventOfCodeYear/%02d/$fileName".format(adventOfCodeDay))

    /** Converts string to md5 hash. */
    fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
            .toString(16)
            .padStart(32, '0')
}