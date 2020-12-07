package `in`.thelosergeek.projectapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_received_layout.view.*
import kotlinx.android.synthetic.main.item_date.view.*

class ChatAdapter (private val list: MutableList<ChatEvent>, private val mCurrentUser: String):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        val inflate = { layout: Int ->
            LayoutInflater.from(parent.context).inflate(layout,parent,false)
        }
        return when(viewType){
            TEXT_MESSAGE_RECEIVED ->
            {
                MessageViewHolder(inflate(R.layout.chat_received_layout))
            }
            TEXT_MESSAGE_SENT ->
            {
                MessageViewHolder(inflate(R.layout.chat_sent_message))
            }
            DATE_HEADER ->
            {
                DateViewHolder(inflate(R.layout.item_date))
            }
            else -> MessageViewHolder(inflate(R.layout.chat_received_layout))

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when (val item = list[position]){
           is DateHeader -> {
               holder.itemView.textView.text = item.date
           }
           is MessageModel -> {
                   holder.itemView.content.text = item.msg
                   holder.itemView.time.text = item.sentAt.formatAsTime() //**//

           }

       }
    }

    override fun getItemCount(): Int = list.size


    override fun getItemViewType(position: Int): Int {
        return when(val event = list[position]){
            is MessageModel ->
            {
               if(event.senderId == mCurrentUser){
                   TEXT_MESSAGE_SENT
               }
                else{
                   TEXT_MESSAGE_RECEIVED
               }
            }
            is DateHeader -> DATE_HEADER
            else -> UNSUPPORTED
        }
    }


    class DateViewHolder(view:View): RecyclerView.ViewHolder(view)

    class MessageViewHolder(view: View): RecyclerView.ViewHolder(view)

    companion object{
        private const val UNSUPPORTED =-1
        private const val TEXT_MESSAGE_RECEIVED = 0
        private const val TEXT_MESSAGE_SENT = 1
        private const val DATE_HEADER = 2
    }
}