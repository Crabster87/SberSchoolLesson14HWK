package crabster.rudakov.sberschoollesson14hwk.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import crabster.rudakov.sberschoollesson14hwk.Constants.ENTERED_TEXT
import crabster.rudakov.sberschoollesson14hwk.R

class ThirdFragment : Fragment(R.layout.third_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val enteredText =
            requireArguments().getString(ENTERED_TEXT) //Получаем переданный текст по тегу

        val thirdFragmentTextView =
            view.findViewById<TextView>(R.id.third_fragment_text_view).apply {
                text = enteredText //Устанавливаем получнный текст в текстовое вью
            }
    }

}