package com.hanifikorkmaz.foodbook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.hanifikorkmaz.foodbook.databinding.FragmentDetailPageBinding
import com.hanifikorkmaz.foodbook.model.Food
import com.hanifikorkmaz.foodbook.roomdb.FoodDao
import com.hanifikorkmaz.foodbook.roomdb.FoodDb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import androidx.navigation.findNavController
import androidx.core.graphics.scale

class DetailPage : Fragment() {

    private var _binding: FragmentDetailPageBinding? = null

    private val binding get() = _binding!!

    private var newOrOldControl: String?= null

    private var foodId: Int?= null

    //İzin İsteme Olayı İçin Oluşturulan Değişken
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    //Galeriye Gitmek İçin Oluşturulan Değişken
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    //Galeriden Seçilen Resmin Yolunu Verir.
    private var selectedImage: Uri?= null

    //Uri'yi BitMap'e Dönüştürüp Resmi Değiştireceğiz.
    private var selectedBitMap: Bitmap?= null

    //Veritabanı
    private lateinit var foodDb: FoodDb

    //Sorgular İçin Dao Oluşturma
    private lateinit var  foodDao: FoodDao

    //Veritabanında yapılan sorgularda hafıza düzeni için kullanıyoruz.
    private val mDisposable= CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        //Veritabanını uyguluyoruz.
        foodDb= Room.databaseBuilder(requireContext(),FoodDb::class.java,"FoodDataBase").build()

