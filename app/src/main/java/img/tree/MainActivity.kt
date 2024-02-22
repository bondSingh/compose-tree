package img.tree

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.hellofresh.task2.api.Status
import img.tree.network.TreeService
import img.tree.ui.theme.Compose_TreeTheme
import img.tree.vm.TreeViewModel

class MainActivity : ComponentActivity() {
    private lateinit var treeViewModel: TreeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        val treeAPIService: TreeService = RetrofitInstance.getRetrofit(this).create(TreeService::class.java)

        val treeRepository = TreeRepository(treeAPIService)
        treeViewModel = ViewModelProvider(this, ViewModelFactory(treeRepository))[TreeViewModel::class.java]
        observeAndUpdateTree()

        if (!treeViewModel.treeList.isInitialized) {
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

    private fun observeAndUpdateTree() {
        treeViewModel.treeList.observe(this) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        if (resource.data.isNullOrEmpty()) {
                            Log.d("SatyLog","Empty List")
                        }
                        resource.data?.let {
                            Log.d("SatyLog","Got Data>> ${resource.data.toString()}")
                        }
                    }

                    Status.ERROR -> {
                        resource.message?.let { error ->
                            Log.d("SatyLog","Error $error")
                        }
                    }

                    Status.LOADING -> {
                        Log.d("SatyLog","Loading Data")
                    }
                }
            }
        }
    }


}


@Composable
fun MyApp() {
    Text(
        text = "Hello",
        style = MaterialTheme.typography.displayMedium
    )
}

