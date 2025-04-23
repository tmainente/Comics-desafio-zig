package com.example.comics.presenter

import com.example.comics.CoroutinesTestRule
import com.example.comics.repository.DataModel
import com.example.comics.repository.ItemModel
import com.example.comics.view.IView
import io.mockk.called
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
class PresenterTest {

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    private lateinit var presenter: Presenter

    private val iView: IView = mockk(relaxed = true)

    @Before
    fun setup() {
        presenter = Presenter(iView)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when execute setupList`() = runBlocking {
        presenter.setupList(ItemModel(data = DataModel(results = listOf())))

        coVerify(exactly = 1) { iView.viewList(any()) }
        verify { iView.error() wasNot called }
    }

    @Test
    fun `when execute error`() = runBlocking {
        presenter.error()

        coVerify(exactly = 1) { iView.error() }
        verify { iView.viewList(any()) wasNot called }
    }
}