package com.amonteiro.a03_kmp_mprolead_g1.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.screens.PhotographerScreen
import com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.screens.SearchScreen
import com.amonteiro.a03_kmp_mprolead_g1.presentation.viewmodel.MainViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

class Routes {
    @kotlinx.serialization.Serializable
    data object SearchRoute

    //les paramètres ne peuvent être que des types de base(String, Int, Double...)
    @Serializable
    data class DetailRoute(val id: Int)
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navHostController : NavHostController = rememberNavController()
    //viewModel() en dehors de NavHost lie à l'Activité donc partagé entre les écrans
    //viewModel() dans le NavHost lié à la stack d'écran. Une instance par stack d'écran
    val mainViewModel : MainViewModel = koinViewModel<MainViewModel>()

    //Import version avec Composable
    NavHost(
        navController = navHostController,
        startDestination = Routes.SearchRoute,
        modifier = modifier) {
        //Route 1 vers notre SearchScreen
        composable<Routes.SearchRoute> {

            //Si créé ici, il sera propre à cet instance de l'écran
            //val mainViewModel : MainViewModel = viewModel()

            //on peut passer le navHostController à un écran s'il déclenche des navigations
            SearchScreen( mainViewModel = mainViewModel, navHostController = navHostController )
        }

        //Route 2 vers un écran de détail
        composable<Routes.DetailRoute> {
            val detailRoute = it.toRoute<Routes.DetailRoute>()
            val photographer = mainViewModel.dataList.collectAsStateWithLifecycle().value.first { it.id == detailRoute.id }

            PhotographerScreen(photographer = photographer)
        }
    }
}