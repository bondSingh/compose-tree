package img.tree.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import img.tree.handleErrors
import img.tree.models.ApiEntryData
import img.tree.network.ERROR_TYPE
import img.tree.network.Resource
import img.tree.network.Status
import img.tree.vm.TreeViewModel


@Composable
fun EntryDetailScreen(navController: NavController, nodeId: String) {
    val viewModel: TreeViewModel = hiltViewModel()
    val responseData: Resource<ApiEntryData?> by viewModel.entryLiveData.observeAsState(
        Resource(
            Status.LOADING,
            data = null,
            error = null
        )
    )

    LaunchedEffect(Unit) {
        viewModel.getEntryData(nodeId)
    }

    when (responseData.status) {
        Status.SUCCESS -> {
            if (responseData.data != null) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    showLogDetails(responseData.data!!)
                    showBackButton {
                        navController.popBackStack()
                    }

                }
            } else {
                showBackButton(navController::popBackStack)
                handleErrors(
                    LocalContext.current,
                    stringResource(ERROR_TYPE.EMPTY_RESPONSE.errorMessage())
                )
            }
        }

        Status.ERROR -> {
            showBackButton(goBack = navController::popBackStack)
            handleErrors(
                LocalContext.current,
                responseData.error?.let { stringResource(id = it.errorMessage()) })
        }

        Status.LOADING -> {
            showLoaderView()
        }

    }
}

@Composable
fun showLogDetails(data: ApiEntryData) {
    val localStyle = LocalTextStyle.current
    val mergedStyle =
        localStyle.merge(TextStyle(color = LocalContentColor.current, fontSize = 22.sp))
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "id :" + data.id, style = mergedStyle)
        Text(text = "createdAt : " + data.createdAt, style = mergedStyle)
        Text(text = "createdBy : " + data.createdBy, style = mergedStyle)
        Text(text = "lastModifiedAt : " + data.lastModifiedAt, style = mergedStyle)
        Text(text = "lastModifiedAt : " + data.lastModifiedBy, style = mergedStyle)
        Text(text = "Description : " + data.description, style = mergedStyle)
    }
}

@Composable
fun showBackButton(goBack: () -> Unit) {
    OutlinedButton(
        onClick = { goBack() },
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(0.dp,16.dp,0.dp,16.dp)
    ) {
        Text(
            text = "Go back to listing",
            fontSize = 28.sp,
        )
    }
}

