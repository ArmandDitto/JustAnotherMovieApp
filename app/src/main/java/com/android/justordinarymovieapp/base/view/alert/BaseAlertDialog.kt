package com.android.justordinarymovieapp.base.view.alert

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.android.justordinarymovieapp.R
import com.android.justordinarymovieapp.utils.goneView
import com.android.justordinarymovieapp.utils.visibleView

open class BaseAlertDialog : DialogFragment() {

    private lateinit var alertDialog: Dialog

    private var tvTitle: TextView? = null
    private var tvDesc: TextView? = null
    private var ivDialog: ImageView? = null
    private var ivClose: ImageView? = null
    private var btnPositive: Button? = null
    private var btnNegative: Button? = null
    private var btnPositiveText: String? = null
    private var btnNegativeText: String? = null
    private var layoutButton: LinearLayout? = null
    private var withCloseIcon: Boolean? = false

    var title: String? = null
    var desc: String? = null
    var imageSrc: Int? = null
    var isButtonPositiveVisible: Boolean? = null
    var isButtonNegativeVisible: Boolean? = null

    private var isTouchCancelable: Boolean = false

    private var positiveButtonListener: PositiveButtonListener? = null
    private var negativeButtonListener: NegativeButtonListener? = null
    private var dismissListener: DismissMessageListener? = null

    protected open fun getLayout(): Int {
        return R.layout.layout_alert_dialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        alertDialog = Dialog(requireContext())
        alertDialog.setCancelable(isCancelable)
        alertDialog.setCanceledOnTouchOutside(isTouchCancelable)
        alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        alertDialog.setContentView(getLayout())

        initViews()

        alertDialog.show()

        return alertDialog
    }

    /**
     * Set content for dialog without buttons
     */
    fun setContent(
        title: String?,
        desc: String?,
        imageSrc: Int?,
        onDismiss: (() -> Unit)? = null,
        withCloseIcon: Boolean? = false,
        isCancelable: Boolean = true
    ) {
        setContent(
            title = title,
            desc = desc,
            imageSrc = imageSrc,
            btnPositiveText = null,
            btnNegativeText = null,
            onPositiveBtnClick = null,
            onNegativeBtnClick = null,
            onDismiss = onDismiss,
            withCloseIcon = withCloseIcon,
            isCancelable = isCancelable
        )
    }

    /**
     * Set content for dialog with button positive only
     */
    fun setContent(
        title: String?,
        desc: String?,
        imageSrc: Int?,
        btnPositiveText: String?,
        onPositiveBtnClick: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
        withCloseIcon: Boolean? = false,
        isCancelable: Boolean = true
    ) {
        setContent(
            title = title,
            desc = desc,
            imageSrc = imageSrc,
            btnPositiveText = btnPositiveText,
            btnNegativeText = null,
            onPositiveBtnClick = onPositiveBtnClick,
            onNegativeBtnClick = null,
            onDismiss = onDismiss,
            withCloseIcon = withCloseIcon,
            isCancelable = isCancelable
        )
    }

    /**
     * Set content for dialog with all buttons
     */
    fun setContent(
        title: String?,
        desc: String?,
        imageSrc: Int?,
        btnPositiveText: String? = null,
        btnNegativeText: String? = null,
        onPositiveBtnClick: (() -> Unit)? = null,
        onNegativeBtnClick: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
        withCloseIcon: Boolean? = false,
        isCancelable: Boolean = true
    ) {

        this.title = title
        this.desc = desc
        this.imageSrc = imageSrc
        this.btnPositiveText = btnPositiveText
        this.btnNegativeText = btnNegativeText
        this.withCloseIcon = withCloseIcon
        this.isCancelable = isCancelable

        setListener(
            onPositiveBtnClick = onPositiveBtnClick,
            onNegativeBtnClick = onNegativeBtnClick,
            onDismiss = onDismiss
        )

    }

    private fun initViews() {
        tvTitle = alertDialog.findViewById(R.id.tv_dialog_title) as TextView
        tvDesc = alertDialog.findViewById(R.id.tv_dialog_desc) as TextView
        ivClose = alertDialog.findViewById(R.id.iv_close) as ImageView
        btnPositive = alertDialog.findViewById(R.id.btn_positive) as Button
        btnNegative = alertDialog.findViewById(R.id.btn_negative) as Button
        layoutButton = alertDialog.findViewById(R.id.layout_button) as LinearLayout

        if (title != null) tvTitle?.text = title
        else tvTitle?.goneView()

        tvDesc?.text = HtmlCompat.fromHtml(desc ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

        if (imageSrc != null) ivDialog?.setImageResource(imageSrc!!)
        else ivDialog?.goneView()

        if (btnPositiveText.isNullOrEmpty()) {
            btnPositive.goneView()
        } else {
            btnPositive.visibleView()
            btnPositive?.text = btnPositiveText
            btnPositive?.setOnClickListener {
                positiveButtonListener?.onClick()
                dismiss()
            }
        }

        if (btnNegativeText.isNullOrEmpty()) {
            btnNegative.goneView()
        } else {
            btnNegative.visibleView()
            btnNegative?.text = btnNegativeText
            btnNegative?.setOnClickListener {
                negativeButtonListener?.onClick()
                dismiss()
            }
        }

        if (withCloseIcon == true) {
            ivClose?.visibleView()
            ivClose?.setOnClickListener {
                dismiss()
            }
        } else ivClose.goneView()
    }

    private fun setListener(
        onPositiveBtnClick: (() -> Unit)? = null,
        onNegativeBtnClick: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null
    ) {
        setOnBtnPositiveClickListener(object : PositiveButtonListener {
            override fun onClick() {
                onPositiveBtnClick?.invoke()
            }
        })

        setOnBtnNegativeClickListener(object : NegativeButtonListener {
            override fun onClick() {
                onNegativeBtnClick?.invoke()
            }
        })

        setOnDismissListener(object : DismissMessageListener {
            override fun onDismissed() {
                onDismiss?.invoke()
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissListener?.onDismissed()
        super.onDismiss(dialog)
    }

    override fun onDestroy() {
        dismissListener?.onDismissed()
        super.onDestroy()
    }

    override fun onCancel(dialog: DialogInterface) {
        dismissListener?.onDismissed()
        super.onCancel(dialog)
    }

    fun setOnBtnPositiveClickListener(listener: PositiveButtonListener?) {
        this.positiveButtonListener = listener
    }

    fun setOnBtnNegativeClickListener(listener: NegativeButtonListener?) {
        this.negativeButtonListener = listener
    }

    fun setOnDismissListener(listener: DismissMessageListener?) {
        this.dismissListener = listener
    }

    interface PositiveButtonListener {
        fun onClick()
    }

    interface NegativeButtonListener {
        fun onClick()
    }

    interface DismissMessageListener {
        fun onDismissed()
    }
}