package img.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import img.tree.vm.TreeViewModel

class ViewModelFactory(private val repository: TreeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TreeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TreeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
