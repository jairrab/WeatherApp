package com.github.jairrab.repository

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("MemberVisibilityCanBePrivate")
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], manifest = Config.NONE)
abstract class BaseTestClass : TestCase() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Suppress("unused")
    val context: Context = ApplicationProvider.getApplicationContext()
}