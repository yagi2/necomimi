package app.yagi2.necomimi.ext

fun CharSequence?.orEmpty(): CharSequence {
    return this ?: ""
}