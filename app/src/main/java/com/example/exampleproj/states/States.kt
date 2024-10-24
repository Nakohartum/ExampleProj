package com.example.exampleproj.states

import com.example.exampleproj.data.DrinkData

data class NonAlcoholicDrinksState(
    var result: List<DrinkData> = listOf(),
    var error: String? = null
)
data class AlcoholicDrinksState(
    var result: List<DrinkData> = listOf(),
    var error: String? = null
)

data class DrinkState(
    var drink: DrinkData? = null,
    var error: String? = null
)