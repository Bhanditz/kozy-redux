package com.github.rozag.redux.notes.model

sealed class Note(open val id: String, open val title: String) {

    data class Text(
            override val id: String,
            override val title: String,
            val body: String
    ) : Note(id, title) {
        companion object {
            val EMPTY: Text = Text("", "", "")
        }
    }

    data class Todo(
            override val id: String,
            override val title: String,
            val items: List<TodoItem>
    ) : Note(id, title) {
        companion object {
            val EMPTY: Todo = Todo("", "", emptyList())
        }
    }

}