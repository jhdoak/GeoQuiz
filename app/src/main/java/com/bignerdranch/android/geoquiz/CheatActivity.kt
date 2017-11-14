package com.bignerdranch.android.geoquiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import com.bignerdranch.android.geoquiz.IntentExtraKeyConstants.EXTRA_CHEAT_COUNT
import com.bignerdranch.android.geoquiz.IntentExtraKeyConstants.EXTRA_ANSWER_IS_TRUE
import com.bignerdranch.android.geoquiz.IntentExtraKeyConstants.EXTRA_ANSWER_SHOWN

class CheatActivity : AppCompatActivity() {

    private val KEY_CHEAT_COUNT = "cheatCount"
    private val KEY_CHEAT_BUTTON_ENABLED = "cheatButtonEnabled"
    private val KEY_CHEAT_BUTTON_VISIBLE = "cheatButtonVisible"
    private val KEY_ANSWER_TEXT = "answerText"

    private var mAnswerIsTrue: Boolean = false
    private var mCheatCount: Int = 3

    private lateinit var mAnswerTextView: TextView
    private lateinit var mShowAnswerButton: Button
    private lateinit var mApiLevelTextView: TextView
    private lateinit var mCheatCountView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        mCheatCount = intent.getIntExtra(EXTRA_CHEAT_COUNT, mCheatCount)

        toggleCheatButton()

        mAnswerTextView = findViewById(R.id.answer_text_view)

        mCheatCountView = findViewById(R.id.cheat_count_text_view)

        mApiLevelTextView = findViewById(R.id.api_level_text_view)
        mApiLevelTextView.text = ("API Level " + Build.VERSION.SDK_INT)

        mShowAnswerButton = findViewById(R.id.show_answer_button)

        if (savedInstanceState != null) {
            mCheatCount = savedInstanceState.getInt(KEY_CHEAT_COUNT)
            mShowAnswerButton.isEnabled = savedInstanceState.getBoolean(KEY_CHEAT_BUTTON_ENABLED)
            mShowAnswerButton.visibility = savedInstanceState.getInt(KEY_CHEAT_BUTTON_VISIBLE)
            mAnswerTextView.text = savedInstanceState.getCharSequence(KEY_ANSWER_TEXT)
        }

        mCheatCountView.text = generateCheatCountText()

        mShowAnswerButton.setOnClickListener({
            updateCheatCount()
            toggleCheatButton()

            mAnswerTextView.setText(
                    if (mAnswerIsTrue) R.string.true_button
                    else R.string.false_button
            )
            setAnswerShownResult(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val cx: Int = mShowAnswerButton.width / 2
                val cy: Int = mShowAnswerButton.height / 2
                val radius: Float = mShowAnswerButton.width.toFloat()
                var anim: Animator = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0.toFloat())
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        mShowAnswerButton.visibility = View.INVISIBLE
                    }
                })
                anim.start()
            } else {
                mShowAnswerButton.visibility = View.INVISIBLE
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(KEY_CHEAT_COUNT, mCheatCount)
        outState?.putInt(KEY_CHEAT_BUTTON_VISIBLE, mShowAnswerButton.visibility)
        outState?.putBoolean(KEY_CHEAT_BUTTON_ENABLED, mShowAnswerButton.isEnabled)
        outState?.putCharSequence(KEY_ANSWER_TEXT, mAnswerTextView.text)
    }

    private fun updateCheatCount() {
        when {
            mCheatCount > 0 -> mCheatCount -= 1
            mCheatCount == 0 -> mShowAnswerButton.isEnabled = false
        }
        mCheatCountView.text = generateCheatCountText()
    }

    private fun toggleCheatButton() {
        if (mCheatCount == 0) {
            mShowAnswerButton.isEnabled = false
        }
    }

    private fun generateCheatCountText(): String {
        return mCheatCount.toString() + " Cheats Remaining"
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheatCount: Int): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            intent.putExtra(EXTRA_CHEAT_COUNT, cheatCount)
            return intent
        }

        fun wasAnswerShown(result: Intent): Boolean {
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }

        fun checkCheatCount(result: Intent): Int {
            return result.getIntExtra(EXTRA_CHEAT_COUNT, 3)
        }
    }
}
