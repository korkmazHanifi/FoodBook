package com.hanifikorkmaz.foodbook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hanifikorkmaz.foodbook.model.Food




@Dao
interface FoodDao {

    //Tüm yemekleri listeler.
    @Query("SELECT * FROM Food")
    fun getAll(): List<Food>

    //Id' ye göre yemekleri listeler.
    @Query("SELECT * FROM Food WHERE uuid= :foodId")
    fun findById(foodId: Int): Food

    //Yemek eklemek için kullanılır.
    @Insert
    fun insert(food: Food)

    //Yemek silmek için kullanılır.
    @Delete
    fun delete(food: Food)
}