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
    private val showError: () -> Unit
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

        val submitButton = view.findViewById<TextView>(R.id.rate_button)

        val backButton = view.findViewById<ImageView>(R.id.iv_prev)
        val text = view.findViewById<EditText>(R.id.et_review_text)

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
                val textResult = if (text.text.toString().trim().isEmpty()) null else text.text.toString()
                onClickRating.invoke(rating, textResult)
                dismiss()
            } else {
                showError.invoke()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        setStyle(STYLE_NO_FRAME, R.style.RateDialog)
    }
}