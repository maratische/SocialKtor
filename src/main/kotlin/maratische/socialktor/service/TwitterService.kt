package maratische.socialktor.service

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import maratische.socialktor.Utils.Companion.EMAIL_QA_PIANO
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

const val EMAIL_REGEX = "([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})"
    const val ID_QA_PIANO = "164387421679303"
    const val FULL_NAME = "John Doe"

class TwitterService {

    var cache: LoadingCache<String, String> = loadingCache()

    private val random = Random()

    fun createToken(header: String): String {
        val matcher = Pattern.compile("oauth_callback=\"([^\"]+)\"").matcher(header)
        val token = getRandomToken(15)
        if (matcher.find()) {
            val url = URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8.name())
            cache.put(token, url)
            return "oauth_token=$token&oauth_token_secret=validRequestTokenSecret&oauth_callback_confirmed=true"
        }
        return "oauth_token=randomOAuthToken&oauth_token_secret=validRequestTokenSecret&oauth_callback_confirmed=true"
    }

    private fun getRandomToken(length: Int): String {
        val result = StringBuilder()
        result.append(random.nextInt(9) + 1)
        for (i in 0 until length) {
            result.append(random.nextInt(10))
        }
        return result.toString()
    }

    fun login(login: String, token: String): String {
        val oAuthVerifier = if (login.contains(Regex(EMAIL_REGEX))) {
            "validRequestTokenSecretForEmail$token"
        } else {
            "validRequestTokenSecretForUserName$token"
        }

        val callbackUrl = cache[token]
        cache.put(token, login)
        return "$callbackUrl&oauth_token=$token&oauth_verifier=$oAuthVerifier"
    }

    fun getAccessToken(header: String): String {
        val matcher = Pattern.compile("oauth_verifier=\"([^\"]+)\"").matcher(header)

        val accessToken =
        if (matcher.find() && matcher.group(1).contains("validRequestTokenSecretForEmail")) {
            val code = Regex("validRequestTokenSecretForEmail(.+)").find(matcher.group(1))?.groupValues?.get(1).toString()
            "validAccessTokenForEmail$code"
        } else {
            val code = Regex("validRequestTokenSecretForUserName(.+)").find(matcher.group(1))?.groupValues?.get(1).toString()
            "validAccessTokenForUserName$code"
        }
        return "oauth_token=$accessToken&oauth_token_secret=validAccessTokenSecret&user_id=0&x_auth_expires=0"
    }

    fun getInfo(header: String): Map<String, Any> {
        val matcher = Pattern.compile("oauth_token=\"([^\"]+)\"").matcher(header)

        if (matcher.find() && matcher.group(1).contains("validAccessTokenForUserName")) {
            val codeForName = Regex("validAccessTokenForUserName(.+)").find(matcher.group(1))?.groupValues?.get(1).toString()
            return mapOf<String, Any>(
                    "id_str" to if (cache[codeForName] == "qa") "938539767738582" else codeForName,
                    "name" to if (cache[codeForName] == "qa") FULL_NAME else FULL_NAME + codeForName.substring(0, 3)
            )
        }
        val code = Regex("validAccessTokenForEmail(.+)").find(matcher.group(1))?.groupValues?.get(1).toString()
        return mapOf<String, Any>(
                "id_str" to if (cache[code] == EMAIL_QA_PIANO) ID_QA_PIANO else code,
                "email" to cache[code],
                "name" to if (cache[code] == EMAIL_QA_PIANO) FULL_NAME else FULL_NAME + code.substring(0, 3)
        )
    }

    fun loadingCache(): LoadingCache<String, String> {
        return CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(
                object : CacheLoader<String, String>() {
                    override fun load(key: String): String {
                        return key.uppercase(Locale.getDefault())
                    }
                })
    }
}
