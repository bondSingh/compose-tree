package img.tree.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import img.tree.RetrofitInstance
import img.tree.TreeRepositoryImpl
import img.tree.ViewModelFactory
import img.tree.network.TreeAPIService
import img.tree.ui.theme.Compose_TreeTheme
import img.tree.vm.TreeViewModel

class MainActivity : ComponentActivity() {
    private lateinit var treeViewModel: TreeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        val treeAPIService: TreeAPIService =
            RetrofitInstance.getRetrofit(this).create(TreeAPIService::class.java)
        val treeRepository = TreeRepositoryImpl(treeAPIService)
        treeViewModel =
            ViewModelProvider(this, ViewModelFactory(treeRepository))[TreeViewModel::class.java]

        setContent {
            Compose_TreeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                    //showLoaderView()
                }
            }
        }

        if (!treeViewModel.treeState.isInitialized) {
            treeViewModel.fetchTreeData()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    Scaffold(
        topBar = { TopBarView() },
        content = { MainAppScreen(it) }
    )
}







