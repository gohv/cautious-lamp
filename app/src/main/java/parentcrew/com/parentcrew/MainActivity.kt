package parentcrew.com.parentcrew

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import parentcrew.com.parentcrew.database.FirebaseDb


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val something = FirebaseDb().getData(this)

        Log.d("FIREBASEKUR", something)

    }
}
