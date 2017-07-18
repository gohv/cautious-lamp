package parentcrew.com.parentcrew.database

import android.content.Context
import android.util.Log
import com.google.firebase.database.*
import parentcrew.com.parentcrew.util.UserUtility

class FirebaseDb(){

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    fun getData(context: Context): String?{
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference(UserUtility.getUID(context))
        val uid = UserUtility.getUID(context)
        var kur: String = "ogobong"

        databaseReference.child(uid).addValueEventListener(object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                for (data: DataSnapshot in dataSnapshot!!.children){
                     kur = data.child("name").getValue(String::class.java)
                }
            }
            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }
        })

        return kur
    }

}