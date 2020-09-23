import io.github.rybalkinsd.kohttp.ext.httpGet

fun Kobweb.withAppRunning(body: () -> Unit) {
    this.also { start() }.use {
        body()
    }
}

class Client(val kobweb: Kobweb) {
    fun get(path: String): Response {
        val response = "http://${kobweb.host}:${kobweb.port}${path}".httpGet()
        return Response(response.code(), response.body()?.string())
    }

    data class Response(val statusCode: Int, val textContents: String?)
}

val Kobweb.client: Client
    get() = Client(this)