package img.tree.vm


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import img.tree.getOrAwaitValue
import img.tree.models.ApiEntryData
import img.tree.models.TreeNode
import img.tree.network.ERROR_TYPE
import img.tree.network.Resource
import img.tree.repo.TreeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.UUID

class TreeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: TreeRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val sampleTreeResponse = listOf( TreeNode("labelA", listOf(
        TreeNode("LabelAChild1", null, "1.1", 2,0),
        TreeNode("LabelAChild2", null, "1.2", 2,0),
    ), "1", 1, 2),
        TreeNode("labelB", listOf(
            TreeNode("LabelAChild1", null, "2.1", 2,0),
            TreeNode("LabelAChild2", null, "2.2", 2,0),
            TreeNode("LabelAChild3", null, "2.3", 2,0),
            TreeNode("LabelAChild4", null, "2.4", 2,0),
        ), "2", 1, 4)
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getTreeState_empty() = runTest {
        Mockito.`when`(repository.fetchTreeData())
            .thenReturn(Resource.success(getParentNode(emptyList())))
        val sut = TreeViewModel(repository)
        sut.fetchTreeData()
        testScheduler.advanceUntilIdle()
        val result = sut.treeState.getOrAwaitValue()
        Assert.assertEquals(emptyList<TreeNode>(), result.data!!.children)
    }


    @Test
    fun getTreeState_valid() = runTest {
        Mockito.`when`(repository.fetchTreeData())
            .thenReturn(Resource.success(getParentNode(sampleTreeResponse)))
        val sut = TreeViewModel(repository)
        sut.fetchTreeData()
        testScheduler.advanceUntilIdle()
        val result = sut.treeState.getOrAwaitValue()
        Assert.assertEquals(2, result.data!!.children!!.size)
        Assert.assertEquals(4, result.data!!.children!![1].children!!.size)
    }


    @Test
    fun getEntryData_valid() = runTest{
        Mockito.`when`(repository.getEntryData("3232"))
            .thenReturn(Resource.success(ApiEntryData("3333", "2047-04-16T08:07:55.327Z", "saty", "2047-05-16T08:08:55.327Z", "Someone","Updated the docs as per the new guidelines.")))
        val sut = TreeViewModel(repository)
        sut.getEntryData("3232")
        testScheduler.advanceUntilIdle()
        val result = sut.entryLiveData.getOrAwaitValue()
        Assert.assertEquals("saty", result.data!!.createdBy)
    }

    @Test
    fun getEntryData_valid_notfound() = runTest{
        Mockito.`when`(repository.getEntryData("3232"))
            .thenReturn(Resource.error(ERROR_TYPE.EMPTY_RESPONSE))
        val sut = TreeViewModel(repository)
        sut.getEntryData("3232")
        testScheduler.advanceUntilIdle()
        val result = sut.entryLiveData.getOrAwaitValue()
        Assert.assertEquals("EMPTY_RESPONSE", result.error!!.name)
    }

    private fun getParentNode(nodeList: List<TreeNode>): TreeNode {
        return TreeNode(
            "rootNode", nodeList, UUID.randomUUID().toString(), 0, 0
        )
    }
}