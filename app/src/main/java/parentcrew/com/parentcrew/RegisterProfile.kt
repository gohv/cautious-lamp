package parentcrew.com.parentcrew


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import mehdi.sakout.fancybuttons.FancyButton
import parentcrew.com.parentcrew.model.Kid
import parentcrew.com.parentcrew.model.Parent
import parentcrew.com.parentcrew.util.ProfilePictureUploader

class RegisterProfile : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var lookingForEditText: EditText
    private lateinit var interestEditText: EditText
    private lateinit var kidNameEditText: EditText
    private lateinit var kidAgeEditText: EditText
    private lateinit var kidNameLabel: TextView
    private lateinit var kidAgeLabel: TextView
    private lateinit var registerKidCheckBox: CheckBox
    private lateinit var submitButton: FancyButton
    private lateinit var profilePic: ImageView
    private lateinit var databaseRef: DatabaseReference
    private lateinit var authentication: FirebaseAuth
    private val REQUEST_IMAGE_LOAD = 2
    private lateinit var image: String
    private lateinit var imageUploader: ProfilePictureUploader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_profile)
        imageUploader = ProfilePictureUploader()

        nameEditText = bind(R.id.nameEditText)
        locationEditText = bind(R.id.locationEditText)
        lookingForEditText = bind(R.id.lookingForEditText)
        interestEditText = bind(R.id.interestEditText)
        registerKidCheckBox = bind(R.id.kidsCheckBox)
        kidNameEditText = bind(R.id.kidNameEditText)
        kidAgeEditText = bind(R.id.kidAgeEditText)
        submitButton = bind(R.id.submitButton)
        profilePic = bind(R.id.profilePic)
        kidAgeLabel = bind(R.id.kidAgeLabel)
        kidNameLabel = bind(R.id.kidNameLabel)

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        authentication = FirebaseAuth.getInstance()


        registerKids(false)

        registerKidCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                registerKids(true)
            } else {
                registerKids(false)
            }
        }

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val location = locationEditText.text.toString()
            val interest = interestEditText.text.toString()
            val lookingFor = lookingForEditText.text.toString()
            val kidName = kidNameEditText.text.toString()
            val kidAge = kidAgeEditText.text.toString()
            val uid = authentication.currentUser!!.uid
            databaseRef = database.getReference(uid)

            val kid = Kid(kidName, kidAge)
            val parent = Parent(uid, name,
                    location, lookingFor,
                    interest, image, kid)

            databaseRef.setValue(parent)
            val profilePicPath: Uri = Uri.parse(image)
            imageUploader.uploadPicture(profilePicPath, uid)
            //TODO: GET PICTURE URL AND STORE INTO DATABASE
            //TODO: Clear input, dissplay a message for success and send to MainActivity
            val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(profilePicPath)
                    .build()
            authentication.currentUser!!.updateProfile(profileUpdate)
                    .addOnCompleteListener { task ->
                        if (task.isComplete) {
                            val intent = Intent(this@RegisterProfile, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RegisterProfile, task.exception!!.message, Toast.LENGTH_LONG).show()
                        }
                    }
        }

        profilePic.setOnClickListener {
            loadPic()
        }


    }

    private fun loadPic() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_LOAD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_LOAD && resultCode == Activity.RESULT_OK) {
            image = data!!.data.toString()
            Picasso.with(this).load(image).into(profilePic)
        }
    }

    private fun registerKids(b: Boolean) {
        if (b) {
            kidAgeEditText.visibility = View.VISIBLE
            kidNameEditText.visibility = View.VISIBLE
            kidAgeLabel.visibility = View.VISIBLE
            kidNameLabel.visibility = View.VISIBLE
        } else {
            kidNameEditText.visibility = View.GONE
            kidAgeEditText.visibility - View.GONE
            kidAgeLabel.visibility = View.INVISIBLE
            kidNameLabel.visibility = View.INVISIBLE
        }

    }

    override fun onBackPressed() {
        //TODO: HANDLE THIS SOMEHOW
    }

    fun <T : View> Activity.bind(@IdRes res: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById(res) as T
    }
}
