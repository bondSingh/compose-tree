package img.tree

import java.io.InputStreamReader

object FileReader {
    fun readFileResource(filename: String) : String{
        val inputStream = FileReader::class.java.getResourceAsStream(filename)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}