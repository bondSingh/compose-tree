package img.tree.repo

import img.tree.LOCAL_ID_MARKER
import img.tree.models.ApiEntryData
import img.tree.models.ApiTreeNode
import img.tree.models.TreeNode
import java.text.SimpleDateFormat
import java.util.LinkedList
import java.util.Locale
import java.util.Queue
import java.util.UUID

fun buildTree(nodes: List<TreeNode>, parentLevel: Int = 0): List<TreeNode> {
    return nodes.map { node ->
        val level = parentLevel + 1
        val children = node.children?.let { buildTree(it, level) }
        val childrenCount = getAllChildren(node)
        val nodeId = node.id
        node.copy(
            id = nodeId,
            children = children,
            level = level,
            offspringCount = childrenCount
        )
    }
}
private fun getAllChildren(node: TreeNode): Int {
    var childrenCount = 0
    val queue: Queue<TreeNode> = LinkedList()
    queue.add(node)
    while (!queue.isEmpty()) {
        val headNode = queue.poll()
        childrenCount += headNode?.children?.size ?: 0
        if (headNode != null) {
            headNode.children?.forEach { queue.add(it) }
        }
    }
    return childrenCount + 1
}

fun buildApiTree(nodes: List<ApiTreeNode>, parentLevel: Int = 0): List<TreeNode> {
    return nodes.map { node ->
        val level = parentLevel + 1
        val children = node.children?.let { buildApiTree(it, level) }
        val childrenCount = getApiAllChildren(node)
        val nodeId = node.id ?: (LOCAL_ID_MARKER + UUID.randomUUID().toString())
        TreeNode(
            id = nodeId,
            label = node.label,
            children = children,
            level = level,
            offspringCount = childrenCount
        )
    }
}

private fun getApiAllChildren(node: ApiTreeNode): Int {
    var childrenCount = 0
    val queue: Queue<ApiTreeNode> = LinkedList()
    queue.add(node)
    while (!queue.isEmpty()) {
        val headNode = queue.poll()
        childrenCount += headNode?.children?.size ?: 0
        if (headNode != null) {
            headNode.children?.forEach { queue.add(it) }
        }
    }
    return childrenCount + 1
}

fun transformEntryData(entryData: ApiEntryData): ApiEntryData {
        return entryData.copy(
            createdAt = formattedDate(entryData.createdAt),
            lastModifiedAt = formattedDate(entryData.lastModifiedAt)
        )
}

private fun formattedDate(serverDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = inputFormat.parse(serverDate)
    val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
    return  outputFormat.format(date)
}
