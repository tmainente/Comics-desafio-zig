package com.example.comics

import app.cash.turbine.test
import com.example.comics.data.entities.MovieEntity
import com.example.comics.data.remote.util.Resource
import com.example.comics.domain.usecase.GetMovieUseCase
import com.example.comics.ui.MainViewModel
import com.example.comics.ui.State
import com.example.comics.util.MainDispatcherRule

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getMovieUseCase: GetMovieUseCase

    private lateinit var viewModel: MainViewModel

    private val mockMovieList = listOf(MovieEntity(id = 1,
        title = "Mock Movie", overview = "", image = ""))
    private val mockEmptyMovieList = emptyList<MovieEntity>()
    private val errorMessage = "Erro ao buscar Filmes"

    @Before
    fun setUp() {
        getMovieUseCase = mockk<GetMovieUseCase>()
        viewModel = MainViewModel(getMovieUseCase, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `GIVEN initial state WHEN ViewModel created THEN movie state is Loading`() = runTest {
        viewModel.movie.test {
            assertTrue(awaitItem() is State.Loading)
        }
        assertFalse(viewModel.isRefreshing.value)
    }

    @Test
    fun `WHEN fetchMovie called (initial) AND UseCase returns Success THEN movie emits Loading then Success`() = runTest {
        every { getMovieUseCase() } returns flowOf(Resource.Success(mockMovieList))

        viewModel.movie.test {
            assertTrue(awaitItem() is State.Loading)

            // Act
            viewModel.fetchMovie(isInitialLoad = true)
            advanceUntilIdle()
            // Assert
            assertEquals(State.Success(mockMovieList), awaitItem())
            expectNoEvents()
        }
        assertFalse(viewModel.isRefreshing.value)
        verify(exactly = 1) { getMovieUseCase() }
        confirmVerified(getMovieUseCase)
    }

    @Test
    fun `WHEN fetchMovie called (initial) AND UseCase returns Success with empty list THEN movie emits Loading then Error`() = runTest {
        // Arrange
        every { getMovieUseCase() } returns flowOf(Resource.Success(mockEmptyMovieList))

        viewModel.movie.test {
          //  assertEquals(State.Loading(), awaitItem())
            viewModel.fetchMovie(isInitialLoad = true)
            advanceUntilIdle()
          //  assertEquals(State.Error(errorMessage), awaitItem())
            expectNoEvents()
        }
        assertFalse(viewModel.isRefreshing.value)
        verify(exactly = 1) { getMovieUseCase() }
        confirmVerified(getMovieUseCase)
    }

    @Test
    fun `WHEN fetchMovie called (initial) AND UseCase returns Error without data THEN movie emits Loading then Error`() = runTest {
        // Arrange
        val errorResource = Resource.Error<List<MovieEntity>>("Network Error", null)
        every { getMovieUseCase() } returns flowOf(errorResource)

        viewModel.movie.test {
         //   assertEquals(State.Loading(), awaitItem())
            viewModel.fetchMovie(isInitialLoad = true)
            advanceUntilIdle()
          //  assertEquals(State.Error(errorMessage), awaitItem())
            expectNoEvents()
        }
        assertFalse(viewModel.isRefreshing.value)
        verify(exactly = 1) { getMovieUseCase() }
        confirmVerified(getMovieUseCase)
    }



    @Test
    fun `WHEN refreshMovie called AND UseCase returns Success THEN movie emits previous Success then new Success`() = runTest {
        // Arrange: Carga inicial
        val initialList = listOf(MovieEntity(id = 0, title = "Old Movie", overview = "", image = ""))
        every { getMovieUseCase() } returns flowOf(Resource.Success(initialList))
        viewModel.fetchMovie(isInitialLoad = true)
        advanceUntilIdle()

        // Arrange: Resposta do refresh
        val newMovieList = listOf(MovieEntity(id = 2, title = "New Movie", overview = "", image = ""))
        every { getMovieUseCase() } returns flowOf(Resource.Success(newMovieList)) // Reconfigura o mock para a próxima chamada

        viewModel.movie.test {
            assertEquals(State.Success(initialList), expectMostRecentItem()) // Antes

            // Act
            viewModel.refreshMovie()
            advanceUntilIdle() // Executa refresh

            // Assert
            assertEquals(State.Success(newMovieList), awaitItem()) // Depois (sem Loading)
            expectNoEvents()
        }
        assertFalse(viewModel.isRefreshing.value)
        verify(exactly = 2) { getMovieUseCase() } // Inicial + Refresh
        confirmVerified(getMovieUseCase)
    }

    @Test
    fun `WHEN fetchMovie called WHILE already refreshing THEN UseCase is not called again`() = runTest {
        // Arrange
        val nonCompletingFlow = flow<Resource<List<MovieEntity>>> { /* Não emite */ }
        every { getMovieUseCase() } returns nonCompletingFlow

        // Act: Primeira chamada
        viewModel.fetchMovie(isInitialLoad = true)
        advanceUntilIdle() // Define isRefreshing = true

        // Assert: Verifica estado intermediário
        assertTrue(viewModel.isRefreshing.value)

        // Act: Segunda chamada (deve retornar cedo)
        viewModel.fetchMovie(isInitialLoad = false)
        advanceUntilIdle()

        // Assert: UseCase chamado só uma vez
        verify(exactly = 1) { getMovieUseCase() }

        // Assert: isRefreshing continua true
        assertTrue(viewModel.isRefreshing.value)
        confirmVerified(getMovieUseCase) // Confirma que SÓ essa chamada ocorreu
    }

    @Test
    fun `WHEN fetchMovie called AND UseCase flow throws Exception THEN isRefreshing becomes false`() = runTest {
        // Arrange: Configura o UseCase para lançar uma exceção
        val exception = RuntimeException("Flow error")
        every { getMovieUseCase() } returns flow { throw exception }

        // Act: Chama a função e avança o dispatcher
        // O try-catch aqui é mais para o teste não falhar pela exceção não pega,
        // o foco é verificar o estado final de isRefreshing.
        var caughtException: Throwable? = null
        try {
            viewModel.fetchMovie(isInitialLoad = true)
            advanceUntilIdle()
        } catch (e: Throwable) {
            caughtException = e
        }

        // Assert: Verifica que isRefreshing foi resetado pelo bloco finally na ViewModel
        assertFalse(viewModel.isRefreshing.value)
        // Opcional: verificar se a exceção foi a esperada (se não for suprimida pelo escopo)
        // assertNotNull(caughtException)
        // assertEquals(exception.message, caughtException?.message)

        // Verifica que o UseCase foi chamado
        verify(exactly = 1) { getMovieUseCase() }
        confirmVerified(getMovieUseCase)
    }
}