package com.github.jairrab.weatherapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.jairrab.weatherapp.R
import timber.log.Timber

abstract class BaseFragment() : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clazz = javaClass.simpleName
        Timber.v("$clazz | onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Timber.v("${javaClass.simpleName} | onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun setDisplayHomeAsUpEnabled(enable: Boolean) {
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(enable)
    }

    protected fun navigate(
        navId: Int,
        bundle: Bundle? = null,
        animateTransition: Boolean = true,
    ) {
        val currentDestination = findNavController().currentDestination?.label
        val navIdName = resources.getResourceName(navId).substringAfterLast("/")
        Timber.v("Navigation Graph | \"$currentDestination\" to R.id.$navIdName")

        if (animateTransition) {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.enter_from_right)
                .setExitAnim(R.anim.exit_to_left)
                .setPopEnterAnim(R.anim.enter_from_left)
                .setPopExitAnim(R.anim.exit_to_right)
                .build()
            findNavController().navigate(navId, bundle, navOptions)
        } else {
            findNavController().navigate(navId, bundle)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun popBackStack() {
        val currentDestination = findNavController().currentDestination?.label
        Timber.v("Navigation Graph | popBackStack: \"$currentDestination\"")
        findNavController().popBackStack()
    }

    protected fun showServerError(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected fun showApiError(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.v("${javaClass.simpleName} | onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.v("${javaClass.simpleName} | onDestroy")
    }
}