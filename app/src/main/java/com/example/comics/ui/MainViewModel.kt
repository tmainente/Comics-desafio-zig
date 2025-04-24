package com.example.comics.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comics.data.entities.MovieEntity
import com.example.comics.data.remote.util.Resource
import com.example.comics.domain.usecase.GetMovieUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class MainViewModel(
    private val getMovieUseCase: GetMovieUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val _movie = MutableStateFlow<State<List<MovieEntity>>>(State.Loading())
    val movie: StateFlow<State<List<MovieEntity>>> get() = _movie

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun fetchMovie(isInitialLoad: Boolean = true) {
        if (_isRefreshing.value) {
            return
        }
        viewModelScope.launch(ioDispatcher) {
            _isRefreshing.value = true
                getMovieUseCase().collectLatest { movieResource ->
                    when (movieResource) {
                        is Resource.Loading -> {
                            if (isInitialLoad) _movie.value = State.Loading()
                        }

                        is Resource.Success -> {
                            _movie.value = if (movieResource.data.isNullOrEmpty())
                                State.Error(ERROR_MESSAGE)
                            else
                                State.Success(movieResource.data)

                            _isRefreshing.value = false

                        }

                        else -> {
                            _isRefreshing.value = false
                            _movie.value = if (movieResource.data.isNullOrEmpty())
                                State.Error(ERROR_MESSAGE)
                            else State.ErrorOffline(ERROR_MESSAGE, movieResource.data)
                        }
                    }
                }

        }
    }

    fun refreshMovie() {
        fetchMovie(isInitialLoad= false)
    }

    companion object {
        private const val ERROR_MESSAGE = "Erro ao buscar Filmes"
    }
}