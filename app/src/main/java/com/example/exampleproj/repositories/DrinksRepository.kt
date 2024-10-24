package com.example.exampleproj.repositories

import com.example.exampleproj.responses.DrinksResponse
import com.example.exampleproj.service.BaseCaller

class DrinksRepository {
    suspend fun getAllNonAlcoholic(): DrinksResponse{
        return BaseCaller.caller.getAllNonAlcoholic()
    }

    suspend fun getDrinkById(id: String): DrinksResponse{
        return BaseCaller.caller.getDrinkById(id)
    }

    suspend fun getAllAlcoholic(): DrinksResponse{
        return BaseCaller.caller.getAllAlcoholic()
    }
}