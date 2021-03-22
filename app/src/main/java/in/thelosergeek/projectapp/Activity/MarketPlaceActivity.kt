package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.AddMarketPlaceActivity
import `in`.thelosergeek.projectapp.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_market_place.*

class MarketPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_place)

        add_sell_button.setOnClickListener{
            startActivity(Intent(this, AddMarketPlaceActivity::class.java))
            finish()
        }
    }
}