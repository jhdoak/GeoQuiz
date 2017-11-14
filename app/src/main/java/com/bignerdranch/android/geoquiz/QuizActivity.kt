package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private val TAG: String = "QuizActivity"
    private val KEY_INDEX: String = "index"
    private val KEY_CHEAT_COUNT = "cheatCount"
    private val REQUEST_CODE_CHEAT: Int = 0

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: Button
    private lateinit var mCheatButton: Button
    private lateinit var mQuestionTextView: TextView

    private var mCurrentIndex: Int = 0
    private var mCheatCount: Int = 3
    private var mIsCheater: Boolean = false

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

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX)
            mCheatCount = savedInstanceState.getInt(KEY_CHEAT_COUNT)
        }

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

        mCheatButton = findViewById(R.id.cheat_button)
        mCheatButton.setOnClickListener({
            val answerIsTrue: Boolean = mQuestionBank[mCurrentIndex].mAnswerTrue
            val intent = CheatActivity.newIntent(this@QuizActivity, answerIsTrue, mCheatCount)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) return
            mIsCheater = CheatActivity.wasAnswerShown(data)
            mCheatCount = CheatActivity.checkCheatCount(data)
        }
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState?.putInt(KEY_INDEX, mCurrentIndex)
        outState?.putInt(KEY_CHEAT_COUNT, mCheatCount)
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
        val question: Int = mQuestionBank[mCurrentIndex].mTextResId
        mQuestionTextView.text = getString(question)
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue: Boolean = mQuestionBank[mCurrentIndex].mAnswerTrue

        val messageResId: Int =
                when {
                    // Checks if user has cheated before checking if their answer is correct
                    mIsCheater -> R.string.judgment_toast
                    userPressedTrue == answerIsTrue -> R.string.correct_toast
                    else -> R.string.incorrect_toast
                }

        Toast.makeText(this@QuizActivity,
                messageResId,
                Toast.LENGTH_SHORT).show()
    }

}
