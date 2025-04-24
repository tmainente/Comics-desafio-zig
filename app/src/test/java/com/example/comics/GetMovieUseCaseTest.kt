package com.example.comics

import app.cash.turbine.test
import com.example.comics.data.entities.MovieEntity
import com.example.comics.data.remote.repository.MovieRepository
import com.example.comics.data.remote.util.Resource
import com.example.comics.domain.usecase.GetMovieUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMovieUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieUseCase: GetMovieUseCase

    @Before
    fun setUp() {
        movieRepository = mockk<MovieRepository>()
        getMovieUseCase = GetMovieUseCase(movieRepository)
    }

    @Test
    fun `WHEN invoke is called THEN should call getMovie from repository exactly once`() = runTest {
        val mockFlow: Flow<Resource<List<MovieEntity>>> = flowOf() // Um flow vazio serve
        every { movieRepository.getMovie() } returns mockFlow
        // Act
        getMovieUseCase()
        // Assert
        verify(exactly = 1) { movieRepository.getMovie() }
        confirmVerified(movieRepository)
    }

    @Test
    fun `WHEN invoke is called THEN should return the Flow from repository`() = runTest {
        val expectedData = listOf(MovieEntity(id = 1, title = "Test Movie", overview = "", image = ""))
        val expectedFlow: Flow<Resource<List<MovieEntity>>> = flowOf(
            Resource.Loading(null),
            Resource.Success(expectedData)
        )
        every { movieRepository.getMovie() } returns expectedFlow

        val resultFlow = getMovieUseCase()
        assertSame( expectedFlow, resultFlow)
        resultFlow.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is Resource.Loading)

            val successState = awaitItem()
            assertTrue(successState is Resource.Success)
            assertEquals(expectedData, successState.data)
            awaitComplete()
        }
        verify(exactly = 1) { movieRepository.getMovie() }
        confirmVerified(movieRepository)
    }

    @Test
    fun `WHEN repository getMovie returns error Flow THEN invoke returns error Flow`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        val expectedFlow: Flow<Resource<List<MovieEntity>>> = flowOf(Resource.Error(errorMessage, null))
        every { movieRepository.getMovie() } returns expectedFlow

        // Act
        val resultFlow = getMovieUseCase()

        // Assert
        assertSame(expectedFlow, resultFlow)

        resultFlow.test {
            val errorState = awaitItem()
            assertTrue(errorState is Resource.Error)
            assertEquals(errorMessage, errorState.message)
            assertNull(errorState.data)
            awaitComplete()
        }
        verify(exactly = 1) { movieRepository.getMovie() }
        confirmVerified(movieRepository)
    }
}