package com.technostore.feature_product.product.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.DialogFragment
import com.technostore.feature_product.R


class RateProductDialog(
    private val onClickRating: (rate: Int, text: String?) -> Unit,
    private val showError: () -> Unit,
    private val userReviewText: String?,
    private val userRating: Int?
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.product_rate_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        val motionLayout = view.findViewById<MotionLayout>(R.id.ml_set_mark)
        setStartUserRating(motionLayout, userRating)
        val submitButton = view.findViewById<TextView>(R.id.rate_button)

        val backButton = view.findViewById<ImageView>(R.id.iv_prev)
        val text = view.findViewById<EditText>(R.id.et_review_text)
        text.setText(userReviewText.orEmpty())

        backButton.setOnClickListener {
            dismissNow()
        }
        submitButton.setOnClickListener {
            if (motionLayout.currentState != R.id.center_btn_0) {
                val rating = when (motionLayout.currentState) {
                    R.id.center_btn_1 -> 1
                    R.id.center_btn_2 -> 2
                    R.id.center_btn_3 -> 3
                    R.id.center_btn_4 -> 4
                    R.id.center_btn_5 -> 5
                    R.id.center_btn_6 -> 6
                    R.id.center_btn_7 -> 7
                    R.id.center_btn_8 -> 8
                    R.id.center_btn_9 -> 9
                    else -> 10
                }
                val textResult =
                    if (text.text.toString().trim().isEmpty()) null else text.text.toString()
                onClickRating.invoke(rating, textResult)
                dismiss()
            } else {
                showError.invoke()
            }
        }
    }

    private fun setStartUserRating(motionLayout: MotionLayout, rating: Int?) {
        when (rating) {
            null -> motionLayout.transitionToState(R.id.center_btn_0)
            1 -> motionLayout.transitionToState(R.id.center_btn_1)
            2 -> motionLayout.transitionToState(R.id.center_btn_2)
            3 -> motionLayout.transitionToState(R.id.center_btn_3)
            4 -> motionLayout.transitionToState(R.id.center_btn_4)
            5 -> motionLayout.transitionToState(R.id.center_btn_5)
            6 -> motionLayout.transitionToState(R.id.center_btn_6)
            7 -> motionLayout.transitionToState(R.id.center_btn_7)
            8 -> motionLayout.transitionToState(R.id.center_btn_8)
            9 -> motionLayout.transitionToState(R.id.center_btn_9)
            10 -> motionLayout.transitionToState(R.id.center_btn_10)
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        setStyle(STYLE_NO_FRAME, R.style.RateDialog)
    }
}