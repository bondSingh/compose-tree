package img.tree

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color

fun randomColor(level:Int, isInDarkMode: Boolean): Color {

    val lightColors = listOf(
        Color(0xFFffe082) ,
        Color(0xFFffb300),
        Color(0xFFd1c4e9),
        Color(0xFF673ab7),
        Color(0xFFffccbc),
        Color(0xFFeeeeee),
        Color(0xFFb3e5fc),
        Color(0xFFc5e1a5)
    )

    val darkColors = listOf(
        Color(0xFF4d73ff) ,
        Color(0xFF455a64),
        Color(0xFF6d4c41),
        Color(0xFF3e2723),
        Color(0xFF9575cd),
        Color(0xFF651fff),
        Color(0xFF616161),
        Color(0xFFc5e1a5)
    )
    return if(isInDarkMode) {
        darkColors[darkColors.lastIndex/level]
    } else{
        lightColors[lightColors.lastIndex/level]
    }
}

fun handleErrors(context: Context, error: String? = null) {
    if (error.isNullOrEmpty()){
        Toast.makeText(context, "Something went wrong!!", Toast.LENGTH_LONG).show()
    } else{
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}