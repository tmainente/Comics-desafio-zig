package com.example.comics.interactor

import com.example.comics.CoroutinesTestRule
import com.example.comics.presenter.IPresenter
import com.example.comics.repository.DataModel
import com.example.comics.repository.ItemModel
import com.example.comics.repository.Repository
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InteractorTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private lateinit var interactor: Interactor

    private val iPresenter: IPresenter = mockk(relaxed = true)
    private val repository: Repository = mockk(relaxed = true)

    @Before
    fun setup() {
        interactor = Interactor(iPresenter, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when execute api getComics return mock success`() = runBlocking {
        coEvery { repository.getComics() } returns ItemModel(data = DataModel(results = listOf()))

        interactor.getComics()

        coVerify(exactly = 1) { iPresenter.setupList(any()) }
        verify { iPresenter.error() wasNot called }
    }

    @Test
    fun `when execute api getComics return mock error`() = runBlocking {
        coEvery { repository.getComics() } throws Exception(MOCK_EXCEPTION)

        interactor.getComics()

        coVerify(exactly = 1) { iPresenter.setupList(any()) }
        verify { iPresenter.error() wasNot called }
    }

    private companion object  {
        const val MOCK_EXCEPTION = "Error mockk"
    }
}