package `in`.thelosergeek.projectapp.Models

import java.util.*

data class ModelPost(
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var postDescription: String? = null,
    val time: Date = Date()
)
//){
////    constructor(postDescription: String) :this("","","", Date())
//    constructor(): this("","","","",Date())
//}