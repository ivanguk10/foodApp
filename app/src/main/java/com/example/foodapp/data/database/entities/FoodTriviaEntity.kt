package com.example.foodapp.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.models.FoodTrivia
import com.example.foodapp.util.Constants.Companion.FOOD_TRIVIA_TABLE

@Entity(tableName = FOOD_TRIVIA_TABLE)
data class FoodTriviaEntity(

    @Embedded
    var foodTrivia: FoodTrivia
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}