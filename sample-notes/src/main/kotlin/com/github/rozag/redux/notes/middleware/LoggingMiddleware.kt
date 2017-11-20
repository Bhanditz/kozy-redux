package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.ReduxStore
import com.github.rozag.redux.notes.Action
import com.github.rozag.redux.notes.State
import com.github.rozag.redux.notes.logger.Logger

class LoggingMiddleware(
        private val logger: Logger,
        private val tag: String = "LoggingMiddleware"
) : ReduxMiddleware<State, Action, ReduxStore<State, Action>>() {

    override fun doAfterDispatch(store: ReduxStore<State, Action>, action: Action) {
        logger.d(tag, "Dispatching action: $action")
    }

    override fun doBeforeDispatch(store: ReduxStore<State, Action>, action: Action) {
        logger.d(tag, "New state: ${store.getState()}")
    }

}