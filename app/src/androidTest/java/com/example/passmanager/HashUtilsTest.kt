package com.example.passmanager
import com.example.passmanager.login.util.HashUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class HashUtilsTest {
    @Test
    fun testSha256Hash() {
        val input = "password123"
        val expectedHash = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f"

        val hashedInput = HashUtils.sha256Hash(input)

        assertEquals(expectedHash, hashedInput)
    }

    @Test
    fun testSha256Hash_WithEmptyInput() {
        val input = ""
        val expectedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"

        val hashedInput = HashUtils.sha256Hash(input)

        assertEquals(expectedHash, hashedInput)
    }

    @Test
    fun testGenerateSalt() {
        val salt1 = HashUtils.generateSalt()
        val salt2 = HashUtils.generateSalt()

        assertNotEquals(salt1, salt2)
    }
}