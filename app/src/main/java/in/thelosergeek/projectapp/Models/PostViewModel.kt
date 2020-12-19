package `in`.thelosergeek.projectapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class PostViewModel :ViewModel(){

    private  val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
    get() = _result


    fun AddPost(modelPost: ModelPost){
        val dbPost = FirebaseDatabase.getInstance().getReference("posts")
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
}