package com.example.bookreports.profile


import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreports.BuildConfig
import com.example.bookreports.R
import com.example.bookreports.databinding.FragmentProfileBinding
import com.example.bookreports.utils.MainViewModel
import com.theartofdev.edmodo.cropper.CropImage
import es.dmoral.toasty.Toasty
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import kotlin.Throws
import kotlin.collections.HashMap
import kotlin.reflect.jvm.internal.impl.load.java.lazy.descriptors.DeclaredMemberIndex


// 汪汪筆記 https://hackmd.io/HnBnR0xOTs2BpXGCYMJSSg
//https://www.youtube.com/watch?v=dbSAIuyHInY&ab_channel=AnandLearningsAnandLearnings

//contentProvider: https://ithelp.ithome.com.tw/articles/10227700

//放在slack的課程: https://github.com/delaroy/UploadMedia/tree/kotlin-imageupload
//https://www.youtube.com/watch?v=ZVeMb9UnVb4&ab_channel=DelaroyStudios


//new https://www.youtube.com/watch?v=QT9l5sdkZyI&t=2546s&ab_channel=TinCoder

class ProfileFragment : Fragment() {


    lateinit var binding: FragmentProfileBinding
    lateinit var userCommentAdapter: UserCommentAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var imageView: ImageView
    lateinit var mRecyclerview: RecyclerView
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var progressTV: TextView




    //0611
    companion object{
        const val PICTUREFROMGALLERY = 1001
        const val PICTUREFROMCAMERA = 1002
    }

    private lateinit var imgFromGallery: ImageView
    private lateinit var imageFromApi: ImageView



    //kai
    var imageUri: Uri? = null



    //0608
//    private var mMediaUri: Uri? = null
//    private var fileUri: Uri? = null
//    private var mediaPath: String? = null
//    private var mImageFileLocation = ""
//    private var postPath: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this
        imageView = binding.imageProfile
        progressBar = binding.pbUpload
        progressTV = binding.tvUploadPleaseWait
        binding.viewmodel = viewModel

        initAdapter()



        if (viewModel.accountProfile.value == null) {
            Toasty.info(requireActivity(), "尚未登入會員，5秒後為您轉到會員登入畫面", Toast.LENGTH_SHORT, true).show()
            Handler(Looper.getMainLooper()).postDelayed({
                this.findNavController()
                    .navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            }, 5000)
        } else {

        }


        viewModel.fetchApiError.observe(viewLifecycleOwner, Observer {
            if (it == "Failed to connect to /52.196.162.105:80") {
                binding.textView4.text = "_"
                binding.textView5.text = "_"
                binding.tvName.text = "_"
            }
        })


        viewModel.accountProfile.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                Log.d("Testing", "observe account $it")
                userCommentAdapter.submitList(it.user?.comments)
            } else {
                Log.d("Testing", "observe null $it")
                userCommentAdapter.submitList(listOf())
            }
        })

        val checkSelfPermission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.CAMERA
        )

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            permissionPhoto()
        } else {
        }


        binding.btnUpload.setOnClickListener {

        }


        binding.btnPickImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICTUREFROMGALLERY)

        }

        binding.btnTakePicture.setOnClickListener {

        }


