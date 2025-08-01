package com.hanifikorkmaz.foodbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(

    @ColumnInfo("foodName")
    val foodName: String?,

    @ColumnInfo("foodRecipe")
    val foodRecipe: String?,

    @ColumnInfo("foodImage")
    val foodImage: ByteArray?
){
    //Birincil amahtarın otomatik olarak atanmasını sağlar.
    @PrimaryKey(autoGenerate = true)
    var uuid: Int= 0
}
