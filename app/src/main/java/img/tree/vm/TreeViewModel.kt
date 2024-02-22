package img.tree.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellofresh.task2.api.Resource
import img.tree.TreeRepository
import img.tree.models.TreeNode
import img.tree.network.NoConnectivityException
import kotlinx.coroutines.launch

class TreeViewModel(private val repository: TreeRepository) : ViewModel() {
    private val _treeListLiveData = MutableLiveData<Resource<List<TreeNode>>>()
    val treeList: LiveData<Resource<List<TreeNode>>> get() = _treeListLiveData


    fun fetchTreeData() {
        viewModelScope.launch {
            _treeListLiveData.value = Resource.loading(data = null)
            try {
                val recipes = repository.fetchTreeData()
                _treeListLiveData.value = Resource.success(data = recipes)
            } catch (noNet : NoConnectivityException){
                _treeListLiveData.value =
                    Resource.error(data = null, message = noNet.message.toString())
            }
            catch (e: Exception) {
                if (e.message != null) {
                    _treeListLiveData.value =
                        Resource.error(data = null, message = e.message.toString())
                } else {
                    _treeListLiveData.value = Resource.error(
                        data = null,
                        message = "Something went wrong. Please try again later."
                    )
                }
            }
        }
    }
}
