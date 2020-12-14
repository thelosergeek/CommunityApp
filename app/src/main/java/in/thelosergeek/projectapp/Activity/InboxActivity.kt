package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.ViewHolder.ChatViewHolder
import `in`.thelosergeek.projectapp.Models.ModelInbox
import `in`.thelosergeek.projectapp.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.activity_inbox.*


class InboxActivity : AppCompatActivity() {

    private lateinit var mAdapter: FirebaseRecyclerAdapter<ModelInbox, ChatViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val mDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        viewManager = LinearLayoutManager(this)
        setupAdapter()

        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = mAdapter
        }
    }
    /////////////////////////

    private fun setupAdapter() {

        val baseQuery: Query =
            mDatabase.reference.child("chats").child(auth.uid!!)

        val options = FirebaseRecyclerOptions.Builder<ModelInbox>()
            .setLifecycleOwner(this)
            .setQuery(baseQuery, ModelInbox::class.java)
            .build()
        // Instantiate Paging Adapter'

        ///////////////////////////////
        mAdapter = object : FirebaseRecyclerAdapter<ModelInbox, ChatViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
                val inflater = layoutInflater
                return ChatViewHolder(inflater.inflate(R.layout.singleuserlayout, parent, false))
            }

            override fun onBindViewHolder(viewHolder: ChatViewHolder, position: Int, inbox: ModelInbox
            ) {

                viewHolder.bind(inbox) { name: String, photo: String, id: String ->
                    startActivity(
                        ChatActivity.createChatActivity(this@InboxActivity, id, name, photo)
                    )
                }
            }


        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

}