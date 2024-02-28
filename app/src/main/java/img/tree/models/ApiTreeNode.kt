package img.tree.models

data class ApiTreeNode(
    val label: String = "",
    val children: List<ApiTreeNode>? = emptyList(),
    val id: String? = null
)


