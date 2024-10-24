package com.example.exampleproj.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.exampleproj.data.DrinkData
import com.example.exampleproj.viewmodel.ALCOHOLIC_DRINKS
import com.example.exampleproj.viewmodel.DrinksViewModel
import com.example.exampleproj.viewmodel.NON_ALCOHOLIC_DRINKS

@Composable
fun MainScreen(drinksViewModel: DrinksViewModel, navHostController: NavHostController){
    val isLoading by drinksViewModel.isLoading.collectAsState(false)
    val selectedDrinksTab by drinksViewModel.selectedDrinksTab.collectAsState()
    val nonAlcoholicDrinksState by drinksViewModel.nonAlcoholicDrinksState.collectAsState()
    val alcoholicDrinksState by drinksViewModel.alcoholicDrinksState.collectAsState()

    LaunchedEffect(Unit) {
        drinksViewModel.setSelectedDrinksTab(NON_ALCOHOLIC_DRINKS)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedDrinksTab
        ) {
            DrinkTab("Alcoholic",
                index = ALCOHOLIC_DRINKS,
                selectedIndex = selectedDrinksTab,
                onTabClicked = {
                drinksViewModel.setSelectedDrinksTab(it)
                    drinksViewModel.setSearchDrink("")
            })
            DrinkTab("Non alcoholic",
                index = NON_ALCOHOLIC_DRINKS,
                selectedIndex = selectedDrinksTab,
                onTabClicked = {
                    drinksViewModel.setSelectedDrinksTab(it)
                    drinksViewModel.setSearchDrink("")
                })
        }
        if (isLoading){
            LoadingScreen()
        }
        else if (nonAlcoholicDrinksState.error != null || alcoholicDrinksState.error != null){
            when(selectedDrinksTab){
                1 -> ErrorScreen(alcoholicDrinksState.error!!)
                0 -> ErrorScreen(nonAlcoholicDrinksState.error!!)
            }
        }
        else{
            when(selectedDrinksTab){
                NON_ALCOHOLIC_DRINKS -> DrinksGrid(
                    viewModel = drinksViewModel,
                    nonAlcoholicDrinksState.result,
                    onClick = {
                        navHostController.navigate(Graph.DRINK_SCREEN)
                    }
                )
                ALCOHOLIC_DRINKS -> DrinksGrid(
                    viewModel = drinksViewModel,
                    alcoholicDrinksState.result,
                    onClick = {
                        navHostController.navigate(Graph.DRINK_SCREEN)
                    }
                )
            }
        }
    }
}

@Composable
fun DrinkTab(type: String, index: Int, selectedIndex: Int, onTabClicked: (Int) -> Unit) {
    Tab(
        onClick = {onTabClicked(index)},
        selected = index == selectedIndex
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = type,
        )
    }
}


@Composable
fun DrinksGrid(viewModel: DrinksViewModel,result: List<DrinkData>, onClick: () -> Unit) {
    val finalList by viewModel.finalList.collectAsState()
    val searchQuery by viewModel.searchDrink.collectAsState()
    Search(
        viewModel = viewModel,
        onSearchChanged = {
            viewModel.setFinalList(result)
        })
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        if (finalList.isEmpty() && searchQuery.isEmpty()){
            items(result){
                DrinkCell(it){
                    viewModel.setDrinkId(it.idDrink)
                    onClick()
                }
            }
        }
        else if(searchQuery.isNotEmpty() && finalList.isEmpty()){
            item{
                ErrorScreen("Nothing found")
            }
        }
        else{
            items(finalList){
                DrinkCell(it){
                    viewModel.setDrinkId(it.idDrink)
                    onClick()
                }
            }
        }
    }
}

@Composable
fun Search(
    viewModel: DrinksViewModel,
    onSearchChanged: () -> Unit
){
    val searchString by viewModel.searchDrink.collectAsState()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            textStyle = TextStyle.Default.copy(fontSize = 24.sp),
            value = searchString,
            onValueChange = {
                viewModel.setSearchDrink(it)
                onSearchChanged()
            }
        )
        HorizontalDivider(thickness = 4.dp)
    }
}

@Composable
fun DrinkCell(drink: DrinkData, onClick: () -> Unit) {
    Box(
        modifier = Modifier.height(300.dp).padding(horizontal = 8.dp)
            .clickable { onClick() }
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.height(200.dp),
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = drink.strDrink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
