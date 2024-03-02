package img.tree.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import img.compose_tree.R
import img.tree.models.TreeNode
import img.tree.randomColor

@Composable
fun TreeView(
    treeState: List<TreeNode>,
    onDeleteNode: (TreeNode) -> Unit,
    onItemClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        //val treeState = remember { mutableStateOf(treeState) }

        LazyColumn {
            items(treeState) { node ->
                TreeNodeItem(node, onDeleteNode, onItemClick)
            }
        }
    }
}


@Composable
fun TreeNodeItem(node: TreeNode, onDeleteNode: (TreeNode) -> Unit, onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 4.dp * node.level)
            .fillMaxWidth()
            .wrapContentHeight()
            .height(maxOf((50 * node.offspringCount), 50).dp)
            .background(randomColor(node.level, isSystemInDarkTheme()))
            .clickable {
                //setting Clickable for only leaf node
                if ((node.id != null) && node.children.isNullOrEmpty()) {
                    onItemClick(node.id)
                }
            }

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            BodyText(text = node.label)
            if ((node.id != null) && node.children.isNullOrEmpty()) {
                Icon(Icons.Default.Info, contentDescription = stringResource(R.string.get_details))
            }
            Button(
                onClick = { onDeleteNode(node) },
                modifier = Modifier.background(Color.Transparent)
            ) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_entry))
            }
        }

        if (node.children?.isNotEmpty() == true) {
            TreeView(node.children, onDeleteNode, onItemClick)
        }
    }
}