//        getState()

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PICTUREFROMGALLERY){
            if(data != null){
                imageUri = data?.data
                imageView.setImageURI(imageUri)
                val body = compressImage(imageUri)
                viewModel.uploadImage(body)
//                val tmpFile = getFilePathFromContentUri(requireActivity(),imageUri)
//                val realPath = Uri.fromFile(tmpFile)
//                val file = File(realPath.path)
//                var requetFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//                var body: MultipartBody.Part = MultipartBody.Part.createFormData("image",file.name,requetFile)
//                viewModel.uploadImage(body)


//
//                Timber.d("imgeUri = $imageUri")
//                Timber.d("tmpFile = $tmpFile")
//                Timber.d("realPath = $realPath")
//                Timber.d("file = $file")
//                Timber.d("requestFile = $requetFile")
//                Timber.d("body = $body")

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


    private fun compressImage(imageUri: Uri?): MultipartBody.Part?{
        return if( imageUri != null){
            val imageStream = requireActivity().contentResolver.openInputStream(imageUri)
            val degree = getImageDegree(requireActivity(),imageUri)
            val bmp = BitmapFactory.decodeStream(imageStream)
            val rotateBmp = rotateBitmpa(bmp,degree)

            val baos: ByteArrayOutputStream? = ByteArrayOutputStream()
            rotateBmp.compress(Bitmap.CompressFormat.JPEG,15,baos)
            val requestFile = RequestBody.create("image/jpe".toMediaTypeOrNull(),baos!!.toByteArray())
            MultipartBody.Part.createFormData("image","sample.jpg",requestFile)

        }else null
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getImageDegree(context: Context,uri: Uri): Int{
        var degree = 0
        val input = context.contentResolver.openInputStream(uri)!!
        val exifInterface = ExifInterface(input)
        val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        )
        when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> degree =90
            ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
        }
        return degree


    }


    private fun rotateBitmpa(bitmap: Bitmap,degree: Int): Bitmap{
        val w = bitmap.width
        val h = bitmap.height
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        return Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true)
    }




    private fun initAdapter() {
        mRecyclerview = binding.recyclerviewAlreadyComment
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerview.layoutManager = linearLayoutManager

        userCommentAdapter = UserCommentAdapter(UserCommentAdapter.OnClickListener {

        })
        mRecyclerview.adapter = userCommentAdapter


    }

    private fun getFilePathFromContentUri(context: Context,contentUri: Uri?):File{
        val fileExtension = getFileExtension(context,contentUri)
        val fileName = "temp_file" + if(fileExtension != null)".$fileExtension" else ""

        val tmpFile = File(context.cacheDir,fileName)
        tmpFile.createNewFile()

        var oStream: FileOutputStream? = null
        var inputStream: InputStream? = null

        try {
            oStream = FileOutputStream(tmpFile)
            inputStream = contentUri?.let { context.contentResolver.openInputStream(it) }

            inputStream?.let { copy(inputStream,oStream) }
            oStream.flush()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            inputStream?.close()
            oStream?.close()
        }
        return tmpFile
    }


    private fun getFileExtension(context: Context,uri: Uri?): String?{
        val fileType: String? = uri?.let { context.contentResolver.getType(it) }
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream,target: OutputStream){
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0){
                    target.write(buf,0,length)
                }
    }







}

































//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_FROM_GALLERY) {
//            if(resultCode == Activity.RESULT_OK){
//            if (data != null) {
//                Timber.d("enterActivityResult ")
//                //這裡拿到的Uri是一種經轉換過的抽象路徑 不是真實檔案位置
//                val selectedImage = data.data
//                Timber.d("selectedImage $selectedImage")
//                //原本是DATA，改成 _ID  [Ljava.lang.String;@7b7f475 代表  需要擷取的資料欄位，以下顯示只要_ID的欄位，所以不能印出其他資訊
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                Timber.d("filePathColum $filePathColumn")
//                //設置條件，相當於 SQL的 where
////                val sel = MediaStore.Images.Media._ID + "=?"
//
//                //每一欄位的集合
//                val cursor = requireActivity().contentResolver.query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        filePathColumn,
//                        null,
//                        null,
//                        null
//                )
//                Timber.d("cursor $cursor")
//                //確認cursor 不等於 null
//                if (BuildConfig.DEBUG && cursor == null) {
//                    error("Assertion failed")
//                }else{
//                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
//                    Timber.d("filePathColum[0] ${filePathColumn[0]}")
//                    Timber.d("columnIndex $columnIndex")
//
//                    //將指標轉到第一筆資料
//                    if(cursor != null) {
//                        if (cursor.moveToFirst()) {
//
//                            mediaPath = columnIndex?.let { cursor?.getString(it) }
//                        }
//                        Timber.d("mediaPath $mediaPath")
//                        //原本丟的是_ID 當然不行阿，他要的是Path
//                        imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
//                        //關閉指標，並釋放資源
//                        cursor?.close()
//
//                        postPath = mediaPath
//                    }
//                }
//            }
//            }else{
//
//            }
//        } else if (requestCode == CAMERA_PIC_REQUEST) {
//            if (Build.VERSION.SDK_INT > 21) {
//                Glide.with(this).load(mImageFileLocation).into(imageView)
//                postPath = mImageFileLocation
//            } else {
//                Glide.with(this).load(fileUri).into(imageView)
//                postPath = fileUri!!.path
//            }
//        } else if (requestCode == Activity.RESULT_CANCELED) {
//            Toast.makeText(requireActivity(), "Sorry, there was an error!", Toast.LENGTH_LONG)
//                .show()
//        }
//    }


