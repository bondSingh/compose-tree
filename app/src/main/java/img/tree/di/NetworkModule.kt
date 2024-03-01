package img.tree.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import img.tree.RetrofitInstance
import img.tree.network.TreeAPIService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit{
        return RetrofitInstance.getRetrofit()
    }

    @Singleton
    @Provides
    fun provideTreeAPIService(retrofit: Retrofit) : TreeAPIService{
        return retrofit.create(TreeAPIService::class.java)
    }
}