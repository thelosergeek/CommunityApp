package `in`.thelosergeek.projectapp.Models

import com.google.firebase.firestore.FieldValue

data class User(
    var name: String,
    val imageUrl: String,
    val thumbImage: String,
    val uid: String,
    val deviceToken: String,
    val skills: String,
    val online: String
    ) {

    constructor() : this("","","","","","","")

    constructor(name: String, imageUrl: String, thumbImage: String, uid: String) : this(
        name, imageUrl, thumbImage, uid, "","", ""
    )
}