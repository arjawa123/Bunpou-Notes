package com.rjw.bunpoun3.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val expiresAtMillis: Long,
    val userId: String,
    val email: String,
    val authProvider: String,
)

sealed interface SignUpResult {
    data class SignedIn(val session: AuthSession) : SignUpResult
    data object ConfirmationRequired : SignUpResult
    data object AlreadyRegistered : SignUpResult
}

class SupabaseAuthRepository(
    private val supabaseUrl: String,
    private val anonKey: String,
) {
    private val callbackUrl = "com.rjw.bunpoun3://login-callback"
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    val isConfigured: Boolean
        get() = supabaseUrl.isNotBlank() && anonKey.isNotBlank()

    suspend fun signIn(email: String, password: String): AuthSession =
        authRequest(
            endpoint = "/auth/v1/token?grant_type=password",
            body = credentialPayload(email, password),
        ).requireSession(
            fallbackEmail = email,
            fallbackProvider = "email",
        )

    suspend fun signUp(email: String, password: String): SignUpResult =
        authRequest(
            endpoint = "/auth/v1/signup?redirect_to=${callbackUrl.encoded()}",
            body = credentialPayload(email, password),
        ).toSignUpResult(email)

    suspend fun signInWithGoogleIdToken(
        idToken: String,
        nonce: String,
    ): AuthSession =
        authRequest(
            endpoint = "/auth/v1/token?grant_type=id_token",
            body = googleIdTokenPayload(idToken, nonce),
        ).requireSession(
            fallbackEmail = "",
            fallbackProvider = "google",
        )

    suspend fun sendPasswordReset(email: String) {
        request(
            endpoint = "/auth/v1/recover?redirect_to=${callbackUrl.encoded()}",
            method = "POST",
            body = json.encodeToString(JsonObject.serializer(), emailPayload(email)),
            bearerToken = anonKey,
        )
    }

    suspend fun enrichSession(session: AuthSession): AuthSession {
        val user = request(
            endpoint = "/auth/v1/user",
            method = "GET",
            body = null,
            bearerToken = session.accessToken,
        ).let { response -> json.decodeFromString(AuthUserResponse.serializer(), response) }

        return session.copy(
            userId = user.id ?: session.userId,
            email = user.email ?: session.email,
            authProvider = user.provider ?: session.authProvider,
        )
    }

    suspend fun updatePassword(accessToken: String, password: String) {
        request(
            endpoint = "/auth/v1/user",
            method = "PUT",
            body = json.encodeToString(JsonObject.serializer(), passwordPayload(password)),
            bearerToken = accessToken,
        )
    }

    suspend fun signOut(accessToken: String) {
        request(
            endpoint = "/auth/v1/logout",
            method = "POST",
            body = null,
            bearerToken = accessToken,
        )
    }

    private suspend fun authRequest(
        endpoint: String,
        body: JsonObject,
    ): AuthResponse =
        request(
            endpoint = endpoint,
            method = "POST",
            body = json.encodeToString(JsonObject.serializer(), body),
            bearerToken = anonKey,
        ).let { response -> json.decodeFromString(AuthResponse.serializer(), response) }

    private suspend fun request(
        endpoint: String,
        method: String,
        body: String?,
        bearerToken: String,
    ): String = withContext(Dispatchers.IO) {
        check(isConfigured) { "Supabase belum dikonfigurasi." }
        val baseUrl = supabaseUrl.trimEnd('/')
        val connection = (URL("$baseUrl$endpoint").openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 15_000
            readTimeout = 20_000
            setRequestProperty("apikey", anonKey)
            setRequestProperty("Authorization", "Bearer $bearerToken")
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            doInput = true
            if (body != null) {
                doOutput = true
            }
        }

        try {
            if (body != null) {
                OutputStreamWriter(connection.outputStream).use { writer -> writer.write(body) }
            }
            val response = connection.responseText()
            if (connection.responseCode !in 200..299) {
                throw IllegalStateException(response.toSupabaseMessage())
            }
            response
        } finally {
            connection.disconnect()
        }
    }

    private fun HttpURLConnection.responseText(): String {
        val stream = if (responseCode in 200..299) inputStream else errorStream
        return stream?.bufferedReader()?.use { reader -> reader.readText() }.orEmpty()
    }

    private fun credentialPayload(email: String, password: String): JsonObject =
        buildJsonObject {
            put("email", email.trim())
            put("password", password)
        }

    private fun googleIdTokenPayload(idToken: String, nonce: String): JsonObject =
        buildJsonObject {
            put("provider", "google")
            put("id_token", idToken)
            put("nonce", nonce)
        }

    private fun emailPayload(email: String): JsonObject =
        buildJsonObject {
            put("email", email.trim())
        }

    private fun passwordPayload(password: String): JsonObject =
        buildJsonObject {
            put("password", password)
        }

    private fun String.toSupabaseMessage(): String =
        runCatching {
            val obj = json.decodeFromString(JsonObject.serializer(), this)
            obj["msg"]?.jsonPrimitive?.content
                ?: obj["message"]?.jsonPrimitive?.content
                ?: obj["error_description"]?.jsonPrimitive?.content
                ?: obj["error"]?.jsonPrimitive?.content
        }.getOrNull()
            ?: "Tidak bisa terhubung ke Supabase."

    private fun String.encoded(): String =
        URLEncoder.encode(this, "UTF-8")
}

