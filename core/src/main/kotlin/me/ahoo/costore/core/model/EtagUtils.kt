package me.ahoo.costore.core.model

fun String?.normalizeEtag(): String? = this?.let {
    if (it.startsWith("\"")) it else "\"$it\""
}
