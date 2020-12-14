package `in`.thelosergeek.projectapp.ViewHolder

import `in`.thelosergeek.projectapp.Models.ModelInbox
import `in`.thelosergeek.projectapp.R
import `in`.thelosergeek.projectapp.Utility.formatAsListItem
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.singleuserlayout.view.*

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: ModelInbox, onClick: (name: String, photo: String, id: String) -> Unit) =
        with(itemView) {
            messagecountTV.isVisible = item.count > 0
            messagecountTV.text = item.count.toString()
            timeTV.text = item.time.formatAsListItem(context)

            nameTV.text = item.name
            skillsTV.text = item.message
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.defaultavatar)
                .error(R.drawable.defaultavatar)
                .into(profile_image)
            setOnClickListener {
                onClick.invoke(item.name, item.image, item.sender)
            }
        }
}