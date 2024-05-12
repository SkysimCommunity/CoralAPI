package sh.lumin.coral.utils

fun String.removeFormatting() = this.replace("[\u00a7&][0-9a-fk-or]".toRegex(), "")