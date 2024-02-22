package img.tree.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import img.tree.network.Resource
import img.tree.TreeRepository
import img.tree.models.TreeNode
import img.tree.network.NoConnectivityException
import kotlinx.coroutines.launch

class TreeViewModel(private val repository: TreeRepository) : ViewModel() {
    private val _treeStateLiveData = MutableLiveData<Resource<List<TreeNode>>>()
    val treeState: LiveData<Resource<List<TreeNode>>> = _treeStateLiveData

    fun getValue() :LiveData<Resource<List<TreeNode>>>{
        return treeState
    }

    fun fetchTreeData() {
        viewModelScope.launch {
            _treeStateLiveData.value = Resource.loading(data = null)
            try {
                val recipes = repository.fetchTreeData()
                _treeStateLiveData.value = Resource.success(data = recipes)
            } catch (noNet : NoConnectivityException){
                _treeStateLiveData.value =
                    Resource.error(data = null, message = noNet.message.toString())
            }
            catch (e: Exception) {
                if (e.message != null) {
                    _treeStateLiveData.value =
                        Resource.error(data = null, message = e.message.toString())
                } else {
                    _treeStateLiveData.value = Resource.error(
                        data = null,
                        message = "Something went wrong. Please try again later."
                    )
                }
            }
        }
    }
}
