package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mPreviousButton: ImageButton
    private lateinit var mQuestionTextView: TextView

    private var mCurrentIndex: Int = 0

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

        mQuestionTextView = findViewById(R.id.question_text_view)
        mQuestionTextView.setOnClickListener({
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
        })

        mTrueButton = findViewById(R.id.true_button)
        mTrueButton.setOnClickListener({
            checkAnswer(true)
        })

        mFalseButton = findViewById(R.id.false_button)
        mFalseButton.setOnClickListener({
            checkAnswer(false)
        })

        mPreviousButton = findViewById(R.id.previous_button)
        mPreviousButton.setOnClickListener({
            mCurrentIndex =
                    if (mCurrentIndex == 0) {
                        mQuestionBank.size - 1
                    } else {
                        (mCurrentIndex - 1) % mQuestionBank.size
                    }
            updateQuestion()
        })

        mNextButton = findViewById(R.id.next_button)
        mNextButton.setOnClickListener({
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
        })

        updateQuestion()
    }

    private fun updateQuestion() {
        var question: Int = mQuestionBank[mCurrentIndex].mTextResId
        mQuestionTextView.text = getString(question)
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        var answerIsTrue: Boolean = mQuestionBank[mCurrentIndex].mAnswerTrue

        var messageResId: Int =
                if (userPressedTrue == answerIsTrue) {
                    R.string.correct_toast
                } else {
                    R.string.incorrect_toast
                }

        Toast.makeText(this@QuizActivity,
                messageResId,
                Toast.LENGTH_SHORT).show()

    }

}
