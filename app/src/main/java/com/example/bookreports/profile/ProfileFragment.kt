package com.example.bookreports.profile


import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreports.databinding.FragmentProfileBinding
import com.example.bookreports.registerinfo.AccountViewModel
import com.example.bookreports.utils.MainViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.io.*
import java.net.URI


class ProfileFragment : Fragment() {


    lateinit var binding: FragmentProfileBinding
    lateinit var userCommentAdapter: UserCommentAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var imageView: ImageView
    lateinit var mRecyclerview: RecyclerView
    val vmAccount: AccountViewModel by sharedViewModel()
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTV: TextView


    //0611
    companion object {
        const val PICTURE_FROM_GALLERY = 1001
        const val PICTURE_FROM_CAMERA = 1002
    }


    var imageUri: Uri? = null
    var imagePath: String? = null




    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {


        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        imageView = binding.imageProfile
        progressBar = binding.pbUpload
        progressTV = binding.tvUploadPleaseWait
        binding.accountViewModel = vmAccount

        initAdapter()

        vmAccount.profileInfo.observe(viewLifecycleOwner,{
            Glide.with(binding.imageProfile)
                .load(it.user?.image)
                .into(binding.imageProfile)
        })


        if (vmAccount.profileInfo.value == null) {
            Toasty.info(requireActivity(), "尚未登入會員，5秒後為您轉到會員登入畫面", Toast.LENGTH_SHORT, true).show()
            Handler(Looper.getMainLooper()).postDelayed({
                this.findNavController()
                        .navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            }, 5000)
        } else {

        }

//        Timber.d("account ${viewModel.accountProfile.value}")
        vmAccount.profileFail.observe(viewLifecycleOwner, Observer {
            if (it == "error") {
                binding.textView4.text = "_"
                binding.textView5.text = "_"
                binding.tvName.text = "_"
            }
        })



        val checkSelfPermission = ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.CAMERA
        )

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            permissionPhoto()
        }


        binding.btnUpload.setOnClickListener {
            //這下面不是塞 content，而是塞activity或是fragment
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080,1080)
                    .start()
        }

        return binding.root
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                PICTURE_FROM_GALLERY -> {

            if (data != null && data.data != null) {
                imageUri = data.data
                Timber.d("imageUri:$imageUri")
                imagePath = imageUri?.let { getPathFromUri(it) }
                Timber.d("imagePath: $imagePath")
                val file: File = File(imagePath)
                Timber.d("file: $file")
                val requestBody: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                Timber.d("photoContext:$requestBody")
                val multipart: MultipartBody.Part = MultipartBody.Part.createFormData("image",file.name,requestBody)
                Timber.d("multipart:$multipart")
                vmAccount.accountInfo.value?.token?.let {
                    vmAccount.getProfileInfo(vmAccount.accountInfo.value?.user?.id.toString(),
                        it
                    )
                }

            }
                }
                PICTURE_FROM_CAMERA -> {
                    //以下是拿縮圖
//                    val extras = data?.extras
//                    val imageBitmap = extras?.get("data") as Bitmap
//                    imageView.setImageBitmap(imageBitmap)

                    //給新的位置
                    //data.data應該為Null
                    if (imageUri != null){
                        imageView.setImageURI(imageUri)
                        val file: File = File(imagePath)
                        val requestBody:RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val multipart: MultipartBody.Part = MultipartBody.Part.createFormData("image",file.name,requestBody)
                        Timber.d("Mutipart hello")
//                        viewModel.uploadImage(multipart)

                    }
                }
                ImagePicker.REQUEST_CODE ->{

                    imageUri = data?.data
                    Timber.d("imageUri: $imageUri")
                    Timber.d("imagePath: ${imageUri?.path}")
                    val file = File(requireNotNull(imageUri?.path))
//                    val requestBody: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val requestBody: RequestBody = data?.data?.toFile()?.asRequestBody()?:return
                    Timber.d("photoContext:$requestBody")
                    val multipart: MultipartBody.Part = MultipartBody.Part.createFormData("image",file.name,requestBody)
                    Timber.d("multipart:$multipart")
                    vmAccount.uploadImage(multipart)
//                    imageView.setImageURI(imageUri)

                }
            }
        }
    }
    fun permissionPhoto() {
        ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        ), 0
        )
    }


    private fun getPathFromUri(uri: Uri): String{
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = requireActivity().contentResolver.query(uri,projection,null,null,null)
//        val cursor: Cursor? = loader.loadInBackground()
        Timber.d("cursor : $cursor")
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA) ?: null
        Timber.d("column_index: $column_index")
        cursor?.moveToFirst()
        val result: String = column_index?.let { cursor?.getString(it) } ?: ""
        Timber.d("reuslt: $result")
        cursor?.close()
        return result
    }

//    private fun getPathFromCameraUri(context: Context,uri: Uri): String?{
//        val fileName = getFileName(uri)
//        val realPath = uri.path
//        Timber.d("realPath : $realPath")
//        if (!TextUtils.isEmpty(fileName)){
//            val copyFile:File = File(realPath + File.separator + fileName)
//            copy(context,uri,copyFile)
//            return copyFile.absolutePath
//        }
//        return null
//    }
//
//    private fun getFileName(uri:Uri):String?{
//       val path = uri.path
//       var fileName:String? = null
//       val cut: Int? = path?.lastIndexOf('/')
//       if (cut != -1){
//           if (cut != null) {
//               fileName = path.substring(cut + 1)
//           }
//       }
//        return fileName
//    }
//
//    private fun copy(context: Context,uri: Uri, dstFile:File){
//        try {
//            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
//            if(inputStream == null) return
//            val outputStream = FileOutputStream(dstFile)
//
//        }
//    }
    private fun initAdapter() {
        mRecyclerview = binding.recyclerviewAlreadyComment
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerview.layoutManager = linearLayoutManager

        userCommentAdapter = UserCommentAdapter(UserCommentAdapter.OnClickListener {

        })
        mRecyclerview.adapter = userCommentAdapter


    }
}

















