package img.tree.network

import img.tree.models.ApiTreeNode
import retrofit2.http.GET

interface TreeAPIService {
    @GET("data.json")
    suspend fun fetchTreeData(): List<ApiTreeNode>
}