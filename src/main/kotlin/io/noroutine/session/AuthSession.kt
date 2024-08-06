package io.noroutine.session

data class AuthSession(val accessToken: String) {
    companion object {
        const val SESSION_COOKIE = "AUTH"
    }
}