package img.tree

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import img.tree.di.DaggerTreeComponent
import img.tree.di.TreeComponent

@HiltAndroidApp
class TreeApplication : Application() {
    lateinit var treeComponent: TreeComponent

    override fun onCreate() {
        super.onCreate()
        treeComponent = DaggerTreeComponent.builder().build()
    }
}