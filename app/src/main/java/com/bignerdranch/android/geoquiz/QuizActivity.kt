package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private val TAG: String = "QuizActivity"
    private val KEY_INDEX: String = "index"
    private val KEY_ANSWERED: String = "questionsAnswered"

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: Button
    private lateinit var mQuestionTextView: TextView

    private var mCurrentIndex: Int = 0
    private var mCorrectAnswerCount: Int = 0

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
            setQuestionsAnsweredState(savedInstanceState.getBooleanArray(KEY_ANSWERED))
        }

        mQuestionTextView = findViewById(R.id.question_text_view)
        updateQuestion()

        mTrueButton = findViewById(R.id.true_button)
        mTrueButton.setOnClickListener({
            checkAnswer(true)
            setQuestionAnswered()
            toggleButtons()
            if (checkIfAllAnswered()) {
                displayQuizScore()
            }
        })

        mFalseButton = findViewById(R.id.false_button)
        mFalseButton.setOnClickListener({
            checkAnswer(false)
            setQuestionAnswered()
            toggleButtons()
            if (checkIfAllAnswered()) {
                displayQuizScore()
            }
        })

        mNextButton = findViewById(R.id.next_button)
        mNextButton.setOnClickListener({
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
            toggleButtons()
        })

        toggleButtons()
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
        outState?.putBooleanArray(KEY_ANSWERED, getQuestionsAnsweredState())
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
                    mCorrectAnswerCount += 1
                    R.string.correct_toast
                } else {
                    R.string.incorrect_toast
                }

        Toast.makeText(this@QuizActivity,
                messageResId,
                Toast.LENGTH_SHORT).show()
    }

    private fun setQuestionAnswered() {
        mQuestionBank[mCurrentIndex].mHasBeenAnswered = true
    }

    private fun getQuestionsAnsweredState(): BooleanArray {
        var questionsAnsweredState = arrayListOf<Boolean>()
        mQuestionBank.forEach { question ->
            questionsAnsweredState.add(question.mHasBeenAnswered)
        }
        return questionsAnsweredState.toBooleanArray()
    }

    private fun setQuestionsAnsweredState(answeredState: BooleanArray) {
        mQuestionBank.forEachIndexed { index, question ->
            question.mHasBeenAnswered = answeredState[index]
        }
    }

    private fun toggleButtons() {
        mTrueButton.isEnabled = !mQuestionBank[mCurrentIndex].mHasBeenAnswered
        mFalseButton.isEnabled = !mQuestionBank[mCurrentIndex].mHasBeenAnswered
    }

    private fun checkIfAllAnswered(): Boolean {
        var unansweredQuestions: List<Question> =
                mQuestionBank.filter { question -> !question.mHasBeenAnswered }

        return unansweredQuestions.isEmpty()
    }

    private fun displayQuizScore() {
        Log.d(TAG, "mCorrectAnswerCount: ".plus(mCorrectAnswerCount))
        Log.d(TAG, "mQuestionBank.size: ".plus(mQuestionBank.size))
        var quizScore: Float =
                mCorrectAnswerCount.toFloat() / mQuestionBank.size.toFloat()
        Log.d(TAG, "quizScore: ".plus(quizScore))
        var scoreMessage: String = "You scored "
                .plus(Math.round(quizScore * 100))
                .plus("% on this quiz!")

        Toast.makeText(this@QuizActivity,
                scoreMessage,
                Toast.LENGTH_SHORT).show()
    }

}
