package com.amonteiro.a03_kmp_mprolead_g1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform