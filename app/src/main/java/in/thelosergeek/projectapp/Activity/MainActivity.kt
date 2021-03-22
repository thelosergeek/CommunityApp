package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.Adapters.ViewPagerAdapter
import `in`.thelosergeek.projectapp.R
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var profilename: TextView
    lateinit var profileimage: ImageView
    lateinit var toogle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    private val mCurrentUid: String by lazy { FirebaseAuth.getInstance().uid!! }
    private var name: String? = null
    private var photo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
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
                R.id.marketPlace -> {
                    startActivity(Intent(this, MarketPlaceActivity::class.java))
                }
            }
            true

        }

        /*Initializing Header View*/
        profilename = navigationView.getHeaderView(0).findViewById(R.id.navigation_name) as TextView
        profileimage = navigationView.getHeaderView(0).findViewById(R.id.navigation_image) as ImageView
        val docRef = FirebaseFirestore.getInstance().collection("users").document(mCurrentUid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.get("name") as String
                    photo = document.get("imageUrl") as String

                    /*Putting header view values from Firestore*/
                    profilename.text = name
                    Picasso.get().load(photo).into(profileimage)
                } else {
                }

            }
            .addOnFailureListener {
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
        }
    }
}