import io.undertow.Undertow
import io.undertow.server.HttpHandler
import io.undertow.server.HttpServerExchange
import java.io.Closeable

typealias EndpointHandler = () -> String

class Router {
    val paths = mutableMapOf<String, EndpointHandler>()

    fun path(path: String, handler: () -> String) {
        paths[path] = handler
    }
}

class KobwebSetup {
    val router: Router = Router()

    fun router(routerSetup: Router.() -> Unit) {
        router.routerSetup()
    }
}

class Kobweb(
    val host: String = "localhost",
    val port: Int = 8080,
    setup: KobwebSetup.() -> Unit
) : Closeable {
    val undertow: Undertow = Undertow.builder()
        .addHttpListener(port, host)
        .setHandler(KobwebHandler(setup))
        .build()

    fun start() {
        undertow.start()
    }

    fun stop() {
        undertow.stop()
    }

    override fun close() = stop()

    class KobwebHandler(setup: KobwebSetup.() -> Unit) : HttpHandler {
        private val routes: Map<String, EndpointHandler>

        init {
            val kobwebSetup = KobwebSetup()
            kobwebSetup.setup()
            routes = kobwebSetup.router.paths
        }

        override fun handleRequest(exchange: HttpServerExchange) {
            routes[exchange.requestPath]?.invoke()?.let {
                exchange.statusCode = 200
                exchange.responseSender.send(it)
            } ?: run {
                exchange.statusCode = 404
                exchange.responseSender.send("Not Found")
            }
        }
    }
}