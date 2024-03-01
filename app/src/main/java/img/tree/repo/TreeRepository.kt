package img.tree.repo

import android.util.Log
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import img.tree.TAG
import img.tree.models.ApiEntryData
import img.tree.models.TreeNode
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

    private var serverResponse: Resource<TreeNode?>? = null

    suspend fun fetchTreeData(): Resource<TreeNode?>? = withContext(Dispatchers.IO) {
        try {
            if (serverResponse == null) {
                val response = treeAPIService.fetchTreeData()
                val treeRes = buildApiTree(response)
                serverResponse = Resource.success(
                    TreeNode(
                        "rootNode", treeRes, UUID.randomUUID().toString(), 0, 0
                    )
                )
            }
        } catch (noNet: NoConnectivityException) {
            serverResponse = Resource.error(errorType = NO_NETWORK, data = null)
        } catch (e: IOException) {
            serverResponse = Resource.error(errorType = SERVER_ERROR, data = null)
        } catch (e: HttpException) {
            serverResponse = Resource.error(errorType = SERVER_ERROR, data = null)
        }
        return@withContext serverResponse
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

    suspend fun getEntryData(id: String): Resource<ApiEntryData?> {
        Log.d(TAG, "getting entry data")
        return Resource.loading(null)
    }

}