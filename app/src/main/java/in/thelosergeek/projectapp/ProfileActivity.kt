package `in`.thelosergeek.projectapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.jar.Manifest

class ProfileActivity : AppCompatActivity() {

    val storage by lazy { FirebaseStorage.getInstance() }
    val authorization by lazy { FirebaseAuth.getInstance() }
    lateinit var imageDownloadUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        defaultavatar.setOnClickListener {
            checkStoragePermission()
        }
    }

    private fun checkStoragePermission() {

        if((checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            && (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
            val permissionRead = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWrite = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            requestPermissions(
                permissionRead, 100
            )
            requestPermissions(
                permissionWrite, 101
            )
        }
        else
        {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1001)
        {
            data?.data?.let {
                defaultavatar.setImageURI(it)
                uploadImageToFirebase(it)
            }
        }
    }

    private fun uploadImageToFirebase(it: Uri) {

        btnsave.isEnabled = false
        val ref = storage.reference.child("profilepicture/" + authorization.uid.toString())
        val uploadTask = ref.putFile(it)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {

                    throw it

                }
            }
            return@Continuation ref.downloadUrl

        }).addOnCompleteListener { task ->
            btnsave.isEnabled = true
            if (task.isSuccessful){
            imageDownloadUrl = task.result.toString()

        }
        else
        {

        }
    }.addOnFailureListener {

        }
    }
}