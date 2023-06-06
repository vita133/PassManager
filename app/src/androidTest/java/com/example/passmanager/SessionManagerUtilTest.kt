import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.passmanager.login.util.HashUtils
import com.example.passmanager.login.util.SessionManagerUtil
import junit.framework.TestCase.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class SessionManagerUtilTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        sharedPreferences = context.getSharedPreferences(SessionManagerUtil.SESSION_PREFERENCES, Context.MODE_PRIVATE)
    }

    @After
    fun tearDown() {
        sharedPreferences.edit().clear().apply()
    }
    @Test
    fun startUserSession_shouldStoreExpiryTimeInPreferences() {
        val expiresIn = 3600
        val expectedExpiryTime = Calendar.getInstance().apply {
            add(Calendar.SECOND, expiresIn)
        }.time

        SessionManagerUtil.startUserSession(context, expiresIn)

        val storedExpiryTimeMillis = sharedPreferences.getLong(SessionManagerUtil.SESSION_EXPIRY_TIME, 0)
        val storedExpiryTime = Date(storedExpiryTimeMillis)
        val toleranceMillis = 100
        assertTrue(Math.abs(expectedExpiryTime.time - storedExpiryTime.time) <= toleranceMillis)
    }

    @Test
    fun isSessionActive_withActiveSession_shouldReturnTrue() {
        val currentTime = Date()
        val expiresIn = 3600
        val expiryTime = Calendar.getInstance().apply {
            add(Calendar.SECOND, expiresIn)
        }.time
        sharedPreferences.edit().putLong(SessionManagerUtil.SESSION_EXPIRY_TIME, expiryTime.time).apply()

        val isSessionActive = SessionManagerUtil.isSessionActive(currentTime, context)

        assertTrue(isSessionActive)
    }

    @Test
    fun isSessionActive_withExpiredSession_shouldReturnFalse() {
        val currentTime = Date()
        val expiresIn = -3600
        val expiryTime = Calendar.getInstance().apply {
            add(Calendar.SECOND, expiresIn)
        }.time
        sharedPreferences.edit().putLong(SessionManagerUtil.SESSION_EXPIRY_TIME, expiryTime.time).apply()

        val isSessionActive = SessionManagerUtil.isSessionActive(currentTime, context)

        assertFalse(isSessionActive)
    }

    @Test
    fun storeUserToken_shouldStoreTokenInPreferences() {
        val token = "example_token"
        val hashedToken = HashUtils.sha256Hash(token)
        val tokenEditor = context.getSharedPreferences(SessionManagerUtil.SESSION_PREFERENCES, 0).edit()
        tokenEditor.putString(SessionManagerUtil.SESSION_TOKEN, hashedToken)

        SessionManagerUtil.storeUserToken(context, token)

        val storedToken = sharedPreferences.getString(SessionManagerUtil.SESSION_TOKEN, null)
        assertEquals(hashedToken, storedToken)
    }

    @Test
    fun getUserToken_withStoredToken_shouldReturnToken() {
        val token = "example_token"
        sharedPreferences.edit().putString(SessionManagerUtil.SESSION_TOKEN, token).apply()

        val storedToken = SessionManagerUtil.getUserToken(context)

        assertEquals(token, storedToken)
    }

    @Test
    fun getUserToken_withNoStoredToken_shouldReturnNull() {
        val storedToken = SessionManagerUtil.getUserToken(context)

        assertTrue(storedToken.isNullOrEmpty())
    }

    @Test
    fun endUserSession_shouldClearStoredData() {
        sharedPreferences.edit().putString(SessionManagerUtil.SESSION_TOKEN, "example_token").apply()

        SessionManagerUtil.endUserSession(context)

        val storedToken = sharedPreferences.getString(SessionManagerUtil.SESSION_TOKEN, null)
        assertEquals(null, storedToken)
    }
}
