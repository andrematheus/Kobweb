import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HelloWorldWebappTest {
    private val kobwebApp = Kobweb {
        router {
            path("/") {
                "Hello, world!"
            }
        }
    }

    @Test
    internal fun `should display hello world at root`() {
        kobwebApp.withAppRunning {
            val client = kobwebApp.client
            val response = client.get("/")
            assertEquals("Hello, world!", response.textContents)
        }
    }

    @Test
    internal fun `should return 404 for non existing path`() {
        kobwebApp.withAppRunning {
            val client = kobwebApp.client
            val response = client.get("/non-existing")
            assertEquals(404, response.statusCode)
        }
    }
}