fun <T> T.printlnIt() = println(this).also { Logger.add(this.toString()) }
fun <T> T.reply() = this.printlnIt().run { readln().also { Logger.add(it) } }

fun main(vararg args: String) {

    val flashcards = Flashcards()

    val filenameForImport = args.indexOf("-import").run { if (this >= 0 && args.lastIndex > this) args[this + 1] else null }
    val filenameForExport = args.indexOf("-export").run { if (this >= 0 && args.lastIndex > this) args[this + 1] else null }

    if (filenameForImport != null) flashcards.import(filenameForImport)

    while (true) {
        when ("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):".reply()) {
            "add" -> flashcards.add()
            "remove" -> flashcards.remove()
            "import" -> flashcards.import()
            "export" -> flashcards.export()
            "ask" -> flashcards.ask()
            "exit" -> break
            "log" -> Logger.save()
            "hardest card" -> flashcards.hardestCard()
            "reset stats" -> flashcards.resetStats()
            else -> "Invalid action.".printlnIt()
        }
    }

    if (filenameForExport != null) flashcards.export(filenameForExport)
    "Bye bye!".printlnIt()
}
