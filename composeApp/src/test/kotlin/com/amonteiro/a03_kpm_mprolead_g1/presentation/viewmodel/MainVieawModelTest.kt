package com.amonteiro.a03_kmp_mprolead_g1.presentation.viewmodel

import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerAPI
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockAPI: PhotographerAPI
    private lateinit var viewModel: MainViewModel

    private val fakePhotographers = listOf(
        PhotographerDTO(
            id = 10,
            stageName = "Alice Click",
            photoUrl = "https://example.com/alice.jpg",
            story = "Une photographe fictive.",
            portfolio = listOf("https://picsum.photos/10")
        ),
        PhotographerDTO(
            id = 11,
            stageName = "Marc Zoom",
            photoUrl = "https://example.com/marc.jpg",
            story = "Un autre photographe fictif.",
            portfolio = listOf("https://picsum.photos/11")
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockAPI = mock()
        viewModel = MainViewModel(mockAPI)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ─── init / loadFakeData ───────────────────────────────────────────────────

    @Test
    fun `init - dataList contient 2 photographes par défaut`() {
        assertEquals(2, viewModel.dataList.value.size)
    }

    @Test
    fun `init - runInProgress est false par défaut`() {
        assertFalse(viewModel.runInProgress.value)
    }

    @Test
    fun `init - errorMessage est vide par défaut`() {
        assertEquals("", viewModel.errorMessage.value)
    }

    @Test
    fun `loadFakeData - réinitialise les états avec les valeurs passées`() {
        viewModel.loadFakeData(runInProgress = true, errorMessage = "Erreur test")

        assertTrue(viewModel.runInProgress.value)
        assertEquals("Erreur test", viewModel.errorMessage.value)
        assertEquals(2, viewModel.dataList.value.size)
    }

    @Test
    fun `loadFakeData - les stagenames attendus sont présents`() {
        viewModel.loadFakeData()
        val names = viewModel.dataList.value.map { it.stageName }
        assertTrue(names.contains("Bob la Menace"))
        assertTrue(names.contains("Jean-Claude Flash"))
    }

    // ─── loadPhotographer – succès ────────────────────────────────────────────

    @Test
    fun `loadPhotographer - met runInProgress à true pendant le chargement`() = runTest {
        whenever(mockAPI.loadPhotographers()).thenReturn(fakePhotographers)

        viewModel.loadPhotographer()

        // Avant que les coroutines avancent, le flag doit être levé
        assertTrue(viewModel.runInProgress.value)
    }

    @Test
    fun `loadPhotographer - dataList est mis à jour après succès`() = runTest {
        whenever(mockAPI.loadPhotographers()).thenReturn(fakePhotographers)

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertEquals(fakePhotographers, viewModel.dataList.value)
    }

    @Test
    fun `loadPhotographer - runInProgress repasse à false après succès`() = runTest {
        whenever(mockAPI.loadPhotographers()).thenReturn(fakePhotographers)

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertFalse(viewModel.runInProgress.value)
    }

    @Test
    fun `loadPhotographer - errorMessage reste vide après succès`() = runTest {
        whenever(mockAPI.loadPhotographers()).thenReturn(fakePhotographers)

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertEquals("", viewModel.errorMessage.value)
    }

    // ─── loadPhotographer – erreur ────────────────────────────────────────────

    @Test
    fun `loadPhotographer - errorMessage contient le message d'exception`() = runTest {
        val errorMsg = "Connexion impossible"
        whenever(mockAPI.loadPhotographers()).thenThrow(RuntimeException(errorMsg))

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertEquals(errorMsg, viewModel.errorMessage.value)
    }

    @Test
    fun `loadPhotographer - errorMessage vaut 'Une erreur' si exception sans message`() = runTest {
        whenever(mockAPI.loadPhotographers()).thenThrow(RuntimeException())

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertEquals("Une erreur", viewModel.errorMessage.value)
    }

    @Test
    fun `loadPhotographer - runInProgress repasse à false après erreur`() = runTest {
        whenever(mockAPI.loadPhotographers()).thenThrow(RuntimeException("Oops"))

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertFalse(viewModel.runInProgress.value)
    }

    @Test
    fun `loadPhotographer - dataList n'est pas modifiée en cas d'erreur`() = runTest {
        viewModel.loadFakeData() // charge les 2 photographes par défaut
        val before = viewModel.dataList.value

        whenever(mockAPI.loadPhotographers()).thenThrow(RuntimeException("Oops"))

        viewModel.loadPhotographer()
        advanceUntilIdle()

        assertEquals(before, viewModel.dataList.value)
    }
}