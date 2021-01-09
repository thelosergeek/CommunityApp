package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.Adapters.ViewPagerAdapter
import `in`.thelosergeek.projectapp.Models.User
import `in`.thelosergeek.projectapp.R
import android.widget.ImageView
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

const val nUID = "uid"
const val nNAME = "name"
const val nIMAGE = "thumbImage"


class MainActivity : AppCompatActivity() {

    lateinit var toogle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    private val name: String by lazy {
        intent.getStringExtra(nNAME)!!
    }
    private val image: String by lazy {
        intent.getStringExtra(nIMAGE)!!
    }
//    private val mCurrentUid: String by lazy {
//        FirebaseAuth.getInstance().uid!!
//    }
    lateinit var currentUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TAG = "MyMessage"

//        val dbref = FirebaseFirestore.getInstance().collection("users").document(mCurrentUid)
//        dbref.get().addOnSuccessListener { document ->
//            if (document != null) {
////                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                var namesa = document.getString("name").toString()
//                Log.d(TAG, "Name: ${namesa}")
//
//
//            } else {
//
//            }
//        }




        auth = FirebaseAuth.getInstance()
        val headerView: View = navigationView.getHeaderView(0)
        val navUser: TextView = headerView.findViewById(R.id.navigation_name)
      //  navUser.text = currentUser.name
       // Log.d(TAG, "Name: ${mCurrentUid}")


        setUpNavigationDrawer()
        setUpFragments()


    }

    private fun setUpNavigationDrawer() {
        toogle = ActionBarDrawerToggle(this, Drawerlayout, toolbar, R.string.open, R.string.close)
        Drawerlayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.myprofile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.friends -> {
                    startActivity(Intent(this, FriendsActivtiy::class.java))
                }
                R.id.inbox -> {
                    startActivity(Intent(this, InboxActivity::class.java))
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpFragments() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragmnt(ProjectsFragment(), "")
        adapter.addFragmnt(FeedsFragment(), "")
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)

        tablayout.getTabAt(0)?.setIcon(R.drawable.project)
        tablayout.getTabAt(1)?.setIcon(R.drawable.ic_baseline_dynamic_feed_24)

    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
//            Toast.makeText(this, "", Toast.LENGTH_LONG).show()
        }
    }
}