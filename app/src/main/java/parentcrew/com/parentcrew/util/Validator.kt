package parentcrew.com.parentcrew.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class Validator(private val editText: EditText) : TextWatcher {

    abstract fun validate(editText: EditText, text: String)

    override fun afterTextChanged(s: Editable) {
        val text = editText.text.toString()
        validate(editText, text)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //not used
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //not used
    }
}
