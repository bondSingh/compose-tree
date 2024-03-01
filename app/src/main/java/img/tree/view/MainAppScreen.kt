package img.tree.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
        if (!viewModel.treeState.isInitialized) {
            viewModel.fetchTreeData()
        }
        when (responseData.status) {
            Status.SUCCESS -> {
                if (responseData.data != null && responseData.data!!.children?.size!! > 0) {
                    responseData.data!!.children?.let { treeNodeList ->
                        TreeView(treeState = treeNodeList,
                            onDeleteNode = { it.id?.let { it1 -> viewModel.removeNode(it1) } },
                            onItemClick = {
                                onItemClick
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


@Composable
fun EntryDetailScreen(navController: NavController, nodeId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "You entered: $nodeId ", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Go back to Screen 1")
        }
    }
}


