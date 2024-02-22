package img.tree.models

/*data class TreeNode(
    val label: String,
    val children: List<TreeNode> = emptyList()
)*/

data class TreeNode(
    val label: String,
    val children: List<TreeNode> = emptyList(),
    val id: String? = null // Assuming id is present in leaf nodes
)


