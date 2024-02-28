package img.tree.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import img.compose_tree.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarView() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = { /* Handle button click */ }
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Settings"
                )
            }
        }
    )
}