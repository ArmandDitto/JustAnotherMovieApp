package com.android.justordinarymovieapp.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.android.justordinarymovieapp.utils.showErrorDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>

    private var _binding: VB? = null
    protected val binding get() = requireNotNull(_binding)
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    open fun shouldInterceptBackPress(): Boolean = false
    protected var onBackPressCallback: OnBackPressedCallback? = null

    open fun shouldAddBackStackListener(): Boolean = false
    protected var fragmentBackStackListener: FragmentManager.OnBackStackChangedListener? = null

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (shouldAddBackStackListener()) {
            fragmentBackStackListener = FragmentManager.OnBackStackChangedListener {
                onBackStackChanged()
            }.also {
                parentFragmentManager.addOnBackStackChangedListener(it)
            }
        }

        if (shouldInterceptBackPress()) {
            onBackPressCallback =
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    onBackPressHandler()
                }
        }
    }

    open fun onBackPressHandler() {}

    @CallSuper
    open fun onBackStackChanged() {
        onBackPressCallback?.isEnabled = parentFragmentManager.fragments.lastOrNull() == this
    }

    fun showErrorDialog(
        title: String? = null,
        desc: String? = null,
        btnPositiveText: String? = null,
        onPositiveBtnClick: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
        withCloseIcon: Boolean = false,
        isCancelable: Boolean = false
    ) {
        activity?.supportFragmentManager?.showErrorDialog(
            title = title,
            desc = desc,
            btnPositiveText = btnPositiveText,
            onPositiveBtnClick = onPositiveBtnClick,
            onDismiss = onDismiss,
            withCloseIcon = withCloseIcon,
            isCancelable = isCancelable
        )
    }

}