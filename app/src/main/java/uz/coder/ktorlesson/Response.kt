package uz.coder.ktorlesson

import kotlinx.serialization.Serializable

@Serializable
data class Response(
	val id: Int? = null,
	val title: String? = null,
	val body: String? = null,
	val userId: Int? = null
)
