package com.example.comics.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comics.data.entities.MovieEntity
import com.example.comics.data.remote.util.Resource
import com.example.comics.domain.usecase.GetMovieUseCase
import com.example.comics.util.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val getMovieUseCase: GetMovieUseCase
) : ViewModel() {


    private val _movie = MutableStateFlow<State<List<MovieEntity>>>(State.Loading())
    val movie: StateFlow<State<List<MovieEntity>>> get() = _movie

    fun fetchMovie() {
        viewModelScope.launch {
            getMovieUseCase().collectLatest { movieResource ->
                when (movieResource) {
                    is Resource.Loading -> {
                        _movie.value = State.Loading()
                    }

                    is Resource.Success -> {
                        if (movieResource.data.isNullOrEmpty()) {
                            _movie.value = State.Error(ERROR_MESSAGE)
                        } else {
                            _movie.value = State.Success(movieResource.data)
                        }
                    }

                    else -> {
                        _movie.value = State.Error(ERROR_MESSAGE)

                    }
                }
            }
        }
    }

    companion object {
        private const val ERROR_MESSAGE = "Erro ao buscar os quadrinhos"
    }
}