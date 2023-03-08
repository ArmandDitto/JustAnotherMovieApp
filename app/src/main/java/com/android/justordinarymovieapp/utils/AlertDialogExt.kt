package com.android.justordinarymovieapp.utils

import androidx.fragment.app.FragmentManager
import com.android.justordinarymovieapp.base.view.alert.BaseAlertDialog

fun FragmentManager.showErrorDialog(
    title: String? = null,
    desc: String? = null,
    btnPositiveText: String? = null,
    onPositiveBtnClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    withCloseIcon: Boolean = false,
    isCancelable: Boolean = false
) {
    val dialog = BaseAlertDialog()

    dialog.setContent(
        title = title ?: "Error",
        desc = desc,
        imageSrc = null,
        btnPositiveText = btnPositiveText ?: "Retry",
        onPositiveBtnClick = onPositiveBtnClick,
        onDismiss = onDismiss,
        withCloseIcon = withCloseIcon,
        isCancelable = isCancelable
    )

    val ft = beginTransaction()
    ft.add(dialog, null)
    ft.commit()
}