package com.hanifikorkmaz.foodbook.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.hanifikorkmaz.foodbook.adapter.FoodAdapter
import com.hanifikorkmaz.foodbook.databinding.FragmentHomePageBinding
import com.hanifikorkmaz.foodbook.model.Food
import com.hanifikorkmaz.foodbook.roomdb.FoodDao
import com.hanifikorkmaz.foodbook.roomdb.FoodDb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomePage : Fragment() {

    private var _binding: FragmentHomePageBinding? = null

    private val binding get() = _binding!!

    //Veritabanı
    private lateinit var foodDb: FoodDb

    //Sorgular İçin Dao Oluşturma
    private lateinit var  foodDao: FoodDao

    //Veritabanında yapılan sorgularda hafıza düzeni için kullanıyoruz.
    private val mDisposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Tanımladığımız veritabanını uyguluyoruz.
        foodDb= Room.databaseBuilder(requireContext(), FoodDb::class.java,"FoodDataBase").build()
        foodDao= foodDb.FoodDao()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { newFood(it) }
        binding.foodRecycleView.layoutManager= LinearLayoutManager(requireContext())
        getAll()
    }

    //Veritabanından veri çekme işlemi
    private fun getAll(){
        mDisposable.add(
            foodDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseForGetAll))
    }
    private fun handleResponseForGetAll(foodList: List<Food>){
        val adapter= FoodAdapter(foodList)
        binding.foodRecycleView.adapter=adapter
    }

    fun newFood(view: View){
        val action= HomePageDirections.actionHomePageToDetailPage("new",0)
        view.findNavController().navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}