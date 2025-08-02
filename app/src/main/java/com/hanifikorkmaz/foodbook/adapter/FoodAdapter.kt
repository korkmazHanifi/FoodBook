package com.hanifikorkmaz.foodbook.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanifikorkmaz.foodbook.databinding.RecycleRowBinding
import com.hanifikorkmaz.foodbook.model.Food

class FoodAdapter(val foodList: List<Food>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(val binding: RecycleRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodViewHolder {
        val recycleRowBinding= RecycleRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FoodViewHolder(recycleRowBinding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(
        holder: FoodViewHolder,
        position: Int
    ) {
        holder.binding.textView3.text=foodList[position].foodName

        val byteArrayImage = foodList[position].foodImage


        //ByteArray olarak aldığımız görseli Bitmap' e çeviriyoruz.
        val bitmapImage= BitmapFactory.decodeByteArray(byteArrayImage,0, byteArrayImage!!.size)
        holder.binding.imageView2.setImageBitmap(bitmapImage)



    }
}