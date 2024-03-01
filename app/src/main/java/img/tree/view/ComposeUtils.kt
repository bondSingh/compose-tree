package img.tree.view

import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BodyText(text : String)  {
    val localStyle = LocalTextStyle.current
    val mergedStyle = localStyle.merge(TextStyle(color = LocalContentColor.current, fontSize = 22.sp))
    Text(text = text, style = mergedStyle)
}

@Composable
fun DividerLine(){
    Divider(color = MaterialTheme.colorScheme.onSecondaryContainer, thickness = 1.dp)
}