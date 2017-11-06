package com.bignerdranch.android.geoquiz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private lateinit var mTrueButton : Button
    private lateinit var mFalseButton : Button
    private lateinit var mNextButton : Button
    private lateinit var mQuestionTextView : TextView

    private var mCurrentIndex : Int = 0;

    private var mQuestionBank = arrayOf(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        mTrueButton = findViewById(R.id.true_button)
        mTrueButton.setOnClickListener({
            Toast.makeText(this@QuizActivity,
                    R.string.correct_toast,
                    Toast.LENGTH_SHORT).show()
        })

        mFalseButton = findViewById(R.id.false_button)
        mFalseButton.setOnClickListener({
            Toast.makeText(this@QuizActivity,
                    R.string.incorrect_toast,
                    Toast.LENGTH_SHORT).show()
        })
    }

}
