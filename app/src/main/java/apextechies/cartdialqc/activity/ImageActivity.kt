package apextechies.cartdialqc.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import apextechies.cartdialqc.adapter.ImagesAdapter
import java.io.File
import java.util.ArrayList
import apextechies.cartdialqc.R
import apextechies.cartdialqc.R.id.recycler_view
import apextechies.cartdialqc.api.Download_web
import apextechies.cartdialqc.api.OnTaskCompleted
import apextechies.cartdialqc.api.Utilz
import apextechies.cartdialqc.api.WebServices
import apextechies.cartdialqc.common.ConstantValue
import apextechies.cartdialqc.common.MultipartUtility
import apextechies.cartdialqc.easyphotopicker.DefaultCallback
import apextechies.cartdialqc.easyphotopicker.EasyImage
import kotlinx.android.synthetic.main.activity_image.*
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import android.os.AsyncTask.execute



class ImageActivity: AppCompatActivity() {

    private val PHOTOS_KEY = "easy_image_photos_list"

    protected var galleryButton: View? = null

    private var imagesAdapter: ImagesAdapter? = null

    private var photos = ArrayList<File>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        Nammu.init(this)
        galleryButton = findViewById(R.id.gallery_button)

        if (savedInstanceState != null) {
            photos = savedInstanceState.getSerializable(PHOTOS_KEY) as ArrayList<File>
        }

        imagesAdapter = ImagesAdapter(this, photos)
        recycler_view.setLayoutManager(GridLayoutManager(this, 3))
        recycler_view.setHasFixedSize(true)
        recycler_view.setAdapter(imagesAdapter)

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCallback {
                override fun permissionGranted() {
                    //Nothing, this sample saves to Public gallery so it needs permission
                }

                override fun permissionRefused() {
                    finish()
                }
            })
        }

        EasyImage.configuration(this)
                .setImagesFolderName("EasyImage sample")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(true)

        checkGalleryAppAvailability()


        gallery_button.setOnClickListener(View.OnClickListener {
            EasyImage.openGallery(this@ImageActivity, 0)
        })


        camera_button.setOnClickListener(View.OnClickListener { EasyImage.openCameraForImage(this@ImageActivity, 0) })
        submit_button.setOnClickListener {
            /*startActivity(Intent(this@ImageActivity, MainActivity::class.java))
            finish()*/

           var web =  Download_web(this, object : OnTaskCompleted {
               override fun onTaskCompleted(response: String) {

                   if (response.length>0){

                   }
               }
           })
            web.setReqType(true)
            web.setImageUrl(photos[0], "img")
            web.execute(WebServices.UPLOADIMAGE)

        }

    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PHOTOS_KEY, photos)
    }

    private fun checkGalleryAppAvailability() {
        if (!EasyImage.canDeviceHandleGallery(this)) {
            //Device has no app that handles gallery intent
            galleryButton!!.setVisibility(View.GONE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
           override fun onImagePickerError(e: Exception, source: EasyImage.ImageSource, type: Int) {
                //Some error handling
                e.printStackTrace()
            }

            override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                onPhotosReturned(imageFiles)
            }

            override fun onCanceled(source: EasyImage.ImageSource, type: Int) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source === EasyImage.ImageSource.CAMERA_IMAGE) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@ImageActivity)
                    if (photoFile != null) photoFile!!.delete()
                }
            }
        })
    }

    private fun onPhotosReturned(returnedPhotos: List<File>) {
        photos.addAll(returnedPhotos)
        imagesAdapter!!.notifyDataSetChanged()
        recycler_view.scrollToPosition(photos.size - 1)
    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this)
        super.onDestroy()
    }
}

