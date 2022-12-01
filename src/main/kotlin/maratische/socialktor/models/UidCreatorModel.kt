package maratische.socialktor.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable()
data class ExternalHttpUidRequest constructor(
    val reason: ExternalHttpUidRequestReason,
    val email: String,
    val uid: String?,
    @SerialName("first_name") val firstName: String?,//@JsonProperty("first_name")
    @SerialName("last_name") val lastName: String?,//@JsonProperty("last_name")
    @SerialName("social_linking") val socialLinking: SocialLinkingUIdRequest?
) {//@JsonProperty("social_linking")
}

@Serializable
data class SocialLinkingUIdRequest constructor(
    val type: String,
    val id: String
) {
}

@Serializable
data class ResponseHttpUidRequest constructor(
    val uid: String,
    val registration: Boolean?,
    val passwordless: Boolean?
) {
}

enum class ExternalHttpUidRequestReason {
    SWG, GRANT_ACCESS, CREATE_USER, PASSWORDLESS, FRICTIONLESS
}
