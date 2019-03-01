package net.librebora.util

import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions

abstract class VerifySupport<T> {
    private val verifyCallbacks: MutableList<() -> Unit> = mutableListOf()
    abstract val instance: T

    fun verifyAll() {
        verifyCallbacks.forEach { callback ->
            callback()
        }
        verifyNoMoreInteractions(instance)
    }

    protected fun verifyCallback(callback: () -> Unit) {
        verifyCallbacks.add(callback)
    }
}