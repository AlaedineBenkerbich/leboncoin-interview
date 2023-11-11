package fr.leboncoin.albumreader.api

import fr.leboncoin.albumreader.common.testingtools.Classpath
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * Unit tests of [Api].
 */
class ApiTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val api: Api by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("static.leboncoin.fr/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Test
    fun `should invoke the API with GET method`() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(Classpath.resourceText("get-pictures-200-complete.json"))
            )

            // When
            api.getPictures()

            // Then
            val request = mockWebServer.takeRequest()
            val requestUrl = request.requestUrl
            assertEquals("GET", request.method)
            assertEquals(
                listOf("static.leboncoin.fr", "img", "shared", "technical-test.json"),
                requestUrl?.pathSegments
            )
            assertEquals(0, request.requestUrl?.queryParameterNames?.size)
        }

    @Test
    fun `should fail when the API responds NOT_FOUND (404)`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(Classpath.resourceText("404.json"))
        )

        // When
        val exception = assertFailsWith<HttpException> {
            api.getPictures()
        }

        // Then
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, exception.code())
        assertNotNull(exception.response()?.errorBody()?.string()) { errorBodyStr ->
            assertContains(errorBodyStr, "The requested URL was not found on the server")
        }
    }

    @Test
    fun `should return success response with empty picture list when the API responds OK (200) without pictures`() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(Classpath.resourceText("get-pictures-200-without-pictures.json"))
            )

            // When
            val result = api.getPictures()

            // Then
            assertEquals(
                emptyList(),
                result
            )
        }

    @Test
    fun `should return success response with picture list when the API responds OK (200) with pictures`() =
        runTest {
            // Given
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(Classpath.resourceText("get-pictures-200-complete.json"))
            )

            // When
            val result = api.getPictures()

            // Then
            assertEquals(
                listOf(
                    RemotePicture(
                        albumId = 1,
                        id = 1,
                        title = "accusamus beatae ad facilis cum similique qui sunt",
                        url = "https://via.placeholder.com/600/92c952",
                        thumbnailUrl = "https://via.placeholder.com/150/92c952"
                    ),
                    RemotePicture(
                        albumId = 1,
                        id = 2,
                        title = "reprehenderit est deserunt velit ipsam",
                        url = "https://via.placeholder.com/600/771796",
                        thumbnailUrl = "https://via.placeholder.com/150/771796"
                    ),
                    RemotePicture(
                        albumId = 1,
                        id = 3,
                        title = "officia porro iure quia iusto qui ipsa ut modi",
                        url = "https://via.placeholder.com/600/24f355",
                        thumbnailUrl = "https://via.placeholder.com/150/24f355"
                    ),
                    RemotePicture(
                        albumId = 2,
                        id = 51,
                        title = "non sunt voluptatem placeat consequuntur rem incidunt",
                        url = "https://via.placeholder.com/600/8e973b",
                        thumbnailUrl = "https://via.placeholder.com/150/8e973b"
                    ),
                    RemotePicture(
                        albumId = 2,
                        id = 52,
                        title = "eveniet pariatur quia nobis reiciendis laboriosam ea",
                        url = "https://via.placeholder.com/600/121fa4",
                        thumbnailUrl = "https://via.placeholder.com/150/121fa4"
                    ),
                    RemotePicture(
                        albumId = 3,
                        id = 113,
                        title = "hic nulla consectetur",
                        url = "https://via.placeholder.com/600/1dff02",
                        thumbnailUrl = "https://via.placeholder.com/150/1dff02"
                    ),
                    RemotePicture(
                        albumId = 3,
                        id = 114,
                        title = "consequatur quaerat sunt et",
                        url = "https://via.placeholder.com/600/e79b4e",
                        thumbnailUrl = "https://via.placeholder.com/150/e79b4e"
                    ),
                    RemotePicture(
                        albumId = 3,
                        id = 115,
                        title = "unde minus molestias",
                        url = "https://via.placeholder.com/600/da7ddf",
                        thumbnailUrl = "https://via.placeholder.com/150/da7ddf"
                    ),
                    RemotePicture(
                        albumId = 3,
                        id = 116,
                        title = "et iure eius enim explicabo",
                        url = "https://via.placeholder.com/600/aac33b",
                        thumbnailUrl = "https://via.placeholder.com/150/aac33b"
                    ),
                    RemotePicture(
                        albumId = 3,
                        id = 117,
                        title = "dolore quo nemo omnis odio et iure explicabo",
                        url = "https://via.placeholder.com/600/b2fe8",
                        thumbnailUrl = "https://via.placeholder.com/150/b2fe8"
                    )
                ),
                result
            )
        }
}