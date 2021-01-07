package `in`.thelosergeek.projectapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log

class PostViewModel :ViewModel(){

    private val dbPost = FirebaseDatabase.getInstance().getReference("posts")

    private val _posts = MutableLiveData<List<ModelPost>>()
    val posts : LiveData<List<ModelPost>>
    get() = _posts

    private  val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
    get() = _result

    var name: String? = null

    val TAG = "PostViewModel"


    fun AddPost(modelPost: ModelPost){
        modelPost.id = dbPost.push().key
        dbPost.child(modelPost.id!!).setValue(modelPost)
            .addOnCompleteListener{
                if(it.isSuccessful)
                {
                    _result.value = null
                }
                else
                {
                    _result.value = it.exception
                }
            }

    }

    fun fetchPosts(){
        dbPost.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val posts = mutableListOf<ModelPost>()
                        for (postSnapshot in snapshot.children){
                            val post = postSnapshot.getValue(ModelPost::class.java)
                            name = "" + postSnapshot.child("postDescription").value

                            Log.d(TAG, "Post: ${name}")

                            post?.id = postSnapshot.key
                            posts?.let { posts.add(ModelPost()) } /**/
                        }

                        _posts.value = posts
                    }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
}