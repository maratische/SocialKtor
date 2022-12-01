package maratische.socialktor

import java.math.BigInteger
import java.security.MessageDigest

class Utils {
    companion object {
        const val ID_QA_PIANO = "164387421679303"
        const val EMAIL_QA_PIANO = "qa@piano.io"
        const val FULL_NAME = "John Doe"
        const val FAIL_EMAIL = "fail@piano.io"
        const val FAIL_PASSWORD = "fail"

        const val privateKey = "o4lmBhPxFNj580MAqXr9cMHbyQ5MbOJuVx4nsphq";

        fun md5(text: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(text.toByteArray())).toString(16).padStart(32, '0')
        }

        fun checkCredentials(login: String?, password: String?) {
            if (FAIL_EMAIL == login && FAIL_PASSWORD == password) {
                throw RuntimeException("The user with this email address and password was not found")
            }
        }
    }
}