@Serializable
private data class AuthResponse(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("expires_in") val expiresIn: Long? = null,
    val user: AuthUserResponse? = null,
)

@Serializable
private data class AuthUserResponse(
    val id: String? = null,
    val email: String? = null,
    @SerialName("app_metadata") val appMetadata: AuthAppMetadata? = null,
    val identities: List<AuthIdentityResponse>? = null,
)

@Serializable
private data class AuthAppMetadata(
    val provider: String? = null,
)

@Serializable
private data class AuthIdentityResponse(
    val provider: String? = null,
)

private val AuthUserResponse.provider: String?
    get() = appMetadata?.provider ?: identities?.firstOrNull { !it.provider.isNullOrBlank() }?.provider

private fun AuthResponse.requireSession(
    fallbackEmail: String,
    fallbackProvider: String,
): AuthSession =
    toSession(
        fallbackEmail = fallbackEmail,
        fallbackProvider = fallbackProvider,
    )
        ?: throw IllegalStateException("Login berhasil, tapi session Supabase tidak dikembalikan.")

private fun AuthResponse.toSession(
    fallbackEmail: String,
    fallbackProvider: String,
): AuthSession? {
    val token = accessToken ?: return null
    val refresh = refreshToken.orEmpty()
    val expiresInMillis = (expiresIn ?: 3_600L) * 1_000L
    return AuthSession(
        accessToken = token,
        refreshToken = refresh,
        expiresAtMillis = System.currentTimeMillis() + expiresInMillis,
        userId = user?.id.orEmpty(),
        email = user?.email ?: fallbackEmail.trim(),
        authProvider = user?.provider ?: fallbackProvider,
    )
}

private fun AuthResponse.toSignUpResult(fallbackEmail: String): SignUpResult {
    val session = toSession(
        fallbackEmail = fallbackEmail,
        fallbackProvider = "email",
    )
    if (session != null) {
        return SignUpResult.SignedIn(session)
    }

    if (user.isExistingUserPlaceholder()) {
        return SignUpResult.AlreadyRegistered
    }

    return SignUpResult.ConfirmationRequired
}

private fun AuthUserResponse?.isExistingUserPlaceholder(): Boolean {
    if (this == null) return false
    val emailIdentityMissing = identities?.none { it.provider.equals("email", ignoreCase = true) } != false
    return !id.isNullOrBlank() && emailIdentityMissing
}
