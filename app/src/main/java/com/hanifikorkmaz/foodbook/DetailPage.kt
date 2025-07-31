package com.hanifikorkmaz.foodbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanifikorkmaz.foodbook.databinding.FragmentDetailPageBinding


class DetailPage : Fragment() {

    private var _binding: FragmentDetailPageBinding? = null

    private val binding get() = _binding!!

    private var NewOrOldControl: String?= null

    private var FoodId: Int?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
             NewOrOldControl= DetailPageArgs.fromBundle(it).NewOrOldControl
             FoodId= DetailPageArgs.fromBundle(it).FoodId
        }

        if (NewOrOldControl.equals("new")){
            //Butona Tıklanma Durumu

            binding.HeadText.text="AddFood"

            //Buton Aktifliğini Ayarlama Kısmı
            binding.DeleteButton.isEnabled= false
            binding.SaveButton.isEnabled=true

        }
        else{
            //Listedeki Elemana Tıklama Durumu
            
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}