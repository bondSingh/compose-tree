package img.tree.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import img.tree.TAG
import img.tree.TreeRepository
import img.tree.models.TreeNode
import img.tree.network.NoConnectivityException
import kotlinx.coroutines.launch

class TreeViewModel(private val repository: TreeRepository) : ViewModel() {
    private val _treeStateLiveData = MutableLiveData<List<TreeNode>>()
    val treeState: LiveData<List<TreeNode>> = _treeStateLiveData

    fun getValue() :LiveData<List<TreeNode>>{
        return treeState
    }

    private fun buildTree(nodes: List<TreeNode>, parentLevel: Int = 0): List<TreeNode> {
        return nodes.map { node ->
            val level = parentLevel + 1
            val children = node.children?.let { buildTree(it, level) }
            val childrenCount = getAllChildren(node)
            node.copy(children = children, level = level, offspringCount = childrenCount)
        }
    }

    private fun getAllChildren(node: TreeNode): Int {
        var childrenCount = 0
        // Base case: If the node has no children, return 0
        if (node.children != null){
            if (node.children.isEmpty()) {
                return 0
            }

            // Recursive case: Count the number of children of each child node
            // and sum them up
            childrenCount = node.children.sumOf { getAllChildren(it) }
            childrenCount += node.children.size
        }


        // Add 1 to include the direct children of the current node
        return childrenCount+1
    }

    fun fetchTreeData() {
        viewModelScope.launch {
            try {
                val tree = repository.fetchTreeData()
                //_treeStateLiveData.value = tree
                _treeStateLiveData.value = buildTree(tree, 0)
            } catch (noNet : NoConnectivityException){
                    Log.d(TAG, "No Internet")
            }
            /*catch (e: Exception) {
                if (e.message != null) {
                    Log.d(TAG, "Error ${e.message}")

                } else {
                    Log.d(TAG, "Something Went Wrong")
                }
            }*/
        }
    }
}
