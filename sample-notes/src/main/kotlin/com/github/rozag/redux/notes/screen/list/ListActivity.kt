package com.github.rozag.redux.notes.screen.list

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import com.github.rozag.redux.notes.AppState
import com.github.rozag.redux.notes.NotesApplication
import com.github.rozag.redux.notes.R
import com.github.rozag.redux.notes.ReduxActivity
import com.github.rozag.redux.notes.resources.ResProvider
import com.github.rozag.redux.notes.router.RouterAction
import com.github.rozag.redux.notes.router.Screen

class ListActivity : ReduxActivity() {

    override val layoutResourceId = R.layout.activity_list
    override val toolbarTitleId = 0
    override val displayHomeAsUp = false
    override val homeButtonEnabled = false

    private val resProvider: ResProvider = NotesApplication.resProvider
    private val loadNotesActionCreator = NotesApplication.loadNotesActionCreator
    private val newNoteActionCreator = NotesApplication.newNoteActionCreator

    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var addNoteButton: FloatingActionButton

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: ListAdapter

    private var isExiting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coordinatorLayout = findViewById(R.id.coordinator_layout)
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        addNoteButton = findViewById(R.id.add_note_btn)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(StatusBarMarginItemDecoration(resources))
        adapter = ListAdapter(resProvider) { note ->
            store.dispatch(ListAction.Edit(note))
            store.dispatch(RouterAction.Open(Screen.EDIT))
        }
        recyclerView.adapter = adapter

        addNoteButton.setOnClickListener {
            newNoteActionCreator.createAndDispatch()
        }

        loadNotesActionCreator.createAndDispatch()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isExiting = true
        store.dispatch(ListAction.TearDown())
        store.dispatch(RouterAction.Closed(Screen.LIST))
    }

    override fun onNewState(state: AppState) {
        super.onNewState(state)

        if (isExiting) {
            return
        }

        val listState = state.listState
        when {
            listState.isLoading -> {
                progressBar.visibility = VISIBLE
                recyclerView.visibility = GONE
                addNoteButton.visibility = GONE
            }
            else -> {
                progressBar.visibility = GONE
                recyclerView.visibility = VISIBLE
                addNoteButton.visibility = VISIBLE
                adapter.updateNotes(listState.notes)
                if (listState.isError) {
                    Snackbar.make(coordinatorLayout, "Error", Snackbar.LENGTH_SHORT).show()
                    store.dispatch(ListAction.ErrorShown())
                }
            }
        }
    }

}