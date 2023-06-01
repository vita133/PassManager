import com.example.passmanager.login.model.generation.CharacterSet
import kotlinx.coroutines.runBlocking

class PasswordGenerator(private val length: Int) {
    init {
        require(length > 0) { "Length should be more than 0" }
    }

    private val characterSetsList: MutableList<CharacterSet> = mutableListOf()

    fun add(characterSet: CharacterSet) {
        characterSetsList.add(characterSet)
    }

    fun clear() {
        characterSetsList.clear()
    }

    fun generatePassword(): String = runBlocking {
        val password: String = generateRandomPassword()
        println("Password generated: $password")
        return@runBlocking password
    }

    private fun generateRandomPassword(): String {
        val allowedChars = characterSetsList
            .flatMap { it.getAllowedChars() }

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
