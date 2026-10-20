package fr.amonteiro.a10_legrand_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform