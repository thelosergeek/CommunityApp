package `in`.thelosergeek.projectapp

import java.util.*

data class ModelInbox(
    val message: String,
    var sender: String,
    var name: String,
    var image: String,
    val time: Date = Date(),
    var count: Int = 0,

)
{
    constructor(): this("","","","",Date(),0)
}