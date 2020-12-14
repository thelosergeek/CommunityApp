package `in`.thelosergeek.projectapp.Activity

import `in`.thelosergeek.projectapp.R
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()


        val provider = OAuthProvider.newBuilder("github.com")


        val emailEt: EditText = findViewById(R.id.etEmail)
        val passwordEt: EditText = findViewById(R.id.etPassword)


        //Google Sign in

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        google_signin_button.setSize(SignInButton.SIZE_STANDARD)
        google_signin_button.setOnClickListener{
            signIn()
        }


        //Login Button
        btnlogin.setOnClickListener {
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    this,
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }


        //Github Login Button



        github_login_btn.setOnClickListener{



        }

        //Register Button
        btnregister.setOnClickListener{
            startActivity(Intent(this, RegistraionActivity::class.java))
        }

        //Forgot Password Button
        btnforgotpassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }






    }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acc = completedTask.getResult(ApiException::class.java)
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show()
            FirebaseGoogleAuth(acc)
        } catch (e: ApiException) {
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            FirebaseGoogleAuth(null)
        }
    }

    private fun FirebaseGoogleAuth(acct: GoogleSignInAccount?) {
        if (acct != null) {
            val authCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
            auth.signInWithCredential(authCredential).addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                        val user: FirebaseUser? = auth.getCurrentUser()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "acc failed", Toast.LENGTH_SHORT).show()
        }
    }



}