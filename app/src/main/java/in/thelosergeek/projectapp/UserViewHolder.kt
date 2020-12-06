package `in`.thelosergeek.projectapp

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.singleuserlayout.view.*

class UserViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){

    fun bind(user: User) = with(itemView){

        messagecountTV.isVisible =  false
        timeTV.isVisible = false

        nameTV.text = user.name
        skillsTV.text = user.skills

        Picasso.get()
            .load(user.thumbImage)
            .placeholder(R.drawable.defaultavatar)
            .error(R.drawable.defaultavatar)
            .into(profile_image)
    }
}