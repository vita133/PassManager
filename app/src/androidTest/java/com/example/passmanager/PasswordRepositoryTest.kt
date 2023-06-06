

import com.example.passmanager.login.model.PasswordRepository
import com.example.passmanager.login.model.database.PasswordDao
import com.example.passmanager.login.model.entities.PasswordEntity
import com.example.passmanager.login.util.HashUtils
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PasswordRepositoryTest {

    @Mock
    private lateinit var passwordDao: PasswordDao

    private lateinit var passwordRepository: PasswordRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        passwordRepository = PasswordRepository(passwordDao)
    }

    @Test
    fun testInsertPassword() {
        runBlocking {
            val username = "testUser"
            val passname = "testPass"
            val password = "testPassword"

            passwordRepository.insertPassword(username, passname, password)

            verify(passwordDao).insert(any(PasswordEntity::class.java))
        }
    }

    @Test
    fun testGetPasswordByName() {
        runBlocking {
            val username = "testUser"
            val passname = "testPass"
            val expectedPasswordEntity = PasswordEntity(1, HashUtils.sha256Hash(username), passname, "testPassword")

            `when`(passwordDao.getPasswordByName(any(String::class.java), any(String::class.java)))
                .thenReturn(expectedPasswordEntity)

            val actualPasswordEntity = passwordRepository.getPasswordByName(username, passname)

            assertEquals(expectedPasswordEntity, actualPasswordEntity)
        }
    }

    @Test
    fun testGetAllPasswords() {
        runBlocking {
            val username = "testUser"
            val expectedPasswords = listOf(
                PasswordEntity(1, HashUtils.sha256Hash(username), "pass1", "password1"),
                PasswordEntity(2, HashUtils.sha256Hash(username), "pass2", "password2")
            )

            `when`(passwordDao.getAllPasswords(any(String::class.java))).thenReturn(expectedPasswords)

            val actualPasswords = passwordRepository.getAllPasswords(username)

            assertEquals(expectedPasswords, actualPasswords)
        }
    }
}
