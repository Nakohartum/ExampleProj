package com.example.exampleproj.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.exampleproj.data.DrinkData
import com.example.exampleproj.viewmodel.DrinksViewModel

@Composable
fun DrinkInfo(viewModel: DrinksViewModel){
    val drinkState by viewModel.drinkState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDrinkById()
    }

    if (isLoading){
        LoadingScreen()
    }
    else if(drinkState.error != null){
        ErrorScreen(drinkState.error!!)
    }
    else if(drinkState.drink != null){
        DrinkScreen(drinkState.drink!!)
    }

}

@Composable
fun DrinkScreen(drinkData: DrinkData){
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = drinkData.strDrinkThumb,
            contentDescription = drinkData.strDrink
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Category: ${drinkData.strCategory}"
        )
        Text(
            text = "Type: ${drinkData.strAlcoholic}"
        )
        Text(
            text = "IBA: ${drinkData.strIBA}"
        )
        Text(
            text = "Glass Type: ${drinkData.strGlass}"
        )

        LazyColumn {
            item {
                Text(
                    text = drinkData.strInstructions
                )
            }
        }
    }
}