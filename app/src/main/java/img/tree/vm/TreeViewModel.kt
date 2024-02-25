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
import java.util.UUID

class TreeViewModel(private val repository: TreeRepository) : ViewModel() {
    private val _treeStateLiveData = MutableLiveData<List<TreeNode>>()
    val treeState: LiveData<List<TreeNode>> = _treeStateLiveData

    fun getValue(): LiveData<List<TreeNode>> {
        return treeState
    }





    fun removeNode(nodeId: String) {
        viewModelScope.launch {
            repository.removeNode(nodeId)
            _treeStateLiveData.value = repository.fetchTreeData()
        }
    }



    fun fetchTreeData() {
        viewModelScope.launch {
            try {
                val tree = repository.fetchTreeData()
                //_treeStateLiveData.value = tree
                _treeStateLiveData.value = repository.fetchTreeData()
            } catch (noNet: NoConnectivityException) {
                Log.d(TAG, "No Internet")
            }
            catch (e: Exception) {
                if (e.message != null) {
                    Log.d(TAG, "Error ${e.message}")

                } else {
                    Log.d(TAG, "Something Went Wrong")
                }
            }
        }
    }
}
