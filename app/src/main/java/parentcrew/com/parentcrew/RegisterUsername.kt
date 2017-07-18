package parentcrew.com.parentcrew


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import mehdi.sakout.fancybuttons.FancyButton
import parentcrew.com.parentcrew.util.UserUtility
import parentcrew.com.parentcrew.util.Validator


class RegisterUsername : AppCompatActivity() {

    private var count = 0
    private lateinit var authentication: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailError: TextView
    private lateinit var passwordError: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        authentication = FirebaseAuth.getInstance()
        val loginButton: FancyButton = findViewById(R.id.loginButton) as FancyButton
        emailEditText = findViewById(R.id.loginUsername) as EditText
        passwordEditText = findViewById(R.id.loginPassword) as EditText
        emailError = findViewById(R.id.emailError) as TextView
        passwordError = findViewById(R.id.passwordError) as TextView

        emailError.isEnabled = false
        passwordError.isEnabled = false
        loginButton.isEnabled = false

        emailEditText.addTextChangedListener(object : Validator(emailEditText) {
            override fun validate(editText: EditText, text: String) {
                val pass = passwordEditText.text.toString()
                loginButton.isEnabled = text.length > 4 && text.contains("@") && pass.length > 4
            }
        })
        passwordEditText.addTextChangedListener(object : Validator(passwordEditText) {
            override fun validate(editText: EditText, text: String) {
                loginButton.isEnabled = text.length > 4
            }
        })

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            createAccount(email, password)

        }
    }

    override fun onBackPressed() {
        count++
        if (count == 1) {
            Toast.makeText(this, getString(R.string.exit_message), Toast.LENGTH_SHORT).show()
        } else if (count == 2) {
            finish()
        }
    }

    private fun createAccount(email: String, password: String) {
        authentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@RegisterUsername, "Created", Toast.LENGTH_SHORT).show()
                        updateUI()
                    } else {
                        Toast.makeText(this@RegisterUsername, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun updateUI() {
        val uid = authentication.currentUser!!.uid
        UserUtility.setUID(this@RegisterUsername, uid)
        val intent = Intent(this@RegisterUsername, RegisterProfile::class.java)
        startActivity(intent)
    }

}

