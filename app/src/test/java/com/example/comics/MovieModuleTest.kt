package com.example.comics

import android.content.Context
import com.example.comics.data.local.AppDatabase
import com.example.comics.data.remote.api.Api
import com.example.comics.di.MovieModule
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import retrofit2.Retrofit


class MovieModuleTest : KoinTest {

    @Test
    fun `checkModules should verify MovieModule definitions`() {
        koinApplication {
            androidContext(mockk<Context>(relaxed = true)) // Mock Context
            modules(
                module {
                    single {
                        mockk<Retrofit>(relaxed = true) {
                              every { create(Api::class.java) } returns mockk<Api>(relaxed = true)
                        }
                    }
                    single { mockk<AppDatabase>(relaxed = true) }
                },
                MovieModule.modules
            )
            checkModules()
        }
        assertTrue(true)
    }
}