package ecke.smstorest.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class RestMessage(var text: String)