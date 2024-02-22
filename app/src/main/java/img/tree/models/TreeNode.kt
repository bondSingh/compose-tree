package img.tree.models

data class TreeNode(
    val label: String,
    val children: List<TreeNode> = emptyList()
)