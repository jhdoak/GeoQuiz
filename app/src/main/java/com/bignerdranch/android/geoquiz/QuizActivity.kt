package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private val TAG: String = "QuizActivity"

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: Button
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
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_quiz)

        mQuestionTextView = findViewById(R.id.question_text_view)
        updateQuestion()

        mTrueButton = findViewById(R.id.true_button)
        mTrueButton.setOnClickListener({
            checkAnswer(true)
        })

        mFalseButton = findViewById(R.id.false_button)
        mFalseButton.setOnClickListener({
            checkAnswer(false)
        })

        mNextButton = findViewById(R.id.next_button)
        mNextButton.setOnClickListener({
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
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
