package com.github.rozag.redux.base

import com.github.rozag.redux.core.ReduxAction
import com.github.rozag.redux.core.ReduxState
import java.util.*

open class BufferedSubscribableStore<S : ReduxState, A : ReduxAction>(
        initialState: S,
        override var reducer: (state: S, action: A) -> S,
        private var bufferSizeLimit: Int = UNLIMITED,
        initialBufferSize: Int = 0
) : SubscribableStore<S, A>(initialState, reducer), ReduxBufferedSubscribableStore<S, A> {

    companion object {
        const val UNLIMITED: Int = 0
    }

    private var currentBufferPosition: Int = 0
    private val stateBuffer: MutableList<S> = ArrayList(when {
        initialBufferSize > 0 -> initialBufferSize
        bufferSizeLimit > 0 -> bufferSizeLimit
        else -> 1
    })

    init {
        stateBuffer.add(initialState)
    }

    override fun getState(): S = stateBuffer[currentBufferPosition]

    override fun internalDispatch(action: A) {
        // Apply the reducer graph
        val newState = reducer(stateBuffer.last(), action)

        // Handle the buffer
        val previousBufferSize = stateBuffer.size
        stateBuffer.add(newState)
        if (previousBufferSize == bufferSizeLimit) {
            stateBuffer.removeAt(0)
        }
        currentBufferPosition = stateBuffer.lastIndex

        notifySubscribers()
    }

    override fun bufferSizeLimit(): Int = bufferSizeLimit

    override fun changeSizeLimit(newSizeLimit: Int) {
        if (newSizeLimit in 1..(bufferSizeLimit - 1)) {
            val currentBufferSize = stateBuffer.size
            if (currentBufferSize > newSizeLimit) {
                for (i in currentBufferSize - newSizeLimit - 1 downTo 0) {
                    stateBuffer.removeAt(i)
                }
            }
        }
        bufferSizeLimit = newSizeLimit
    }

    override fun currentBufferSize(): Int = stateBuffer.size

    override fun currentBufferPosition(): Int = currentBufferPosition

    override fun resetBuffer(initialState: S) {
        stateBuffer.clear()
        stateBuffer.add(initialState)
        currentBufferPosition = 0
    }

    override fun jumpToState(position: Int) {
        currentBufferPosition = when {
            position < 0 -> throw IllegalArgumentException("Position $position is negative")
            position > stateBuffer.lastIndex -> throw IllegalArgumentException(
                    "Position $position is larger than the last buffer's index (${stateBuffer.lastIndex})"
            )
            else -> position
        }

        notifySubscribers()
    }

}