//    private fun captureImage() {
//        if (Build.VERSION.SDK_INT > 21) {
//            val callCameraApplicationIntent = Intent(Intent.ACTION_GET_CONTENT)
//            callCameraApplicationIntent.setType("image/*")
//            var photoFile: File? = null
//            try {
//                photoFile = createImageFile()
//                Timber.d("photoFile : $photoFile")
//            } catch (e: IOException) {
//                Logger.getAnonymousLogger().info("Exception error in generating the file")
//                e.printStackTrace()
//            }
//
//            val outputUri = photoFile?.let {
//                FileProvider.getUriForFile(
//                    requireActivity(),
//                     "com.example.bookreports.provider",
//                    it
//                )
//            }
//
//            Timber.d("outputUri: $outputUri")
//
//            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
//            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            Logger.getAnonymousLogger().info("Calling the camera App by intent")
//
//            startActivityForResult(callCameraApplicationIntent, REQUEST_FROM_GALLERY)
//        } else {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
//            Timber.d("enter else getOutputMediaFileUri")
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//
//            startActivityForResult(intent, REQUEST_FROM_GALLERY)
//
//        }
//    }
//
//    @Throws(IOException::class)
//    internal fun createImageFile(): File {
//        Logger.getAnonymousLogger().info("Generating the image - method started")
//
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
//        val imageFileName = "IMAGE_" + timeStamp
//        val storageDirectory =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
//        Logger.getAnonymousLogger().info("Storage directory set")
//
//        if (!storageDirectory.exists()) storageDirectory.mkdir()
//
//        val image = File(storageDirectory, imageFileName + ".jpg")
//        Logger.getAnonymousLogger().info("File naem and path set")
//
//        mImageFileLocation = image.absolutePath
//
//        return image
//
//    }

//    fun getOutputMediaFileUri(type: Int): Uri {
//        return Uri.fromFile(getOutputMediaFile(type))
//    }


//    private fun uploadFile() {
//
//        Timber.d("upload path:$postPath ")
//        if (postPath == null || postPath == "") {
//            return
//        } else {
//
//            val file = File(postPath)
//
//            Timber.d("file $file")
//            val requestFile = RequestBody.create("*/*".toMediaTypeOrNull(), file)
//            var body: MultipartBody.Part =
//                MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//            viewModel.uploadImage(body)
//
//
//        }
//    }

//    companion object {
//
//        const val REQUEST_FROM_CAMERA = 1001
//        const val REQUEST_FROM_GALLERY = 1002
//        const val CAMERA_PIC_REQUEST = 1111
//
//        val IMAGE_DIRECTORY_NAME = "Android File Upload"
//
//
//        private fun getOutputMediaFile(type: Int): File? {
//            val mediaStorageDir = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                IMAGE_DIRECTORY_NAME)
//
//            if(!mediaStorageDir.exists()){
//                if(!mediaStorageDir.mkdir()){
//                   Timber.d("Failed create$IMAGE_DIRECTORY_NAME directory")
//                    return null
//                }
//            }
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(Date())
//            val mediaFile: File
//            if(type == MEDIA_TYPE_IMAGE){
//                mediaFile = File(mediaStorageDir.path + File.separator + "IMG_"+".jpg")
//            }else{
//                return null
//            }
//            return mediaFile
//        }


//    }
//
//    private fun getState(){
//
//        val state = Environment.getExternalStorageState()
//        if (Environment.MEDIA_MOUNTED.equals(state)){
//            Timber.d("envi${Environment.MEDIA_MOUNTED.equals(state)}")
//        } else{
//            Timber.d("envi${Environment.MEDIA_MOUNTED.equals(state)}")
//        }
//    }


//}















