package com.amonteiro.a03_kmp_mprolead_g1.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerDTO
import com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.screens.PictureRowItem

@Composable
actual fun PictureGallery(modifier: Modifier, list: List<PhotographerDTO>, navHostController: NavHostController?) {
    //Permet de remplacer très facilement le RecyclerView. LazyRow existe aussi
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(list.size) {
            PictureRowItem(data = list[it], onClick = {
                navHostController?.navigate(Routes.DetailRoute(list[it].id))
            })
        }
    }
}