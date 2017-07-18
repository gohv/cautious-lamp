package parentcrew.com.parentcrew.util

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfilePictureUploader {

    var storageReference: StorageReference

    constructor() {

        storageReference = FirebaseStorage.getInstance().reference
    }

    public fun uploadPicture(filePath: Uri, uid: String) {
        val profilePictureReference = storageReference.child("profile/pictures/" + uid + ".jpg")
        profilePictureReference.putFile(filePath)
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