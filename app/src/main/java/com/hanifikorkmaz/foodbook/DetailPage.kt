package com.hanifikorkmaz.foodbook

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hanifikorkmaz.foodbook.databinding.FragmentDetailPageBinding


class DetailPage : Fragment() {

    private var _binding: FragmentDetailPageBinding? = null

    private val binding get() = _binding!!

    private var NewOrOldControl: String?= null

    private var FoodId: Int?= null

    //İzin İsteme Olayı İçin Oluşturulan Değişken
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    //Galeriye Gitmek İçin Oluşturulan Değişken
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    //Galeriden Seçilen Resmin Yolunu Verir.
    private var selectedImage: Uri?= null

    //Uri'yi BitMap'e Dönüştürüp Resmi Değiştireceğiz.
    private var selectedBitMap: Bitmap?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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

            binding.imageView.setOnClickListener { SelectImage(it) }

        }
        else{
            //Listedeki Elemana Tıklama Durumu

        }
    }

    //Galeriden görsel seçmek için oluşturulan fonksiyon.
    fun SelectImage(view: View){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //READ_MEDIA_IMAGES(33 ve üzeri versiyonlarda galeriye gitmek için istenilecek olan izin)

            //İlk olarak kullanıcının galeriye erişim izin verip vermediğini kontrol etmem lazım.

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){

                //İzin verilmediğinde tekrardan izin istemeliyiz.

                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)){

                    //Burada tekrar izin isteme olayına yukarıdaki kodla Android karar ver eğer bu if true dönerse izini tekrar istemeliyiz.

                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener{

                            //İzin İste

                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                        }).show()
                }
                else{

                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            else{
                //İzin verilmiş, direk olarak galeriye git.

                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }


        }
        else{
            //READ_EXTERNAL_STORAGE(33 altı versiyonlarda galeriye gitmek için istenilecek izin)

            //İlk olarak kullanıcının galeriye erişim izin verip vermediğini kontrol etmem lazım.

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                //İzin verilmediğinde tekrardan izin istemeliyiz.

                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){

                    //Burada tekrar izin isteme olayına yukarıdaki kodla Android karar ver eğer bu if true dönerse izini tekrar istemeliyiz.

                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener{

                            //İzin İste

                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }).show()
                }
                else{

                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else{
                //İzin verilmiş, direk olarak galeriye git.

                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }
    }

    private fun registerLauncher(){

        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

            //Kullanıcının galeriden resim seçip seçmediğini kontrol ediyoruz.
            if (result.resultCode== AppCompatActivity.RESULT_OK){

                val intentFromResult= result.data

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
                            //28 altı versiyonlardauri, bitmap dönüşümü
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

                val intentToGallery= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                //İzin verilmedi.
                Toast.makeText(requireContext(),"Permission needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun Save(view: View){


    }

    fun Delete(view: View){


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}