package img.tree.network

import img.tree.models.TreeNode
import retrofit2.http.GET

interface TreeService {
    @GET("data.json")
    suspend fun fetchTreeData(): List<TreeNode>
}