package parentcrew.com.parentcrew


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth

import mehdi.sakout.fancybuttons.FancyButton
import parentcrew.com.parentcrew.util.UserUtility

class LoginActivity : AppCompatActivity() {

    private lateinit var registrationButton: FancyButton
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var authentication: FirebaseAuth
    private lateinit var authenticationListener: FirebaseAuth.AuthStateListener
    val TAG: String = LoginActivity::class.java.toString()

    private var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authentication = FirebaseAuth.getInstance()


        registrationButton = findViewById(R.id.loginButton) as FancyButton
        usernameEditText = findViewById(R.id.usernameEditText) as EditText
        passwordEditText = findViewById(R.id.passwordEditText) as EditText
        registrationButton.isEnabled = false


        usernameEditText.addTextChangedListener(object : parentcrew.com.parentcrew.util.Validator(usernameEditText) {
            override fun validate(editText: EditText, text: String) {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                registrationButton.isEnabled = !(username.length < 5 ||
                        !username.contains("@") || password.isEmpty())
            }
        })
        passwordEditText.addTextChangedListener(object : parentcrew.com.parentcrew.util.Validator(passwordEditText) {
            override fun validate(editText: EditText, text: String) {
                val password = passwordEditText.text.toString()
                val username = usernameEditText.text.toString()
                registrationButton.isEnabled = password.length >= 8 && !username.isEmpty()
            }
        })

        registrationButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            setAuthentication(username, password)
            val user = authentication.currentUser

            //Log.d(TAG,user!!.uid)
            usernameEditText.setText("")
            passwordEditText.setText("")
        }
    }

    private fun setAuthentication(username: String, password: String) {
        registrationButton.setText(getString(R.string.label_loggingIn))
        authentication.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = authentication.currentUser
                        Toast.makeText(this@LoginActivity, "Athentication Succesful", Toast.LENGTH_SHORT)
                                .show()
                        UserUtility.setUID(this@LoginActivity, user!!.uid)
                        updateUI()
                    } else {
                        Toast.makeText(this@LoginActivity, task.exception!!.message,
                                Toast.LENGTH_SHORT).show()
                        registrationButton.setText(getString(R.string.label_login))
                    }
                }
    }

    private fun updateUI() {
        val user = authentication.currentUser
        if (user!!.displayName != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "Leaving through here!, Main Activity")
        } else {
            val intent = Intent(this@LoginActivity, RegisterProfile::class.java)
            startActivity(intent)
            Log.d(TAG, "Leaving through here!, Register Profile")
        }

    }

    override fun onBackPressed() {
        count++
        if (count == 1) {
            Toast.makeText(this@LoginActivity, "Tap back again to exit", Toast.LENGTH_SHORT).show()
        } else if (count == 2) {
            finish()
        }

    }
}
