package img.tree.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import img.tree.TreeApplication
import img.tree.ViewModelFactory
import img.tree.ui.theme.Compose_TreeTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
     //private lateinit var treeViewModel: TreeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        //val treeAPIService: TreeAPIService =
          //  RetrofitInstance.getRetrofit().create(TreeAPIService::class.java)
        //val treeRepository = TreeRepository(treeAPIService)
        (application as TreeApplication).treeComponent.inject(this)
        //treeViewModel = ViewModelProvider(this, viewModelFactory)[TreeViewModel::class.java]

        setContent {
            Compose_TreeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }

        /*if (!treeViewModel.treeState.isInitialized) {
            treeViewModel.fetchTreeData()
        }*/
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val navController = rememberNavController()


    Scaffold(
        topBar = { TopBarView() },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "mainAppScreen",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("mainAppScreen") {
                    MainAppScreen {
                        navController.navigate("entryDetailScreen/$it")
                    }
                }

                composable("entryDetailScreen/{nodeId}", arguments = listOf(
                    navArgument("nodeId") {
                        type = NavType.StringType
                    }
                )) { entry ->
                    val nodeId = entry.arguments?.getString("nodeId")
                    if (nodeId != null) {
                        EntryDetailScreen(navController = navController, nodeId)
                    }
                }
            }
        }
    )
}







