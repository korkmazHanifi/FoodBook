package com.hanifikorkmaz.foodbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanifikorkmaz.foodbook.databinding.RecycleRowBinding
import com.hanifikorkmaz.foodbook.model.Food
import com.hanifikorkmaz.foodbook.view.HomePageDirections
import androidx.navigation.findNavController

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
        holder.itemView.setOnClickListener {
            val action= HomePageDirections.actionHomePageToDetailPage("old",foodList[position].uuid)
            it.findNavController().navigate(action)
        }
    }
}