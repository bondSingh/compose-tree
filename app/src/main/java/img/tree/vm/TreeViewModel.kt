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
    private val _treeStateLiveData = MutableLiveData<TreeNode>()
    val treeState: LiveData<TreeNode> = _treeStateLiveData

    fun getValue(): LiveData<TreeNode> {
        return treeState
    }


    fun removeNode(nodeId: String) {
        viewModelScope.launch {
            _treeStateLiveData.value = repository.removeNode(treeNode = treeState.value, nodeId)
        }
    }


    fun fetchTreeData() {
        viewModelScope.launch {
            try {
                _treeStateLiveData.value = repository.fetchTreeData()
            } catch (noNet: NoConnectivityException) {
                Log.d(TAG, "No Internet")
            } catch (e: Exception) {
                if (e.message != null) {
                    Log.d(TAG, "Error ${e.message}")

                } else {
                    Log.d(TAG, "Something Went Wrong")
                }
            }
        }
    }
}
