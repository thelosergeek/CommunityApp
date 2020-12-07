package `in`.thelosergeek.projectapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.ios.IosEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*


const val UID = "uid"
const val NAME = "name"
const val IMAGE = "thumbImage"


class ChatActivity : AppCompatActivity() {


    private val friendId: String by lazy {
        intent.getStringExtra(UID)!!
    }
    private val name: String by lazy {
        intent.getStringExtra(NAME)!!
    }
    private val image: String by lazy {
        intent.getStringExtra(IMAGE)!!
    }
    private val mCurrentUid: String by lazy {
        FirebaseAuth.getInstance().uid!!
    }
    private val db: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }


    lateinit var currentUser: User

    private val messages = mutableListOf<ChatEvent>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(IosEmojiProvider())
        setContentView(R.layout.activity_chat)

        FirebaseFirestore.getInstance().collection("users").document(mCurrentUid).get()
            .addOnSuccessListener {
                currentUser = it.toObject(User::class.java)!!
            }
        val emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(msgEdtv)
        smileBtn.setOnClickListener {
            emojiPopup.toggle()
        }


        nameTv.text = name
        Picasso.get().load(image).into(userImgView)

        // Returns true when Popup is showing.
        sendBtn.setOnClickListener {
            msgEdtv.text?.let {
                if (it.isNotEmpty()) {
                    sendMessage(it.toString())
                    it.clear()
                }
            }
        }
    }

    private fun sendMessage(msg: String) {
        val id = getMessages(friendId).push().key
        checkNotNull(id) { "Cannot be null" }
        val msgMap = MessageModel(msg, mCurrentUid, id)
        getMessages(friendId).child(id).setValue(msgMap)
        /**/
        updateLastMessage(msgMap)

    }

    private fun updateLastMessage(messageModel: MessageModel) {
        val inboxmap = ModelInbox(
            messageModel.msg,
            friendId,
            name,
            image,
            count = 0
        )

        getInbox(mCurrentUid, friendId).setValue(inboxmap)


        getInbox(friendId, mCurrentUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(ModelInbox::class.java)
                inboxmap.apply {
                    sender = messageModel.senderId
                    name = currentUser.name
                    image = currentUser.thumbImage
                    count = 1
                }

                    if (value?.sender == messageModel.senderId) {
                        inboxmap.count = value.count + 1

                }
                getInbox(friendId, mCurrentUid).setValue(inboxmap)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun alreadyRead(){
        getInbox(friendId,mCurrentUid).child("count").setValue(0)
    }

    private fun readMessages(){
        getMessages(friendId).orderByKey().addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val msg = snapshot.getValue(MessageModel::class.java)!!
                addMessage(msg)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addMessage(msg: MessageModel) {
        val eventBefore =  messages.lastOrNull()
        if(eventBefore != null && eventBefore.sentAt.isSameDayAs(msg.sentAt)){
            messages.
        }
    }

    /**/

    private fun getMessages(friendId: String) = db.reference.child("messages/${getId(friendId)}")


    private fun getInbox(toUser: String, fromUser: String) =
        db.reference.child("chats/$toUser/$fromUser")

    private fun getId(friendId: String): String {
        return if (friendId > mCurrentUid) {
            mCurrentUid + friendId
        } else {
            friendId + mCurrentUid
        }
    }
}