package com.example.comics.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.comics.data.entities.MovieEntity
import com.example.comics.ui.components.UiItemScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiMainScreen(
    viewModel: MainViewModel
) {

    val movie by viewModel.movie.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Filmes em cartaz") })
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshMovie() },
            state = pullToRefreshState,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (val state = movie) {

                    is State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .testTag("loadingIndicator")
                        )
                    }

                    is State.Success -> {
                        if (state.data.isNullOrEmpty()) {
                            Text(
                                text = "Nenhum filme encontrado.",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .testTag("emptyListMessage")
                            )
                        } else {
                            LazyColumnScreen(state.data)
                        }
                    }

                    is State.Error -> {
                        Text(
                            text = "Erro: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .testTag("errorMessage")
                        )
                    }

                    is State.ErrorOffline -> {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                text = "Erro Offline: ${state.message}",
                                modifier = Modifier.testTag("errorMessageOffline")
                            )
                            state.data?.let { LazyColumnScreen(it) }                        }
                    }

                    }
                }
            }
        }
    }

@Composable
fun LazyColumnScreen(
    listMovie: List<MovieEntity>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .testTag("movieList")
    ) {
        items(listMovie, key = { it.id }) { movie ->
            UiItemScreen(movie = movie)
            HorizontalDivider(
                color = Color.Black,
                thickness = 2.dp
            )
        }
    }
    }


