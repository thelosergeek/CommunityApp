package `in`.thelosergeek.projectapp

import android.Manifest
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_market_place.*
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.activity_new_post.btn_post
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddMarketPlaceActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var email: String
    lateinit var photo: String


    private val STORAGE_CODE = 200
    private val IMAGE_PICK_GALLERY = 400

    lateinit var storagePermission: Array<String>
    var progressDialog: ProgressDialog? = null


    private val mCurrentUid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_market_place)


        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        progressDialog = ProgressDialog(this);

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            email = user.email.toString()
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

        btn_post.setOnClickListener {
            val description = descriptionBox.text.toString().trim()


//            if (description.isEmpty()) {
//                postEdtv.error = getString(R.string.error_field)
//                return@setOnClickListener
//
//            }
//            if (image_uri == null && pdffilepath == null) {
//                uploadData(description, "noImage", "noPDF");
//            } else if (pdffilepath == null) {
//                uploadData(description, image_uri.toString(), "noPDF");
//            } else
//            {
                uploadData(description, "noImage")
            }

            // uploadData(description);
            descriptionBox.setText("")


//            viewModel.AddPost(description)




    }

    private fun uploadData(description: String, s: String) {

        progressDialog?.setMessage("Uploading...");
        progressDialog?.show();

        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val tf: DateFormat = SimpleDateFormat("HH:mm")
        val date: String = df.format(Calendar.getInstance().getTime())
        val time: String = tf.format(Calendar.getInstance().time)
        val timeStamp = System.currentTimeMillis().toString()
        val datePost = date

        val timePost = time


        val hashMap: HashMap<Any, String> = HashMap()
        hashMap["uid"] = mCurrentUid  //curentuid, name, email, timestamp, description
        hashMap["id"] = timeStamp
        hashMap["name"] = name
        hashMap["image"] = photo
        hashMap["email"] = email
        hashMap["postDescription"] = description
        hashMap["postDate"] = datePost
        hashMap["postImage"] = "noImage";
        hashMap["postTime"] = timePost
        hashMap["postPDF"] = "noPDF";
        val ref = FirebaseDatabase.getInstance().getReference("marketplace")
        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener {
            Toast.makeText(this, "Post Published", Toast.LENGTH_SHORT).show()

            progressDialog?.dismiss();
            //post_image?.setOnClickListener { image_uri = null }
            descriptionBox.setText("")
//            post_image?.setImageURI(null)
//            image_uri = null
        }.addOnFailureListener { e ->
            progressDialog?.dismiss();

            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()

        }
    }
}