        //Dao'yu uyguluyoruz
        foodDao=foodDb.FoodDao()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentDetailPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
             newOrOldControl= DetailPageArgs.fromBundle(it).NewOrOldControl
             foodId= DetailPageArgs.fromBundle(it).FoodId
        }

        if (newOrOldControl.equals("new")){
            //Butona Tıklanma Durumu

            binding.HeadText.text="AddFood"

            //Buton Aktifliğini Ayarlama Kısmı
            binding.DeleteButton.isEnabled= false
            binding.SaveButton.isEnabled=true

            binding.imageView.setOnClickListener { selectImage(it) }

            binding.SaveButton.setOnClickListener { save(it) }

        }
        else{
            //Listedeki Elemana Tıklama Durumu

            binding.SaveButton.isEnabled=false
            binding.DeleteButton.isEnabled=true
            binding.imageView.isEnabled=false
            getById()

        }
    }

    //Galeriden görsel seçmek için oluşturulan fonksiyon.
    fun selectImage(view: View){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //READ_MEDIA_IMAGES(33 ve üzeri versiyonlarda galeriye gitmek için istenilecek olan izin)

            //İlk olarak kullanıcının galeriye erişim izin verip vermediğini kontrol etmem lazım.

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){

                //İzin verilmediğinde tekrardan izin istemeliyiz.

                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)){

                    //Burada tekrar izin isteme olayına yukarıdaki kodla Android karar ver eğer bu if true dönerse izini tekrar istemeliyiz.

                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){

                            //İzin İste
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }
                else{

                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            else{
                //İzin verilmiş, direk olarak galeriye git.

                val intentToGallery=
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }


        }
        else{
            //READ_EXTERNAL_STORAGE(33 altı versiyonlarda galeriye gitmek için istenilecek izin)

            //İlk olarak kullanıcının galeriye erişim izin verip vermediğini kontrol etmem lazım.

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                //İzin verilmediğinde tekrardan izin istemeliyiz.

                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){

                    //Burada tekrar izin isteme olayına yukarıdaki kodla Android karar verir eğer bu if true dönerse izini tekrar istemeliyiz.

                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){

                            //İzin İste
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }.show()
                }
                else{

                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else{
                //İzin verilmiş, direk olarak galeriye git.

                val intentToGallery=
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }
    }

    private fun registerLauncher(){

        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            //Kullanıcının galeriden resim seçip seçmediğini kontrol ediyoruz.
            if (result.resultCode== AppCompatActivity.RESULT_OK){

                val intentFromResult= result.data

                //Mekanik bir olay olduğundan seçilen resim boşta gelebilir bu yüzden kontrol ediyoruz.
                if(intentFromResult != null){

                    selectedImage= intentFromResult.data  //Seçilen resmin yolunu verir.(dir/media/hanfi.png)

                    try {

                        if (Build.VERSION.SDK_INT>=28) {
                            //28 üstü versiyonlarda uri, bitmap dönüşümü.
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedImage!!)
                            selectedBitMap = ImageDecoder.decodeBitmap(source)

                            binding.imageView.setImageBitmap(selectedBitMap)
                        }
                        else{
                            //28 altı versiyonlarda uri, bitmap dönüşümü
                            selectedBitMap= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)

                            binding.imageView.setImageBitmap(selectedBitMap)
                        }
                    }
                    catch (e: Exception){
                        println(e.localizedMessage)
                    }
                }
            }
        }

        permissionLauncher= registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->

            if(result){
                //İzin verildi, galeriye git.

                val intentToGallery=
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                //İzin verilmedi.
                Toast.makeText(requireContext(),"Permission needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun save(view: View){

        //İlk olarak kullanıcının girdiği değerleri alıyoruz.
        val foodName= binding.editTextText.text.toString()
        val recipe= binding.editTextText2.text.toString()

        //Görsel byte dizisi olarak almamız gerekiyor. Bunun nedeni Entity kısmında görseli ByteArray olarak aldık.

        if(selectedBitMap != null){

            //Bitmapi istediğimiz boyuta getirmek için yazdığımız fonksiyonu kullanuyoruz ve Bitmapi Byte dizisine döndürüyoruz.
            val smallBitmap= scaledDownBitmap(selectedBitMap!!,300)
            val outputStream= ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArrayImage= outputStream.toByteArray()

            //Tabloya koyacağımız değerleri veriyoruz.
            val food= Food(foodName,recipe,byteArrayImage)


            //RxJava Uygulanması(Kodlarımızın arkaplanda çalışmasını sağlıyoruz.)
            mDisposable.add(
                foodDao.insert(food)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseForInsert))

        }
    }

    private fun handleResponseForInsert(){
        //İşlemin sonucunda ne yapılacağını ele alıyoruz.(Ana sayfaya geri dönmesini istiyoruz.)

        val action= DetailPageDirections.actionDetailPageToHomePage()
        requireView().findNavController().navigate(action)

        //Kullanıcıya geri bildirim veririz.
        Toast.makeText(requireContext(),"Food Saved", Toast.LENGTH_LONG).show()

    }

    fun delete(view: View){


    }

    //Id'e göre veri çekmek için oluşturulan fonksiyon.
    private fun getById(){

            mDisposable.add(
                foodDao.findById(foodId!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForGetById)
            )
    }

    private fun handleResponseForGetById(food: Food){
        binding.HeadText.text= food.foodName
        binding.editTextText.setText(food.foodName)
        binding.editTextText2.setText(food.foodRecipe)

        //ByteArray şeklinde olan görselimiz tekrardan Bitmap'e çeviriyoruz.
        val bitMapImage= BitmapFactory.decodeByteArray(food.foodImage,0,food.foodImage!!.size)
        binding.imageView.setImageBitmap(bitMapImage)
    }

    //Resmin boyutunu küçültmek için oluşturulan fonksiyon.
    fun scaledDownBitmap(selectedBitmap: Bitmap, maxSize: Int): Bitmap{

        //İlk olarak güncel genişlik ve yüksekliği alıyoruz.
        var width= selectedBitmap.width
        var height= selectedBitmap.height

        //Güncel Bitmap oranını alıyoruz.
        val bitmapRatio= width.toDouble()/ height.toDouble()

        //Görselimiz yatay mı dikey mi bunu anlamak için kontrol yapıyoruz.
        if(bitmapRatio >= 1){
            //Görsel Yatay
            width= maxSize
            val scaledHeight= width/bitmapRatio
            height= scaledHeight.toInt()
        }
        else{
            //Görsel Dikey
            height=maxSize
            val scaledWidht= height*bitmapRatio
            width= scaledWidht.toInt()
        }

        //Son olarak bulduğumuz yeni değerleri fonksiyonda yerine yazıyoruz.
        return selectedBitmap.scale(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //Uygulama kapanırken verileri temizliyor.
        mDisposable.clear()
    }
}