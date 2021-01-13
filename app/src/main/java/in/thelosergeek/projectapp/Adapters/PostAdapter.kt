package `in`.thelosergeek.projectapp.Adapters

import `in`.thelosergeek.projectapp.Activity.FeedsFragment
import `in`.thelosergeek.projectapp.Models.ModelPost
import `in`.thelosergeek.projectapp.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class PostAdapter(context: FragmentActivity?, var list: ArrayList<ModelPost>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    var context: Context? = null
    //var postList: List<ModelPost>? = null


    fun PostAdapter(context: Context?, postList: List<ModelPost>) {
//        this.postList = postList
        this.context = context!!
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.singlepostlayout,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val name: String? = list.get(position)?.name
        val description: String? = list?.get(position)?.postDescription
        val date: String? = list.get(position).postDate
        val image: String? = list.get(position).image
        val time: String? = list.get(position).postTime
        val postimage: String? = list.get(position).postImage
        val pdf: String? = list.get(position).postPDF

        holder.textViewName.setText(name)
        holder.tvpost.setText(description)
        holder.dateTV.setText(date)
        holder.timeTV.setText(time)


        if (pdf.equals("noPDF")) {
            holder.pdfbuttonn.visibility = View.GONE
        } else {
            holder.pdfbuttonn.setText(pdf)


        }





        Picasso.get().load(image).placeholder(R.drawable.defaultavatarastronaut)
            .into(holder.imageView)

        if (postimage.equals("noImage")) {
            holder.postpic.visibility = View.GONE
        } else {
            try {
                Picasso.get().load(postimage).into(holder.postpic)
            } catch (e: Exception) {
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView = itemView.findViewById<View>(R.id.TV_name) as TextView
        var tvpost: TextView = itemView.findViewById<View>(R.id.tv_post) as TextView

        var dateTV: TextView = itemView.findViewById<View>(R.id.DateTV) as TextView
        var imageView: ImageView = itemView.findViewById<View>(R.id.TV_profile) as ImageView

        var timeTV: TextView = itemView.findViewById(R.id.TimeTV)

        var postpic: ImageView = itemView.findViewById(R.id.post_attach)

        var pdfbuttonn: Button = itemView.findViewById(R.id.btn_open_pdf)
    }
}