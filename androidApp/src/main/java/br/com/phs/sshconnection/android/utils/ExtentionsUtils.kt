package br.com.phs.sshconnection.android.utils

inline fun <T, R> T.letOr(block: (T) -> R?, isNull: ()-> R?): R? {
    return if (this == null) {
        isNull.invoke()
    } else {
        block(this)
    }
}