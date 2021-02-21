package com.github.jairrab.viewmodel.base

import androidx.lifecycle.ViewModel
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    init {
        Timber.v("${javaClass.simpleName} | initialized")
    }

    override fun onCleared() {
        super.onCleared()
        Timber.v("${javaClass.simpleName} | onCleared")
    }
}