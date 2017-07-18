package parentcrew.com.parentcrew.util

import android.content.Context
import android.preference.PreferenceManager


object UserUtility {
    private val UID_KEY = "username"

    /**
     * Sets the uid in SharedPreferences.
     * @param context Used for accessing SharedPreferences
     * *
     * @param uid Username to store
     */
    fun setUID(context: Context, uid: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(UID_KEY, uid)
        editor.apply()
    }

    /**
     * Gets the username from SharedPreferences.
     * @param context Used for accessing SharedPreferences
     * *
     * @return The username of the user logged in; or null if no user is logged in
     */
    fun getUID(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val username = prefs.getString(UID_KEY, null)
        return username
    }

    /**
     * Used to determine if user is signed in or not.
     * @param context Used for accessing SharedPreferences
     * *
     * @return true if a user is logged in; false otherwise
     */
    fun isUserSignedIn(context: Context): Boolean {
        var isUserSignedIn = false
        if (getUID(context) != null) {
            isUserSignedIn = true
        }
        return isUserSignedIn
    }
}
