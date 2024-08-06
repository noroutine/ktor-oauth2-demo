package io.noroutine.session

data class UserSession(val count: Int = 0) {
    companion object {
        const val SESSION_COOKIE = "USER"
    }
}
