package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.Models.PostViewModel
import `in`.thelosergeek.projectapp.Models.User
import `in`.thelosergeek.projectapp.R
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import kotlinx.android.synthetic.main.activity_new_post.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class NewPostActivity : AppCompatActivity() {


    //    private val name: String by lazy {
//        intent.getStringExtra(mNAME)!!
//    }
    private val mCurrentUid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }


    //    private val db: FirebaseDatabase by lazy {
//        FirebaseDatabase.getInstance()
//    }
//
//    lateinit var currentUser: User
    lateinit var currentUser: User
    lateinit var name: String

    //    lateinit var email: String
    lateinit var photo: String


    lateinit var userEmail: String
    lateinit var firebaseFirestore: FirebaseFirestore

    val authorization by lazy { FirebaseAuth.getInstance() }

    private lateinit var viewModel: PostViewModel

    val TAG: String = "firestore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(IosEmojiProvider())
        setContentView(R.layout.activity_new_post)

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
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    name = document.get("name") as String
//                    email = document.get("")
                    photo = document.get("imageUrl") as String

//                    Log.i(fbname, "fbname");


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


        btn_post.setOnClickListener {
            val description = postEdtv.text.toString().trim()
//            val uuids: String = mCurrentUid
//            val autjorname = name
//            // val imaafe = image


            if (description.isEmpty()) {
                postEdtv.error = getString(R.string.error_field)
                return@setOnClickListener

            }
//            val description = ModelPost()
//            description.postDescription = post

//            Log.i(mCurrentUid,"currentuid");

//            val userId = ModelPost()
//            userId.uid = mCurrentUid


            uploadData(description);
            postEdtv.setText("")


//            val userid = ModelPost()
//            userid.id = uuids
//
//            val nameuser = ModelPost()
//            nameuser.name = autjorname


//            val imageuser = ModelPost()
//            imageuser.image = imaafe


//            viewModel.AddPost(description)


        }
    }

    private fun uploadData(description: String) {

        val df: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val tf: DateFormat = SimpleDateFormat("HH:mm:ss")
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
        hashMap["email"] = userEmail
        hashMap["postDescription"] = description
        hashMap["postDate"] = datePost
        hashMap["postTime"] = timePost
        val ref = FirebaseDatabase.getInstance().getReference("posts")
        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener {
            Toast.makeText(this, "Post Published", Toast.LENGTH_SHORT).show()


//                post_description.setText("")
//                post_image.setImageURI(null)
//                image_uri = null
        }.addOnFailureListener { e ->
            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
        }
    }
}




