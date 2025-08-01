package com.hanifikorkmaz.foodbook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hanifikorkmaz.foodbook.model.Food
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


@Dao
interface FoodDao {

    //Tüm yemekleri listeler.
    @Query("SELECT * FROM Food")
    fun getAll(): Flowable<List<Food>>

    //Id' ye göre yemekleri listeler.
    @Query("SELECT * FROM Food WHERE uuid= :foodId")
    fun findById(foodId: Int): Flowable<Food>

    //Yemek eklemek için kullanılır.
    @Insert
    fun insert(food: Food): Completable

    //Yemek silmek için kullanılır.
    @Delete
    fun delete(food: Food): Completable
}

//Flowable: Eğer yazdığımız sorgu geriye bir değer döndürüyorsa Flowable kullanılır.

//Completable: Eğer yazdığımız sorgu geriye bir değer döndürmüyorsa Completable kullanılır.