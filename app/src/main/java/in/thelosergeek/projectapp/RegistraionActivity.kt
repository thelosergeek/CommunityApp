package `in`.thelosergeek.projectapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_registraion.*


class RegistraionActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
//    private lateinit var emailre: EditText
//    private lateinit var passwordEt: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registraion)


        auth = FirebaseAuth.getInstance()
        val emailre:EditText = findViewById(R.id.emailTextFieldReg)
        val passwordEt:EditText = findViewById(R.id.passwordTextFieldReg)


        btnregister.setOnClickListener{
            var email: String = emailre.text.toString()
            var password: String = passwordEt.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }



    }
}