package parentcrew.com.parentcrew

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mehdi.sakout.fancybuttons.FancyButton
import parentcrew.com.parentcrew.util.UserUtility

class WelcomeActivity : AppCompatActivity() {

    private lateinit var authentication: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    private lateinit var callBackManager: CallbackManager
    val TAG: String = WelcomeActivity::class.java.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val typeFace = Typeface.createFromAsset(assets, "pacifico-regular.ttf")
        val labelTitle = findViewById(R.id.labelTitle) as TextView
        labelTitle.typeface = typeFace
        authentication = FirebaseAuth.getInstance()
        callBackManager = CallbackManager.Factory.create()

        initLoginScreen()

        val registrationButton = findViewById(R.id.loginButton) as FancyButton
        val loginButton = findViewById(R.id.registerButton) as FancyButton

        registrationButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val intent = Intent(this, RegisterUsername::class.java)
            startActivity(intent)
        }

        val fbLoginButton = findViewById(R.id.facebookLoginButton) as LoginButton

        fbLoginButton.setReadPermissions("email", "public_profile")

        fbLoginButton.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken);
                Log.d(TAG, result.toString())

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, error!!.message.toString())
            }

        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        authentication.signInWithCredential(credential)
                .addOnCompleteListener {
                    task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, task.exception.toString())
                    } else {
                        val user: FirebaseUser = authentication.currentUser!!
                        Log.d(TAG, user.displayName + " " + user.email)
                    }
                }
    }

    private fun initLoginScreen() {
        val registrationButton = findViewById(R.id.loginButton) as FancyButton
        val loginButton = findViewById(R.id.registerButton) as FancyButton

        registrationButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        loginButton.setOnClickListener {
            val intent = Intent(this, RegisterUsername::class.java)
            startActivity(intent)
        }
        val fbLoginButton = findViewById(R.id.facebookLoginButton) as LoginButton
        fbLoginButton.setReadPermissions("email", "public_profile")

        fbLoginButton.registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken);
                Log.d(TAG, result.toString())
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, error!!.message.toString())
            }

        })
    }

    override fun onStart() {
        super.onStart()
        val default = UserUtility.getUID(this) ?: "no display name - debug"
        if (UserUtility.isUserSignedIn(this)) {
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
        } else {
            Log.d(TAG, default)
        }
    }

    private fun queryDatabase() {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance();
        val myRef: DatabaseReference = database.reference;
        val uid = authentication.currentUser!!.uid
        //TODO: FIX THIS
    }
}
