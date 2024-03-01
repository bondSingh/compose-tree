package img.tree.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import img.tree.models.ApiEntryData
import img.tree.models.TreeNode
import img.tree.network.ERROR_TYPE
import img.tree.network.Resource
import img.tree.repo.TreeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreeViewModel @Inject constructor(private val repository: TreeRepository) : ViewModel() {
    private val _treeStateLiveData = MutableLiveData<Resource<TreeNode?>>()
    val treeState: LiveData<Resource<TreeNode?>> = _treeStateLiveData

    private val _entryLiveData = MutableLiveData<Resource<ApiEntryData?>>()
    val entryLiveData: LiveData<Resource<ApiEntryData?>> = _entryLiveData

    fun removeNode(nodeId: String) {
        viewModelScope.launch {
            val modifiedTree = repository.removeNode(treeNode = treeState.value?.data, nodeId)
            if (modifiedTree != null) {
                _treeStateLiveData.value = Resource.success(data = modifiedTree)
            }
        }
    }


    fun fetchTreeData() {
        _treeStateLiveData.value = Resource.loading(data = null)
        viewModelScope.launch {
            try {
                _treeStateLiveData.value = repository.fetchTreeData()
            } catch (ex: Exception) {
                _treeStateLiveData.value = Resource.error(ERROR_TYPE.GENERIC_ERROR)
            }
        }
    }

    fun getEntryData(id: String){
        viewModelScope.launch{
            _entryLiveData.value =repository.getEntryData(id)
        }
    }
}
