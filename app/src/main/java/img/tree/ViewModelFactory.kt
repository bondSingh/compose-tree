package img.tree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import img.tree.repo.TreeRepository
import img.tree.vm.TreeViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val repository: TreeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TreeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TreeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
