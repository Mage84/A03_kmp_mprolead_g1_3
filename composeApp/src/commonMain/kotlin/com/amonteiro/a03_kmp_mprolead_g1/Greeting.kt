package com.amonteiro.a03_kmp_mprolead_g1

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}