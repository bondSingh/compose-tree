package img.tree

import android.util.Log
import img.tree.models.TreeNode
import img.tree.network.TreeAPIService
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

class TreeRepositoryImpl(private val treeAPIService: TreeAPIService) : TreeRepository{
    private var _treeData : TreeNode? = null

    override suspend fun fetchTreeData(): TreeNode? {
         try {
             if(_treeData == null){
                 var response = treeAPIService.fetchTreeData()
                 var treeRes = buildTree(response)
                 _treeData = TreeNode("rootNode", treeRes, "sadhkaj",0,0)
             }

             //_treeData =  buildTree(tree)
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
    }/*
    private fun buildTree(nodes: TreeNode, parentLevel: Int = 0): TreeNode {
        for (node in nodes.children!!){
            node.let {
                val level = parentLevel + 1
                val children = buildTree(it, level).children
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
        return nodes
    }*/

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

    override suspend fun removeNode(treeNode: TreeNode?,nodeId: String) : TreeNode? {
            if (treeNode == null) {
                return null
            }
            if (treeNode.id == nodeId) {
                Log.d(TAG, "Deleting Item : $treeNode")
                return null // Deleting the current node
            }

        val updatedChildren = treeNode.children?.mapNotNull { removeNode(it, nodeId) }
        return treeNode.id?.let { TreeNode(treeNode.label, updatedChildren, treeNode.id,treeNode.level, treeNode.offspringCount) }
    }


}


interface TreeRepository {
    suspend fun fetchTreeData(): TreeNode?
    suspend fun removeNode(treeNode: TreeNode?, nodeId: String): TreeNode?
}