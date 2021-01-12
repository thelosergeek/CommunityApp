package `in`.thelosergeek.projectapp.Models

import java.util.*

data class ModelPost(
    var uid: String? = null,
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var email: String? = null,
    var postDescription: String? = null,
    val postDate: String?= null,
    val postTime: String? = null,
    val postImage: String? = null,
    val postPDF: String? = null,
    val time: Date = Date()

)
 {

    constructor() :
            this("", "", "", "", "","","","","","", Date(0L))

}