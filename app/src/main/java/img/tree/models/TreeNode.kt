package img.tree.models

data class TreeNode(
    val label: String = "",
    val children: List<TreeNode>? = emptyList(),
    val id: String? = null,
    val level: Int = 0,
    val offspringCount: Int = 0
)


