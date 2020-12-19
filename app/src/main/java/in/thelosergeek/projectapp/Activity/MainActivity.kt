package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.Adapters.ViewPagerAdapter
import `in`.thelosergeek.projectapp.R
import android.widget.ImageView
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var toogle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()


        setUpNavigationDrawer()
        setUpFragments()
//
//        var navigationView = findViewById<View>(R.id.navigationView) as NavigationView
//
//
//        val hView: View = navigationView.inflateHeaderView(R.layout.navigation_header)
//        val imgvw: ImageView = hView.findViewById(R.id.profile_image) as ImageView
//        imgvw.setImageResource(R.drawable.logo)

//        if(auth.currentUser == null){
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }else{
//            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
//        }

    }

    private fun setUpNavigationDrawer() {
        toogle = ActionBarDrawerToggle(this, Drawerlayout, toolbar, R.string.open, R.string.close)
        Drawerlayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
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
        if(toogle.onOptionsItemSelected(item)){
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
        tablayout.getTabAt(1)?.setIcon(R.drawable.feeds)

    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if(currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this, "Already logged in", Toast.LENGTH_LONG).show()
        }
    }
}