package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.R
import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import kotlinx.android.synthetic.main.activity_new_post.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class NewPostActivity : AppCompatActivity() {

    var image_uri: Uri? = null

    private val mCurrentUid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }

    lateinit var name: String
    lateinit var photo: String
    lateinit var userEmail: String


    private val CAMERA_CODE = 100
    private val STORAGE_CODE = 200

    private val IMAGE_PICK_CAMERA = 300
    private val IMAGE_PICK_GALLERY = 400

    private val PDF_PICK_CODE = 500


    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

    var post_image: ImageView? = null
    var progressDialog: ProgressDialog? = null

    private var pdffilepath: Uri? = null

    //private lateinit var viewModel: PostViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(IosEmojiProvider())
        setContentView(R.layout.activity_new_post)
        post_image = findViewById(R.id.etImage);
        cameraPermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        progressDialog = ProgressDialog(this);

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userEmail = user.email.toString()
        } else {
            // No user is signed in
        }


        val docRef = FirebaseFirestore.getInstance().collection("users").document(mCurrentUid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.get("name") as String
                    photo = document.get("imageUrl") as String
                } else {

                }

            }
            .addOnFailureListener {

            }

//        val emojiPopup = EmojiPopup.Builder.fromRootView(rootView2).build(postEdtv)
//        emojibtn.setOnClickListener {
//            emojiPopup.toggle()
//        }

