package parentcrew.com.parentcrew

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

/**
 * Created by gohv on 12.07.17.
 */

class Convert {

    internal var file = Uri.fromFile(File("path/to/images/rivers.jpg"))

    private var storageRef: StorageReference? = null


    private fun something() {
        storageRef = FirebaseStorage.getInstance().reference
        val riversRef = storageRef!!.child("images/rivers.jpg")

        riversRef.putFile(file)
                .addOnSuccessListener { taskSnapshot ->
                    // Get a URL to the uploaded content
                    val downloadUrl = taskSnapshot.downloadUrl
                }
                .addOnFailureListener {
                    // Handle unsuccessful uploads
                    // ...
                }
    }
}
