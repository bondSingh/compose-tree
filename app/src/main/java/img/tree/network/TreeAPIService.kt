package img.tree.network

import img.tree.models.ApiEntryData
import img.tree.models.ApiTreeNode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TreeAPIService {
    @GET("data.json")
    suspend fun fetchTreeData(): List<ApiTreeNode>

    @GET("entries/{id}.json")
    suspend fun fetchEntryData(@Path("id") id: String): Response<ApiEntryData>
}