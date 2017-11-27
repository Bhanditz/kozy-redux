package com.github.rozag.redux.notes.screen.list.creator

import com.github.rozag.kueue.Kueue
import com.github.rozag.redux.notes.NotesStore
import com.github.rozag.redux.notes.repo.NotesRepo
import com.github.rozag.redux.notes.screen.list.ListAction
import timber.log.Timber

class LoadNotesActionCreator(
        private val store: NotesStore,
        private val repo: NotesRepo,
        private val taskQueue: Kueue
) {

    fun createAndDispatch() {
        store.dispatch(ListAction.LoadNotes.Started())
        taskQueue.fromCallable { repo.getNotes() }
                .onComplete { notes ->
                    store.dispatch(ListAction.LoadNotes.Complete(notes))
                }
                .onError { throwable ->
                    Timber.e(throwable)
                    store.dispatch(ListAction.LoadNotes.Error())
                }
                .go()
    }

}