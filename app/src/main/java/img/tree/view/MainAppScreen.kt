package img.tree.view

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import img.tree.handleErrors
import img.tree.models.TreeNode
import img.tree.network.ERROR_TYPE
import img.tree.network.Resource
import img.tree.network.Status
import img.tree.vm.TreeViewModel

@Composable
fun MainAppScreen(onItemClick: (String) -> Unit) {
    Box {
        val viewModel: TreeViewModel = hiltViewModel()
        val responseData: Resource<TreeNode?> by viewModel.treeState.observeAsState(
            Resource(
                Status.LOADING,
                data = null,
                error = null
            )
        )
        LaunchedEffect(Unit) {
            if (!viewModel.treeState.isInitialized) {
                viewModel.fetchTreeData()
            }
        }

        when (responseData.status) {
            Status.SUCCESS -> {
                if (responseData.data != null && responseData.data!!.children?.size!! > 0) {
                    responseData.data!!.children?.let { treeNodeList ->
                        TreeView(treeState = treeNodeList,
                            onDeleteNode = { it.id?.let { it1 -> viewModel.removeNode(it1) } },
                            onItemClick = {
                                onItemClick(it)
                            })
                    }
                } else {
                    handleErrors(
                        LocalContext.current,
                        stringResource(ERROR_TYPE.EMPTY_RESPONSE.errorMessage())
                    )
                }
            }

            Status.ERROR -> {
                handleErrors(
                    LocalContext.current,
                    responseData.error?.let { stringResource(id = it.errorMessage()) })
            }

            Status.LOADING -> {
                showLoaderView()
            }
        }
    }
}


