package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.Models.ModelPost
import `in`.thelosergeek.projectapp.Models.PostViewModel
import `in`.thelosergeek.projectapp.R
import android.R.attr
import android.R.attr.name
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.internal.ActivityLifecycleObserver.of
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_post.*
import java.util.EnumSet.of


class NewPostActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var photo: String
    lateinit var email: String
    lateinit var uid: String

    lateinit var firebaseFirestore: FirebaseFirestore

    val authorization by lazy { FirebaseAuth.getInstance() }

    private  lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)

        viewModel.result.observe(this, Observer {
            val message = if(it== null)
            {
                Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this,"Not Added",Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show()

        })


        btn_post.setOnClickListener {
            val post = postEdtv.text.toString().trim()

            if(post.isEmpty()){
                postEdtv.error = getString(R.string.error_field)
                return@setOnClickListener

            }
            val description = ModelPost()
            description.postDescription = post

            viewModel.AddPost(description)

        }
    }

}






        /*uploading: created model class and the below line*/



//        btn_post.setOnClickListener {
//
//            val description:String = postEdtv.text.toString()
//
//            /*till here*/
////            uploadData(description)
//        }
//    }

//    private fun uploadData(description: String) {
//
//        val timeStamp = System.currentTimeMillis().toString()
//        val filePathAndName = "Postspost_$timeStamp"
//
//        val hashMap: HashMap<Any, String> = HashMap()
//        hashMap["uid"] = uid
//        hashMap["uName"] = name
//        hashMap["uEmail"] = email
//        hashMap["uDp"] = photo
//        hashMap["pDescription"] = description
//        hashMap["pTime"] = timeStamp
//
//        val ref = FirebaseDatabase.getInstance().getReference("Posts")
//        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener {
//            Toast.makeText(this@HomeActivity, "Post Published", Toast.LENGTH_SHORT).show()
//            post_description.setText("")
//            post_image.setImageURI(null)
//            image_uri = null
//        }.addOnFailureListener { e ->
//            progressDialog.dismiss()
//            Toast.makeText(this@HomeActivity, "" + e.message, Toast.LENGTH_SHORT).show()
//        }
//
//    }
//}