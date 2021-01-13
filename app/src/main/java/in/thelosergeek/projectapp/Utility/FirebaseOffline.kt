package `in`.thelosergeek.projectapp.Utility

import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase

class FirebaseOffline : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}