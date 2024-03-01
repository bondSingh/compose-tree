package img.tree.di

import dagger.Component
import img.tree.view.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface TreeComponent {

    fun inject(mainActivity: MainActivity)



}