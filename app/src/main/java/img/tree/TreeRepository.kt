package img.tree

import android.util.Log
import img.tree.models.TreeNode
import img.tree.network.ERROR_TYPE.*
import img.tree.network.NoConnectivityException
import img.tree.network.Resource
import img.tree.network.TreeAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

class TreeRepositoryImpl(private val treeAPIService: TreeAPIService) : TreeRepository {
    private var serverResponse: Resource<TreeNode?>? = null

    override suspend fun fetchTreeData(): Resource<TreeNode?>? = withContext(Dispatchers.IO){
        try {
            if (serverResponse == null ) {
                val response = treeAPIService.fetchTreeData()
                val treeRes = buildTree(response)
                serverResponse = Resource.success(TreeNode("rootNode", treeRes, UUID.randomUUID().toString(), 0, 0))
            }
        } catch (noNet: NoConnectivityException) {
            serverResponse = Resource.error(errorType = NO_NETWORK, data = null)
        } catch (e: IOException) {
            serverResponse = Resource.error(errorType = SERVER_ERROR, data = null)
        } catch (e: HttpException) {
            serverResponse = Resource.error(errorType = SERVER_ERROR, data = null)
        }
        return@withContext serverResponse
    }

    private fun buildTree(nodes: List<TreeNode>, parentLevel: Int = 0): List<TreeNode> {
        return nodes.map { node ->
            val level = parentLevel + 1
            val children = node.children?.let { buildTree(it, level) }
            val childrenCount = getAllChildren(node)
            val nodeId = node.id ?: UUID.randomUUID()
            node.copy(
                id = nodeId.toString(),
                children = children,
                level = level,
                offspringCount = childrenCount
            )
        }
    }

    private fun getAllChildren(node: TreeNode): Int {
        var childrenCount = 0
        // Base case: If the node has no children, return 0
        if (node.children != null) {
            if (node.children.isEmpty()) {
                return 0
            }

            // Recursive case: Count the number of children of each child node
            // and sum them up
            childrenCount = node.children.sumOf { getAllChildren(it) }
            childrenCount += node.children.size
        }

        // Add 1 to include the direct children of the current node
        return childrenCount + 1
    }

    override suspend fun removeNode(treeNode: TreeNode?, nodeId: String): TreeNode? {
        if (treeNode == null) {
            return null
        }
        if (treeNode.id == nodeId) {
            Log.d(TAG, "Deleting Item : $treeNode")
            return null // Deleting the current node
        }

        var updatedChildren = treeNode.children?.mapNotNull { removeNode(it, nodeId) }
        updatedChildren = updatedChildren?.let { buildTree(it) }
        return treeNode.id?.let {
            TreeNode(
                treeNode.label,
                updatedChildren,
                treeNode.id,
                treeNode.level,
                treeNode.offspringCount
            )
        }
    }
}


interface TreeRepository {
    suspend fun fetchTreeData(): Resource<TreeNode?>?
    suspend fun removeNode(treeNode: TreeNode?, nodeId: String): TreeNode?
}