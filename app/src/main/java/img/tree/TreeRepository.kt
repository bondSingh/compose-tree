package img.tree

import img.tree.models.TreeNode
import img.tree.network.TreeAPIService
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

class TreeRepositoryImpl(private val treeAPIService: TreeAPIService) : TreeRepository{
    private var _treeData = mutableListOf<TreeNode>()

    override suspend fun fetchTreeData(): List<TreeNode> {
         try {
            if (_treeData.isEmpty()){
                _treeData =  buildTree(treeAPIService.fetchTreeData()).toMutableList()
            }
        } catch (e: IOException) {
            // Network error
            emptyList<TreeNode>()
        } catch (e: HttpException) {
            // HTTP error (e.g., 404)
            emptyList<TreeNode>()
        }
        return _treeData
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

    override suspend fun removeNode(nodeId: String) {
        val nodeToRemove = findNodeById(nodeId, _treeData)
        if (nodeToRemove != null) {
            _treeData.remove(nodeToRemove)
        }
    }

    private fun findNodeById(nodeId: String, nodes: List<TreeNode>): TreeNode? {
        val children = nodes
        for (node in nodes) {
            if (node.id == nodeId) {
                return node
            }
            val foundNode = node.children?.let { findNodeById(nodeId, it) }
            if (foundNode != null) {
                return foundNode
            }
        }
        return null
    }
}

interface TreeRepository {
    suspend fun fetchTreeData(): List<TreeNode>
    suspend fun removeNode(nodeId: String)
}