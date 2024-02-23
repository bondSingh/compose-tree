package img.tree.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import img.tree.RetrofitInstance
import img.tree.TAG
import img.tree.TreeRepository
import img.tree.ViewModelFactory
import img.tree.models.TreeNode
import img.tree.network.TreeAPIService
import img.tree.randomColor
import img.tree.ui.theme.Compose_TreeTheme
import img.tree.vm.TreeViewModel

class MainActivity : ComponentActivity() {
    private lateinit var treeViewModel: TreeViewModel
    private var treeState: List<TreeNode> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        val treeAPIService: TreeAPIService =
            RetrofitInstance.getRetrofit(this).create(TreeAPIService::class.java)
        val treeRepository = TreeRepository(treeAPIService)
        treeViewModel =
            ViewModelProvider(this, ViewModelFactory(treeRepository))[TreeViewModel::class.java]
        //observeAndUpdateTree()

        if (!treeViewModel.treeState.isInitialized) {
            treeViewModel.fetchTreeData()
        }


        setContent {
            Compose_TreeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }

    /*private fun observeAndUpdateTree() {
        treeViewModel.treeState.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        if (resource.data.isNullOrEmpty()) {
                            Log.d(TAG, "Empty List")
                        }
                        resource.data?.let {
                            treeState = resource.data
                            Log.d(TAG, "Got Data>> ${resource.data.toString()}")
                        }
                    }

                    Status.ERROR -> {
                        resource.message?.let { error ->
                            Log.d(TAG, "Error $error")
                        }
                    }

                    Status.LOADING -> {
                        Log.d(TAG, "Loading Data")
                    }
                }
            }
        }
    }*/
}

@Composable
fun MyApp() {
    val viewModel: TreeViewModel = viewModel()
    val treeState: List<TreeNode> by viewModel.treeState.observeAsState(emptyList())

    TreeView(treeState = treeState, onItemClick = { Log.d(TAG, "Item Clicked") })
    //TreeView(treeState = treeState, viewModel )
}


@Composable
fun TreeView(treeState: List<TreeNode>, onItemClick: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            items(treeState) { node ->
                TreeNodeItem(node, onItemClick)
            }
        }
    }
}


@Composable
fun TreeNodeItem(node: TreeNode, onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 4.dp * node.level)
            .fillMaxWidth()
            .wrapContentHeight()
            .height(maxOf((50 * node.offspringCount), 50).dp)
            .background(randomColor(node.level, isSystemInDarkTheme()))

    ) {
        Text(text = node.label, style = MaterialTheme.typography.headlineMedium)
        if (node.children?.isNotEmpty() == true) {
            TreeView(node.children, onItemClick)
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


