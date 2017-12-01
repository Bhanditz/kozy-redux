package com.github.rozag.redux.notes.middleware

import com.github.rozag.redux.core.ReduxMiddleware
import com.github.rozag.redux.core.store.ReduxStore
import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.IdGenerator
import com.github.rozag.redux.notes.NotesAction
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.model.Note
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.resources.ResProvider
import timber.log.Timber

class FirstLaunchMiddleware(
        private val idGenerator: IdGenerator,
        private val resProvider: ResProvider,
        private val repo: NotesRepo
) : ReduxMiddleware<AppState, NotesAction, ReduxStore<AppState, NotesAction>>() {

    override fun doBeforeDispatch(store: ReduxStore<AppState, NotesAction>, action: NotesAction) = Unit

    override fun doAfterDispatch(store: ReduxStore<AppState, NotesAction>, action: NotesAction) {
        if (action is NotesAction.FirstLaunch.Started) {
            val notes = listOf(
                    Note.Text(
                            id = idGenerator.generateId(),
                            title = resProvider.getString(R.string.initial_note_title_0),
                            body = resProvider.getString(R.string.initial_note_body_0)
                    ),
                    Note.Text(
                            id = idGenerator.generateId(),
                            title = resProvider.getString(R.string.initial_note_title_1),
                            body = resProvider.getString(R.string.initial_note_body_1)
                    ),
                    Note.Text(
                            id = idGenerator.generateId(),
                            title = resProvider.getString(R.string.initial_note_title_2),
                            body = resProvider.getString(R.string.initial_note_body_2)
                    )
            )
            repo.addNotes(
                    notes = notes,
                    onComplete = { noteList -> store.dispatch(NotesAction.FirstLaunch.Complete(noteList)) },
                    onError = { throwable -> Timber.e(throwable) }
            )
        }
    }

}