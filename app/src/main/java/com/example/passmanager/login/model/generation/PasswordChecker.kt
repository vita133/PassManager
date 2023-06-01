import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.security.MessageDigest
import java.util.*

class PasswordChecker {

    private val client = OkHttpClient()

    suspend fun isPasswordCompromised(password: String): Boolean {
        val hashedPassword = hashPassword(password)
        val prefix = hashedPassword.substring(0, 5)
        val postfix = hashedPassword.substring(5).uppercase(Locale.ROOT)
        println("Password postfix: $postfix")

        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://api.pwnedpasswords.com/range/${prefix}")
                .build()

            println("Request URL: ${request.url}")

            try {
                val response: Response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                response.close()

                println("Response Body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    val hashes = responseBody.split("\n")
                    val compromisedPassMap = mutableMapOf<String, String>()

                    for (hash in hashes) {
                        val splitHash = hash.split(":")
                        compromisedPassMap[splitHash[0]] = splitHash[1]
                    }
                    if (postfix in compromisedPassMap.keys){
                        println("This password was compromised ${compromisedPassMap[postfix]} times")
                        return@withContext true
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return@withContext false
        }
    }


    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(password.toByteArray(Charsets.UTF_8))
        val sb = StringBuilder()
        for (b in result) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }
}