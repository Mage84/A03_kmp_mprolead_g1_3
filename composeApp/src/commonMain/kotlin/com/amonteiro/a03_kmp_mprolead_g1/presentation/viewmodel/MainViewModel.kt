package com.amonteiro.a03_kmp_mprolead_g1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerAPI
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(val photographerAPI: PhotographerAPI) : ViewModel() {
    val dataList = MutableStateFlow(emptyList<PhotographerDTO>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {//Création d'un jeu de donnée au démarrage
        println("Instanciation de MainViewModel")
        loadFakeData()
    }

    fun loadPhotographer() {
        runInProgress.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataList.value = photographerAPI.loadPhotographers()
            }
            catch(e:Exception){
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur"
            }
            runInProgress.value = false
        }

    }

    fun loadFakeData(runInProgress :Boolean = false, errorMessage:String = "" ) {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            PhotographerDTO(
                id = 1,
                stageName = "Bob la Menace",
                photoUrl = "https://www.amonteiro.fr/img/fakedata/bob.jpg",
                story = "Ancien agent secret, Bob a troqué ses gadgets pour un appareil photo après une mission qui a mal tourné. Il traque désormais les instants volés plutôt que les espions.",
                portfolio = listOf(
                    "https://picsum.photos/1",
                    "https://picsum.photos/2",
                    "https://picsum.photos/3"
                )
            ),
            PhotographerDTO(
                id = 2,
                stageName = "Jean-Claude Flash",
                photoUrl = "https://www.amonteiro.fr/img/fakedata.com/jc.jpg",
                story = "Ancien champion de rodéo, il s’est reconverti en photographe après une chute mémorable. Maintenant, il dompte la lumière comme un vrai cow-boy.",
                portfolio = listOf(
                    "https://picsum.photos/407",
                    "https://picsum.photos/125",
                    "https://picsum.photos/549"
                )
            )
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }
}