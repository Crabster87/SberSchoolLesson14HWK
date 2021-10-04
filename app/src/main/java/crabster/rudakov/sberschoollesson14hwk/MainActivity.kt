package crabster.rudakov.sberschoollesson14hwk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import crabster.rudakov.sberschoollesson14hwk.Constants.ENTERED_TEXT
import crabster.rudakov.sberschoollesson14hwk.Constants.THIRD_FRAGMENT_TAG
import crabster.rudakov.sberschoollesson14hwk.fragments.FirstFragment
import crabster.rudakov.sberschoollesson14hwk.fragments.SecondFragment
import crabster.rudakov.sberschoollesson14hwk.fragments.ThirdFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var thirdFragmentContainer: ViewGroup? = null
    private var enteredText: String? = null

    /**
     * В данном методе создаём первый и второй фрагменты в случае если они ещё ни
     * разу не были созданы
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { //Если сохранение состояния ещё ни разу не производилось, то
            //Передаём менеджеру команду для создания структуры из фрагментов
            supportFragmentManager.commit {
                setReorderingAllowed(true) //Переупорядочиваем вызов методов (вызываем onCreate() новых фрагмента до onDestroy() старых)
                add<FirstFragment>(R.id.first_fragment_container) //Добавляем первый фрагмент
                add<SecondFragment>(R.id.second_fragment_container) //Добавляем второй фрагмент
            }
        }
    }

    /**
     * В данном методе реализуем возможнось получать данные из первого фрагмента,
     * нажатием кнопки второго фрагмента вызывать создание третьего фрагмента
     * */
    override fun onResume() {
        super.onResume()
        val firstFragment: FirstFragment =
            supportFragmentManager.findFragmentById(R.id.first_fragment_container) as FirstFragment
        val secondFragment: SecondFragment =
            supportFragmentManager.findFragmentById(R.id.second_fragment_container) as SecondFragment

        secondFragment.requireView().findViewById<Button>(R.id.second_fragment_button)?.apply {
            setOnClickListener {
                enteredText =
                    firstFragment.requireView() //Получаем введённый в поле первого фрагмента текст
                        .findViewById<EditText>(R.id.first_fragment_edit_text_view).text.toString()

                val bundle = bundleOf(ENTERED_TEXT to enteredText) //Помещаем текст в бандл по тегу
                createThirdFragment(bundle) //Создаём третий фрагмент, передав в него полученный текст
            }
        }
    }

    /**
     * Метод создаёт третий фрагмент по нажатию кнопки во втором фрагменте
     * */
    private fun createThirdFragment(bundle: Bundle) {
        val rootView = findViewById<LinearLayout>(R.id.root_view)
        if (thirdFragmentContainer == null) { //Если третий фрагмент ещё ни разу не создавался, то
            thirdFragmentContainer =
                FragmentContainerView(this) //Создаём специальный контейнер для фрагментов
            thirdFragmentContainer?.id = R.id.third_fragment
            thirdFragmentContainer?.layoutParams =
                LinearLayout.LayoutParams( //Задаём параметры контейнера третьего фрагмента
                    ViewGroup.LayoutParams.MATCH_PARENT, //width
                    ViewGroup.LayoutParams.WRAP_CONTENT, //height
                    1f) //weight
            rootView.addView(thirdFragmentContainer) //Добавляем вью третьего фрагмента в корневое вью активити
        }
        //Передаём менеджеру команду для создания третьего фрагмента
        supportFragmentManager.commit {
            replace<ThirdFragment>( //Заменяем пустой третий фрагмент в контейнере на такой же, но с текстом
                thirdFragmentContainer!!.id,
                THIRD_FRAGMENT_TAG,
                args = bundle
            )
            setReorderingAllowed(true) //Сглаживаем переход между состояниями экрана
            addToBackStack(null) //Добавляем транзакцию в стек
        }
    }

    /**
     * Переопределяем метод для сохранения данных в случае поворота экрана
     * */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(bundleOf(ENTERED_TEXT to enteredText)) //Помещаем текст в бандл по тегу
    }

    /**
     * Переопределяем метод для восстановления данных после поворота экрана
     * */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        enteredText =
            savedInstanceState.getString(ENTERED_TEXT) //Получаем по тегу введённый в поле первого фрагмента текст
        if (enteredText != null) { //В случае если текст был введён, восстанавливаем третий фрагмент на основе сохранённых данных
            createThirdFragment(savedInstanceState)
        }
    }

}