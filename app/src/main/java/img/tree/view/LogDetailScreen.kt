package img.tree.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import img.compose_tree.R
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
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                ) {
                    showEmptyView()
                    showBackButton(navController::popBackStack)
                }

                handleErrors(
                    LocalContext.current,
                    stringResource(ERROR_TYPE.EMPTY_RESPONSE.errorMessage())
                )
            }
        }

        Status.ERROR -> {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                showEmptyView()
                showBackButton(navController::popBackStack)
            }
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
fun showEmptyView() {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BodyText(text = stringResource(R.string.no_entry_found_msg))
    }
}

@Composable
fun showLogDetails(data: ApiEntryData) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BodyText(text = stringResource(R.string.id, data.id))
        DividerLine()
        BodyText(text = stringResource(R.string.created_at, data.createdAt))
        DividerLine()
        BodyText(text = stringResource(R.string.created_by, data.createdBy))
        DividerLine()
        BodyText(text = stringResource(R.string.last_modified_at, data.lastModifiedAt))
        DividerLine()
        BodyText(text = stringResource(R.string.lastmodifiedby, data.lastModifiedBy))
        DividerLine()
        BodyText(text = stringResource(R.string.description, data.description))
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
            .padding(0.dp, 16.dp, 0.dp, 16.dp)
    ) {
        BodyText(
            text = stringResource(R.string.go_back_to_listing)
        )
    }
}