//        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
//
//        viewModel.result.observe(this, Observer {
////            val message = if (it == null) {
////                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
////            } else {
////                Toast.makeText(this, "Not Added", Toast.LENGTH_SHORT).show()
////            }
//            Toast.makeText(this, "Posted", Toast.LENGTH_SHORT).show()
//
//
//        })

        btn_attach.setOnClickListener {
            ImagePick()
        }

        btn_attach_document.setOnClickListener {
            DocumentPick()
        }


        btn_post.setOnClickListener {
            val description = postEdtv.text.toString().trim()


//            if (description.isEmpty()) {
//                postEdtv.error = getString(R.string.error_field)
//                return@setOnClickListener
//
//            }
            if (image_uri == null && pdffilepath == null) {
                uploadData(description, "noImage", "noPDF");
            } else if (pdffilepath == null) {
                uploadData(description, image_uri.toString(), "noPDF");
            } else {
                uploadData(description, "noImage", pdffilepath.toString())
            }

            // uploadData(description);
            postEdtv.setText("")


//            viewModel.AddPost(description)


        }
    }

    private fun DocumentPick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName())
            );
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        intent = Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PDF_PICK_CODE);
    }


    private fun uploadData(description: String, uri: String, pdfuri: String) {

        progressDialog?.setMessage("Uploading...");
        progressDialog?.show();

        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val tf: DateFormat = SimpleDateFormat("HH:mm")
        val date: String = df.format(Calendar.getInstance().getTime())
        val time: String = tf.format(Calendar.getInstance().time)
        val timeStamp = System.currentTimeMillis().toString()
        val datePost = date

        val timePost = time
        if (!uri.equals("noImage")) {
            val reference: StorageReference =
                FirebaseStorage.getInstance().getReference().child(timeStamp)
            reference.putFile(Uri.parse(uri)).addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot>() { taskSnapshot ->

//                    lateinit var taskSnapShot: UploadTask.TaskSnapshot
                    val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);

                    var downloadUri: String = uriTask.getResult().toString();
                    if (uriTask.isSuccessful()) {
                        val hashMap: HashMap<Any, String> = HashMap()
                        hashMap["uid"] =
                            mCurrentUid  //curentuid, name, email, timestamp, description
                        hashMap["id"] = timeStamp
                        hashMap["name"] = name
                        hashMap["image"] = photo
                        hashMap["email"] = userEmail
                        hashMap["postDescription"] = description
                        hashMap["postDate"] = datePost
                        hashMap["postImage"] = downloadUri
                        hashMap["postTime"] = timePost
                        hashMap["postPDF"] = "noPDF"

                        val ref = FirebaseDatabase.getInstance().getReference("posts")
                        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener {
                            Toast.makeText(this, "Post Published", Toast.LENGTH_SHORT).show()

                            progressDialog?.dismiss();
                            post_image?.setOnClickListener { image_uri = null }
                            postEdtv.setText("")
                            post_image?.setImageURI(null)
                            image_uri = null
                        }.addOnFailureListener { e ->
                            progressDialog?.dismiss();
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            ).addOnFailureListener(OnFailureListener {
                progressDialog?.dismiss();
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//                }
            })
        } else if (!pdfuri.equals("noPDF")) {
            val reference: StorageReference =
                FirebaseStorage.getInstance().getReference().child(timeStamp)
            reference.putFile(Uri.parse(pdfuri)).addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot>() { taskSnapshot ->
                    val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);

                    var downloadUri: String = uriTask.getResult().toString();
                    if (uriTask.isSuccessful()) {
                        val hashMap: HashMap<Any, String> = HashMap()
                        hashMap["uid"] =
                            mCurrentUid  //curentuid, name, email, timestamp, description
                        hashMap["id"] = timeStamp
                        hashMap["name"] = name
                        hashMap["image"] = photo
                        hashMap["email"] = userEmail
                        hashMap["postDescription"] = description
                        hashMap["postDate"] = datePost
                        hashMap["postImage"] = "noImage";
                        hashMap["postTime"] = timePost
                        hashMap["postPDF"] = downloadUri

                        val ref = FirebaseDatabase.getInstance().getReference("posts")
                        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener {
                            Toast.makeText(this, "Post Published", Toast.LENGTH_SHORT).show()

                            progressDialog?.dismiss();
                            //post_image?.setOnClickListener { image_uri = null }
                            postEdtv.setText("")
                           //post_image?.setImageURI(null)
                           // image_uri = null
                            pdfView.fromUri(pdffilepath)
                            pdfView.fromAsset(downloadUri)

                        }.addOnFailureListener { e ->
                            progressDialog?.dismiss();
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            ).addOnFailureListener(OnFailureListener {
                progressDialog?.dismiss();
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
//                }
            })
        } else {
            val hashMap: HashMap<Any, String> = HashMap()
            hashMap["uid"] = mCurrentUid  //curentuid, name, email, timestamp, description
            hashMap["id"] = timeStamp
            hashMap["name"] = name
            hashMap["image"] = photo
            hashMap["email"] = userEmail
            hashMap["postDescription"] = description
            hashMap["postDate"] = datePost
            hashMap["postImage"] = "noImage";
            hashMap["postTime"] = timePost
            hashMap["postPDF"] = "noPDF";
            val ref = FirebaseDatabase.getInstance().getReference("posts")
            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener {
                Toast.makeText(this, "Post Published", Toast.LENGTH_SHORT).show()

                progressDialog?.dismiss();
                //post_image?.setOnClickListener { image_uri = null }
                postEdtv.setText("")
                post_image?.setImageURI(null)
                image_uri = null
            }.addOnFailureListener { e ->
                progressDialog?.dismiss();

                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ImagePick() {
        val options = arrayOf("\uD83D\uDCF7  Camera", " \uD83D\uDDBC️ Gallery")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Image From ")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickFromCamera()
                }
            }
            if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickFromGallery()
                }
            }
        })
        builder.create().show()
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY)
    }

    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Pick")
        image_uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, storagePermission, CAMERA_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            CAMERA_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(this, "Both are necessary", Toast.LENGTH_SHORT).show()
                    }
                } else {
                }
            }
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(
                            this,
                            "Storage Permissions are necessary",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY) {
                image_uri = data?.data
                post_image?.setImageURI(image_uri)
            } else if (requestCode == IMAGE_PICK_CAMERA) {
                post_image?.setImageURI(image_uri)
            } else if (requestCode == PDF_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
                if (data.getData() != null) {
                    //uploading the file
                    pdffilepath = data?.data

                    {

                    }
                }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}




