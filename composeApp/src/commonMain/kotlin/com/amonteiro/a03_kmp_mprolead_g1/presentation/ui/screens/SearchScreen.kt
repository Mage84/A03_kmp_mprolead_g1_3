package com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.screens

import a03_kmp_mprolead_g1.composeapp.generated.resources.Res
import a03_kmp_mprolead_g1.composeapp.generated.resources.bt_load
import a03_kmp_mprolead_g1.composeapp.generated.resources.error
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerDTO
import com.amonteiro.a03_kmp_mprolead_g1.di.apiModule
import com.amonteiro.a03_kmp_mprolead_g1.di.viewModelModule
import com.amonteiro.a03_kmp_mprolead_g1.presentation.PictureGallery
import com.amonteiro.a03_kmp_mprolead_g1.presentation.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@Preview(showBackground = true, showSystemUi = true, uiMode = 2)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    KoinApplicationPreview(application = {
        modules(viewModelModule, apiModule)
    }) {
        MaterialTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                val mainViewModel = koinViewModel<MainViewModel>()

                SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun SearchScreen(modifier: Modifier = Modifier,
                 mainViewModel: MainViewModel,
                 navHostController : NavHostController? = null
                 ) {


    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        var searchText by remember { mutableStateOf("") }

        SearchBar(text = searchText) {
            searchText = it
        }

        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value.filter {
            it.stageName.contains(searchText, true)
        }


        PictureGallery(modifier = Modifier.weight(1f),
            list = list,
            navHostController = navHostController
            )

        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { searchText = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Clear")
            }
            Button(
                onClick = { mainViewModel.loadPhotographer() },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(Res.string.bt_load))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, text:String,  onValueChange: (String) -> Unit) {

    TextField(
        value = text, //Valeur affichée
        onValueChange = onValueChange, //Nouveau texte entrée
        leadingIcon = { //Image d'icône
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans string.xml, R de votre package com.nom.projet
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        //keyboardActions = KeyboardActions(onSearch = {onSearchAction()}), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@Composable //Composable affichant 1 élément
fun PictureRowItem(modifier: Modifier = Modifier, data: PhotographerDTO, onClick: () -> Unit) {

    var expended by remember { mutableStateOf(false) }


    Row(modifier = modifier.fillMaxWidth()) {

        //Permission Internet nécessaire
        AsyncImage(
            model = data.photoUrl,
            //Pour aller le chercher dans string.xml R de votre package com.nom.projet
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            contentScale = ContentScale.FillWidth,

            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(Res.drawable.error),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),

            onError = { println(it) },
            modifier = Modifier
                .heightIn(max = 100.dp)
                .widthIn(max = 100.dp)
                .clickable(onClick = onClick)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    expended = !expended
                }


        ) {
            Text(text = data.stageName, fontSize = 20.sp, color = MaterialTheme.colorScheme.tertiary)
            Text(
                text = if (expended) data.story else (data.story.take(20) + "..."), fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}