package com.hanifikorkmaz.foodbook.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanifikorkmaz.foodbook.model.Food

//Veritabanına birden fazla tablo ekleyebileceğimiz için entities şeklinde kullandık.
@Database(entities = [Food::class], version = 1)
abstract class FoodDb : RoomDatabase() {
    abstract fun FoodDao(): FoodDao
}