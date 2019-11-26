package com.mohamedeleish.tagged.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Tagged(val customValName: String = "", val customTag: String = "")