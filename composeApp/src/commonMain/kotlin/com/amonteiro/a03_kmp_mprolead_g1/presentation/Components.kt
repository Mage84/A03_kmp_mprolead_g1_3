package com.amonteiro.a03_kmp_mprolead_g1.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerDTO

@Composable
expect fun PictureGallery(modifier: Modifier = Modifier, list: List<PhotographerDTO>, navHostController: NavHostController?)