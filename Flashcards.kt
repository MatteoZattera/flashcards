import java.io.File

class Flashcards {

    inner class Flashcard(val term: String, val definition: String, var mistakes: UInt = 0U)

    private val flashcards = mutableListOf<Flashcard>()
    private val delimiter = Regex("##")

    private val List<Flashcard>.terms get() = this.map { it.term }
    private val List<Flashcard>.definitions get() = this.map { it.definition }

    private fun MutableList<Flashcard>.remove(term: String) = if (remove(this.find { it.term == term })) true else null

    fun add() {

        val term = "The card:".reply()
        if (term in flashcards.terms) "The card \"$term\" already exists.".printlnIt().also { return }

        val definition = "The definition of the card:".reply()
        if (definition in flashcards.definitions) "The definition \"$definition\" already exists.".printlnIt().also { return }

        flashcards.add(Flashcard(term, definition))
        "The pair (\"$term\":\"$definition\") has been added.".printlnIt()
    }

    fun remove() {

        val term = "Which card?".reply()
        flashcards.remove(term)?.also { "The card has been removed.".printlnIt() } ?: "Can't remove \"$term\": there is no such card.".printlnIt()
    }

    fun import(filename: String? = null) {

        val file = File(filename ?: "File name:".reply())
        if (!file.exists() || file.isDirectory) "File not found.".printlnIt().also { return }

        try {
            val newFlashcards = file.readLines().map {
                it.split(delimiter, 3).run {
                    Flashcard(
                        get(0),
                        getOrElse(1) { throw IllegalStateException("Invalid file content.") },
                        getOrElse(2) { throw IllegalStateException("Invalid file content.") }.toUIntOrNull() ?: 0U
                    )
                }
            }

            flashcards.removeIf { it.term in newFlashcards.terms }
            flashcards.addAll(newFlashcards)
            "${newFlashcards.size} cards have been loaded.".printlnIt()

        } catch (e: IllegalStateException) {
            e.message.printlnIt()
        }
    }

    fun export(filename: String? = null) {

        val file = File(filename ?: "File name:".reply())

        file.writeText(flashcards.joinToString("\n") { "${it.term}$delimiter${it.definition}$delimiter${it.mistakes}" })
        "${flashcards.size} cards have been saved.".printlnIt()
    }

    fun ask() {

        val numberOfQuestions = "How many times to ask?".reply().toIntOrNull() ?: 0

        repeat(numberOfQuestions) {
            val randomFlashcard = flashcards.random()
            val reply = "Print the definition of \"${randomFlashcard.term}\":".reply()
            when (reply) {
                randomFlashcard.definition -> "Correct!"
                in flashcards.definitions -> {
                    flashcards.first { it.term == randomFlashcard.term }.mistakes++
                    "Wrong. The right answer is \"${randomFlashcard.definition}\", but your definition is " +
                            "correct for \"${flashcards.first { it.definition == reply }.term}\"."
                }
                else -> {
                    flashcards.first { it.term == randomFlashcard.term }.mistakes++
                    "Wrong. The right answer is \"${randomFlashcard.definition}\"."
                }
            }.printlnIt()
        }
    }

    fun hardestCard() {

        val highestMistakeNumber = if (flashcards.isEmpty()) 0U else flashcards.maxOf { it.mistakes }
        val mostMistakenCards = flashcards.filter { it.mistakes == highestMistakeNumber && it.mistakes != 0U }

        when (mostMistakenCards.size) {
            0 -> "There are no cards with errors."
            1 -> "The hardest card is \"${mostMistakenCards[0].term}\". You have $highestMistakeNumber errors answering it."
            else -> mostMistakenCards
                .joinToString(", ", "The hardest cards are ", ". You have $highestMistakeNumber errors answering them.") { "\"${it.term}\"" }
        }.printlnIt()
    }

    fun resetStats() = flashcards.forEach { it.mistakes = 0U }.also { "Card statistics have been reset.".printlnIt() }
}
