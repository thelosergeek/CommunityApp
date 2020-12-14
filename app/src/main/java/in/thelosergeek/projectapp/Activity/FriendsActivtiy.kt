package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.ViewHolder.EmptyViewHolder
import `in`.thelosergeek.projectapp.Models.User
import `in`.thelosergeek.projectapp.R
import `in`.thelosergeek.projectapp.ViewHolder.UserViewHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_friends_activtiy.*
import java.lang.Exception

private const val NORMAL_VIEW_Type = 2
private const val DELETE_VIEW_Type = 1

class FriendsActivtiy : AppCompatActivity() {

    lateinit var mAdapter: FirestorePagingAdapter<User, RecyclerView.ViewHolder>
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    val database by lazy {
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name", Query.Direction.ASCENDING)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_activtiy)

        setUpAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FriendsActivtiy)
            adapter = mAdapter
        }
    }
    ////////////////////////////////////////////

    private fun setUpAdapter() {
        val config = PagedList.Config.Builder()
            .setPrefetchDistance(2)
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()

        val options = FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(this) //viewLifecycleOwner
            .setQuery(database, config, User::class.java)
            .build()
////////////////////////////////////////
        mAdapter = object : FirestorePagingAdapter<User, RecyclerView.ViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return when (viewType) {
                    NORMAL_VIEW_Type -> UserViewHolder(
                        layoutInflater.inflate(
                            R.layout.singleuserlayout,
                            parent,
                            false
                        )
                    )
                    else -> EmptyViewHolder(
                        layoutInflater.inflate(
                            R.layout.empty_view,
                            parent,
                            false
                        )
                    )
                }

            }

            override fun getItemViewType(position: Int): Int {
                val item = getItem(position)?.toObject(User::class.java)
                return if (auth.uid == item!!.uid) {
                    DELETE_VIEW_Type
                } else {
                    NORMAL_VIEW_Type
                }
            }

            override fun onBindViewHolder(
                holder: RecyclerView.ViewHolder,
                position: Int,
                model: User
            ) {
                if (holder is UserViewHolder) {
                    holder.bind(user = model) { name: String, photo: String, id: String ->
                        val intent = Intent(this@FriendsActivtiy, ChatActivity::class.java)
                        intent.putExtra(UID, id)
                        intent.putExtra(NAME, name)

                        intent.putExtra(IMAGE, photo)

                        startActivity(intent)


                    }

                }
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                super.onLoadingStateChanged(state)
            }

            override fun onError(e: Exception) {
                super.onError(e)
            }


        }
    }
}