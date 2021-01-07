package `in`.thelosergeek.projectapp.Adapters

import `in`.thelosergeek.projectapp.Models.ModelPost
import `in`.thelosergeek.projectapp.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


//class PostAdapter() : RecyclerView.Adapter<ViewHolder>() {
//
//    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//
//    }
//
//    private val posts = ArrayList<ModelPost>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= RecyclerView.ViewHolder(
//        return LayoutInflater.from(parent.context).inflate(R.layout.singlepostlayout,parent,false))
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.itemView.(posts[position])
//    }
//
//    override fun getItemCount() = posts.size
//
//}

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
        //val email: String? = uploads.get(position).email
        val name: String? = list.get(position)?.name
        ////curentuid, name, email, timestamp, description
        //val currentuid: String? = uploads.get(position).uid
        val description: String? = list?.get(position)?.postDescription
        val date: String? = list.get(position).postDate
        val image: String? = list.get(position).image
        val time: String? = list.get(position).postTime

//        val upload: Model = postList.get()
        // val modelPost: ModelPost = postList.get(position)
//        holder.timeTV.setText(time)
        holder.textViewName.setText(name)
        holder.tvpost.setText(description)
        holder.dateTV.setText(date)
        holder.timeTV.setText(time)

        Picasso.get().load(image).placeholder(R.drawable.defaultavatarastronaut).into(holder.imageView)

//        holder.tvpost.setText(modelPost.postDescription)
//        Log.i(description,"description")
//        Log.i(email,"email")
        //   val uDP: String = uploads[position].getpImage()
        //  holder.textViewName.setText(upload.getpID()) //
//        holder.textViewPost.setText(upload.postDescription)
//        try {
//            Picasso.get().load(uDP).placeholder(R.drawable.ic_baseline_insert_drive_file_24)
//                .into(holder.imageView)
//        } catch (e: Exception) {
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        var profileImage = itemView.findViewById<View>(R.id.TV_profile) as ImageView
        var textViewName: TextView = itemView.findViewById<View>(R.id.TV_name) as TextView
        var tvpost: TextView = itemView.findViewById<View>(R.id.tv_post) as TextView

        var dateTV: TextView = itemView.findViewById<View>(R.id.DateTV) as TextView
        var imageView: ImageView = itemView.findViewById<View>(R.id.TV_profile) as ImageView

        var timeTV: TextView = itemView.findViewById(R.id.TimeTV)
        init {
            //            imageView = itemView.findViewById<View>(R.id.document_image) as ImageView
//            textViewDate = itemView.findViewById(R.id.document_time)
        }
    }
}