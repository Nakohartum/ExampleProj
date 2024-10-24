package com.example.exampleproj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exampleproj.data.DrinkData
import com.example.exampleproj.repositories.DrinksRepository
import com.example.exampleproj.states.AlcoholicDrinksState
import com.example.exampleproj.states.DrinkState
import com.example.exampleproj.states.NonAlcoholicDrinksState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val NON_ALCOHOLIC_DRINKS = 1
const val ALCOHOLIC_DRINKS = 0

class DrinksViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading get()= _isLoading.asStateFlow()
    private val _selectedDrinksTab = MutableStateFlow(0)
    val selectedDrinksTab get() = _selectedDrinksTab.asStateFlow()

    private val _drinksRepository = DrinksRepository()

    private val _nonAlcoholicDrinksState = MutableStateFlow(NonAlcoholicDrinksState())
    val nonAlcoholicDrinksState get()= _nonAlcoholicDrinksState.asStateFlow()

    private val _alcoholicDrinksState = MutableStateFlow(AlcoholicDrinksState())
    val alcoholicDrinksState get()= _alcoholicDrinksState.asStateFlow()

    private val _searchDrink = MutableStateFlow("")
    val searchDrink = _searchDrink.asStateFlow()
    private val _finalList = MutableStateFlow(listOf<DrinkData>())
    val finalList = _finalList.asStateFlow()

    private val _drinkId = MutableStateFlow("")
    val drinkId = _drinkId.asStateFlow()
    private val _drinkState = MutableStateFlow(DrinkState())
    val drinkState = _drinkState.asStateFlow()

    fun setDrinkId(id: String){
        _drinkId.value = id;
    }

    fun setFinalList(list: List<DrinkData>) {
        _finalList.value = list.filter { drink ->
            drink.strDrink.startsWith(_searchDrink.value, true)
        }
    }

    fun setSearchDrink(text: String){
        _searchDrink.value = text
        _finalList.value = listOf()
    }

    fun setSelectedDrinksTab(index: Int){
        
        _selectedDrinksTab.value = index
        if (_selectedDrinksTab.value == NON_ALCOHOLIC_DRINKS){
            if (_nonAlcoholicDrinksState.value.result.isEmpty()){
                getAllNonAlcoholic()
            }
        }
        else if (_selectedDrinksTab.value == ALCOHOLIC_DRINKS){
            if (_alcoholicDrinksState.value.result.isEmpty()){
                getAllAlcoholic()
            }
        }
    }

    fun getDrinkById(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = _drinksRepository.getDrinkById(_drinkId.value)
                _drinkState.value =
                    _drinkState.value.copy(
                        drink = response.drinks[0]
                    )
            } catch (e: Exception) {
                _drinkState.value =
                    _drinkState.value.copy(
                        error = e.message
                    )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getAllAlcoholic() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = _drinksRepository.getAllAlcoholic()
                _alcoholicDrinksState.value =
                    _alcoholicDrinksState.value.copy(
                        result = response.drinks
                    )
            } catch (e: Exception) {
                _alcoholicDrinksState.value =
                    _alcoholicDrinksState.value.copy(
                        error = e.message
                    )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getAllNonAlcoholic(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = _drinksRepository.getAllNonAlcoholic()
                _nonAlcoholicDrinksState.value =
                    _nonAlcoholicDrinksState.value.copy(
                        result = response.drinks
                    )
            }
            catch (e: Exception){
                _nonAlcoholicDrinksState.value =
                    _nonAlcoholicDrinksState.value.copy(
                        error = e.message
                    )
            }finally {
                _isLoading.value = false
            }
        }
    }
}