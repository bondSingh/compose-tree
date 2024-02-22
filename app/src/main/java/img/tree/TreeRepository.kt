package img.tree

import img.tree.models.TreeNode
import img.tree.network.TreeAPIService
import retrofit2.HttpException
import java.io.IOException

class TreeRepository(private val treeAPIService: TreeAPIService) {
    suspend fun fetchTreeData(): List<TreeNode> {
        return try {
            treeAPIService.fetchTreeData()
        } catch (e: IOException) {
            // Network error
            emptyList()
        } catch (e: HttpException) {
            // HTTP error (e.g., 404)
            emptyList()
        }
    }
}
