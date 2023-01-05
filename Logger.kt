import java.io.File

object Logger {

    private val content = StringBuilder()

    fun add(log: String) = content.appendLine(log)

    fun save() {

        val file = File("File name:".reply())
        "The log has been saved.".printlnIt()
        file.writeText(content.toString())
    }
}
