package br.com.phs.sshconn.shared.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SSHEntity(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("address")
    val address: String,
    @SerialName("port")
    val port: Int = 22,
    @SerialName("user_name")
    val userName: String,
    @SerialName("pw")
    val pw: String
)