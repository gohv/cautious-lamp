package parentcrew.com.parentcrew

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import parentcrew.com.parentcrew.util.UserUtility


class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var currentUserReference: DatabaseReference
    private lateinit var allUsersReference: DatabaseReference
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation) as BottomNavigationView

        val uid = UserUtility.getUID(this)

        database = FirebaseDatabase.getInstance()
        currentUserReference = database.getReference(uid)
        allUsersReference = database.reference

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_profile ->
                    Toast.makeText(this@MainActivity,"Profile",Toast.LENGTH_LONG).show()
                //TODO: Display current user registered profile
                R.id.action_people ->
                    loadList()
                R.id.action_map ->
                    Toast.makeText(this@MainActivity,"Map",Toast.LENGTH_LONG).show()
                //TODO: Display Map
            }
            true
        }

        currentUserReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError?) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                //This gets the current logged user data
                val name = dataSnapshot!!.child("name").value
                val interest = dataSnapshot.child("interest").value
                val location = dataSnapshot.child("location").value
                val lookingFor = dataSnapshot.child("lookingFor").value
                val profilePic = dataSnapshot.child("profilePic").value

                Log.d("TEST", name.toString())

            }

        })



    }

    private fun updateUI(name: String){
        if (name == null){
            Toast.makeText(this@MainActivity,"Your profile is not completed, Please complete your profile",Toast.LENGTH_LONG).show()
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
        }else{
            loadList()
        }

    }

    fun loadList(){
        //TODO: Populate the List with this Method

        allUsersReference.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError?) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                //This gets the current logged user data
                for (s in dataSnapshot!!.children){
                    val names = s.child("name").value
                    Log.d("TEST", names.toString())
                }


            }

        })
    }

    override fun onBackPressed() {
        //Disabled
    }
}
