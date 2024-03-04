package img.tree.repo

import android.util.Log
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import img.tree.TAG
import img.tree.models.ApiEntryData
import img.tree.models.TreeNode
import img.tree.network.ERROR_TYPE.NOT_FOUND
import img.tree.network.ERROR_TYPE.NO_NETWORK
import img.tree.network.ERROR_TYPE.SERVER_ERROR
import img.tree.network.NoConnectivityException
import img.tree.network.Resource
import img.tree.network.TreeAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
class TreeRepository @Inject constructor(private val treeAPIService: TreeAPIService) {

    private var treeData: Resource<TreeNode?>? = null

    private var entryData: Resource<ApiEntryData?>? = null

    suspend fun fetchTreeData(): Resource<TreeNode?>? = withContext(Dispatchers.IO) {
        try {
            if (treeData == null) {
                val response = treeAPIService.fetchTreeData()
                val treeRes = response.body()?.let { buildApiTree(it) }
                treeData = Resource.success(
                    TreeNode(
                        "rootNode", treeRes, UUID.randomUUID().toString(), 0, 0
                    )
                )
            }
        } catch (noNet: NoConnectivityException) {
            treeData = Resource.error(errorType = NO_NETWORK, data = null)
        } catch (e: IOException) {
            treeData = Resource.error(errorType = SERVER_ERROR, data = null)
        } catch (e: HttpException) {
            treeData = Resource.error(errorType = SERVER_ERROR, data = null)
        }
        return@withContext treeData
    }


    fun removeNode(treeNode: TreeNode?, nodeId: String): TreeNode? {
        if (treeNode == null) {
            return null
        }
        if (treeNode.id == nodeId) {
            Log.d(TAG, "Deleting Item : $treeNode")
            return null // Deleting the current node
        }

        var updatedChildren = treeNode.children?.mapNotNull { removeNode(it, nodeId) }
        updatedChildren = updatedChildren?.let { buildTree(it) }
        return treeNode.id?.let {
            TreeNode(
                treeNode.label,
                updatedChildren,
                treeNode.id,
                treeNode.level,
                treeNode.offspringCount
            )
        }
    }

    suspend fun getEntryData(id: String): Resource<ApiEntryData?>? {
        entryData = try {
            Log.d(TAG, "Making API Call")
            val response = treeAPIService.fetchEntryData(id)
            if (response.isSuccessful && response.body().toString().isNotEmpty() ){
                val responseTransformed = transformEntryData(response.body()!!)
                Resource.success(responseTransformed)
            } else{
                Resource.error(data = null, errorType = NOT_FOUND)
            }
        } catch (exception :Exception){
            Resource.error(data = null, errorType = SERVER_ERROR)
        }

        return entryData
    }



}