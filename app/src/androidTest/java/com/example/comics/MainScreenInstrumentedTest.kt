package com.example.comics

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.comics.data.entities.MovieEntity
import com.example.comics.ui.MainActivity
import com.example.comics.ui.MainViewModel
import com.example.comics.ui.State
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class MovieListScreenInstrumentedTest : KoinTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @RelaxedMockK
    lateinit var mockMainViewModel: MainViewModel

    private val movieStateFlow = MutableStateFlow<State<List<MovieEntity>>>(State.Loading())
    private val isRefreshingStateFlow = MutableStateFlow(false)

    private val testModule = module {
        viewModel() {
            mockMainViewModel
        }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockMainViewModel.movie } returns movieStateFlow
        every { mockMainViewModel.isRefreshing } returns isRefreshingStateFlow
        stopKoin()
        loadKoinModules(testModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun GIVEN_loading_state_WHEN_screen_appears_THEN_shows_loading_indicator() {
        movieStateFlow.value = State.Loading()
        isRefreshingStateFlow.value = false
        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("movieList").assertDoesNotExist() // Lista não deve existir ainda
        composeTestRule.onNodeWithTag("errorMessage").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emptyListMessage").assertDoesNotExist()
    }

    @Test
    fun GIVEN_success_state_with_movies_WHEN_screen_appears_THEN_shows_movie_list() {
        val fakeMovies = listOf(
            MovieEntity(id = 1, title = "Filme Legal 1", overview = "aaa", image = ""),
            MovieEntity(id = 2, title = "Outro Filme 2", overview = "aaa", image = "")
        )
        movieStateFlow.value = State.Success(fakeMovies)
        isRefreshingStateFlow.value = false
        composeTestRule.onNodeWithTag("movieList").assertIsDisplayed()
        composeTestRule.onNodeWithTag("movieItem_1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Filme Legal 1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("movieItem_2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Outro Filme 2").assertIsDisplayed()
        composeTestRule.onNodeWithTag("loadingIndicator").assertDoesNotExist()
        composeTestRule.onNodeWithTag("errorMessage").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emptyListMessage").assertDoesNotExist()
    }

    @Test
    fun GIVEN_success_state_with_empty_list_WHEN_screen_appears_THEN_shows_empty_message() {
        movieStateFlow.value = State.Success(emptyList())
        isRefreshingStateFlow.value = false
        composeTestRule.onNodeWithTag("emptyListMessage").assertIsDisplayed()
        composeTestRule.onNodeWithTag("movieList").assertDoesNotExist() // Ou verificar se não tem filhos
        composeTestRule.onNodeWithTag("loadingIndicator").assertDoesNotExist()
        composeTestRule.onNodeWithTag("errorMessage").assertDoesNotExist()
    }

    @Test
    fun GIVEN_error_state_WHEN_screen_appears_THEN_shows_error_message() {
        val errorMessage = "Falha na rede!"
        movieStateFlow.value = State.Error(errorMessage)
        isRefreshingStateFlow.value = false
        composeTestRule.onNodeWithTag("errorMessage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erro: $errorMessage").assertIsDisplayed() // Assumindo prefixo "Erro: "
        composeTestRule.onNodeWithTag("movieList").assertDoesNotExist()
        composeTestRule.onNodeWithTag("loadingIndicator").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emptyListMessage").assertDoesNotExist()
    }
}