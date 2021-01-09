package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.Adapters.PostAdapter
import `in`.thelosergeek.projectapp.Models.ModelPost
import `in`.thelosergeek.projectapp.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FeedsFragment : Fragment() {
    //  private lateinit var viewModel: PostViewModel

    var firebaseAuth: FirebaseAuth? = null
    var recyclerView: RecyclerView? = null


    lateinit var list: ArrayList<ModelPost>
    fun FeedsFragment() {
        // Required empty public constructor
    }
    private val REQUEST_CODE = 600


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
        val view: View = inflater.inflate(R.layout.fragment_feeds, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.fragment_feeds_RV)


        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.reverseLayout = true

        list = ArrayList()

        recyclerView?.setLayoutManager(linearLayoutManager);

        loadPosts()

        val button = view?.findViewById<FloatingActionButton>(R.id.add_feeds_button)

        if (button != null) {
            button.setOnClickListener {
                val intent = Intent(activity, NewPostActivity::class.java)
                startActivityForResult(intent,REQUEST_CODE)
            }
        }
        return view
    }


    private fun loadPosts() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("posts")
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("RestrictedApi")
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                list.clear();
                for (ds in dataSnapshot.children) {
                    val modelPost: ModelPost? = ds.getValue(ModelPost::class.java)
                    if (modelPost != null) {
                        list.add(modelPost)
                    }
                    recyclerView?.adapter = PostAdapter(context as FragmentActivity?, list)


                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                Toast.makeText(activity, "" + databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

    }
}

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
////            db = FirebaseDatabase.getInstance().reference("posts")
//        firebaseAuth = FirebaseAuth.getInstance()
//        // post =
//
//        val TAG = "MyMessage"
//        postlist = ArrayList()
//        //loadPosts()
//

//        databaseReference = FirebaseDatabase.getInstance().getReference("posts")
//        val query: Query = databaseReference!!.orderByChild("id")
//        query.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
//                for (ds in dataSnapshot.children) {
//                    name = "" + ds.child("postDescription").value
//                    Log.d(TAG, "Post: ${name}")
//                    email = "" + ds.child("email").value
//                    photo = "" + ds.child("image").value
//                }
//            }
//
//            override fun onCancelled(@NonNull databaseError: DatabaseError) {}
//        })
//        tv_post?.text = name
//


//        fragment_feeds_RV.adapter = adapter

// tv_post.text = name
//        viewModel.fetchPosts()
//        viewModel.posts.observe(viewLifecycleOwner, Observer {
//            adapter.setPost(it)
//        })


//    }
//    private fun loadPosts() {
//        val databaseReference = FirebaseDatabase.getInstance().getReference("Posts")
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            @SuppressLint("RestrictedApi")
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                postlist
//                for (ds in dataSnapshot.children) {
//                    val modelHome = ds.getValue(
//                        ModelPost::class.java
//                    )
//                    postlist.add(modelHome)
//                    adapter = PostAdapter(this, postlist)
//                    fragment_feeds_RV.setAdapter(adapter)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
////                Toast.makeText(this,"nn",Toast.LENGTH_SHORT).show();
//            }
//        })
//    }

//    }
//}