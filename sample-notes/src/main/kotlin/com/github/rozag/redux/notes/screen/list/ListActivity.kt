package com.github.rozag.redux.notes.screen.list

import android.os.Bundle
import com.github.rozag.redux.notes.*
import com.github.rozag.redux.notes.logger.Logger

class ListActivity : ReduxActivity() {

    override val layoutResourceId = R.layout.activity_main
    override val toolbarTitleId = R.string.app_name
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    private val loadNotesActionCreator: ActionCreator = NotesApplication.loadNotesActionCreator
    private val logger: Logger = NotesApplication.logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadNotesActionCreator.createAndDispatch()
    }

    override fun onStop() {
        super.onStop()
        store.dispatch(ListAction.TearDown())
    }

    override fun onNewState(state: State) {
        // TODO
        logger.d("onNewState: $state")
    }

}