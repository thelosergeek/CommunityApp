package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.R
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_feeds.*

class FeedsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_feeds, container, false)

        val button = view.findViewById<FloatingActionButton>(R.id.add_feeds_button)

        button.setOnClickListener {
            val intent = Intent(activity, NewPostActivity::class.java)
            startActivity(intent)

        }
        return view

    }


}