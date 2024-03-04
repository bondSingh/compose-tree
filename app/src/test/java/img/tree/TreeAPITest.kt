package img.tree

import img.tree.network.TreeAPIService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import okhttp3.mockwebserver.MockWebServer

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TreeAPITest {

    lateinit var mockServer : MockWebServer
    lateinit var treeAPIService: TreeAPIService

    @Before
    fun setup(){
    mockServer = MockWebServer()
        treeAPIService =
            Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TreeAPIService::class.java)
    }

    @After
    fun tearDown(){

    }

    @Test
    fun testFetchTree_empty_response() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockServer.enqueue(mockResponse)
        val response = treeAPIService.fetchTreeData()
        mockServer.takeRequest()
        assertEquals(true, response.body()?.isEmpty()  )
    }

    @Test
    fun testFetchTree_valid_response() = runTest{
        val mockResponse = MockResponse()
        val content = FileReader.readFileResource("/valid_tree_response.json")
        mockResponse.setBody(content)
        mockResponse.setResponseCode(200)
        mockServer.enqueue(mockResponse)
        val response = treeAPIService.fetchTreeData()
        mockServer.takeRequest()
        assertEquals(false, response.body()?.isEmpty()  )
        assertEquals(2, response.body()?.size  )
    }

    @Test
    fun testFetchLog_empty_response() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("{}")
        mockServer.enqueue(mockResponse)
        val response = treeAPIService.fetchEntryData("any_ID")
        mockServer.takeRequest()
        assertEquals("ApiEntryData(id=null, createdAt=null, createdBy=null, lastModifiedAt=null, lastModifiedBy=null, description=null)", response.body().toString()  )
    }

    @Test
    fun testFetchLog_valid_response() = runTest{
        val mockResponse = MockResponse()
        val content = FileReader.readFileResource("/valid_log_response.json")
        mockResponse.setBody(content)
        mockResponse.setResponseCode(200)
        mockServer.enqueue(mockResponse)
        val response = treeAPIService.fetchEntryData("test")
        mockServer.takeRequest()
        assertEquals("2047-04-16T08:07:55.327Z", response.body()?.createdAt  )
    }

    @Test
    fun testFetchLog_404() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("")
        mockResponse.setResponseCode(404)
        mockServer.enqueue(mockResponse)
        val response = treeAPIService.fetchEntryData("test")
        mockServer.takeRequest()
        assertEquals(null, response.body()  )
    }
}