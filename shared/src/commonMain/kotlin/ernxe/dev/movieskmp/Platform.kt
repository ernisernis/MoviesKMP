package ernxe.dev.movieskmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform