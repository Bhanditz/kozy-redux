package com.github.rozag.redux.core

interface ReduxBufferedStore<S : ReduxState, A : ReduxAction> : ReduxStore<S, A> {

    fun bufferSizeLimit(): Int

    fun changeSizeLimit(newSizeLimit: Int)

    fun currentBufferSize(): Int

    fun currentBufferPosition(): Int

    fun resetBuffer(initialState: S)

    fun jumpToState(position: Int)

    fun resetToState(position: Int)

}