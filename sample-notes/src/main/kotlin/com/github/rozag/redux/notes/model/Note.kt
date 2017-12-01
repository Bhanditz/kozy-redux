package com.github.rozag.redux.notes.model

sealed class Note(open val id: String, open val title: String) {

    companion object {
        val EMPTY: Note = Text("", "", "")
    }

    data class Text(
            override val id: String,
            override val title: String,
            val body: String
    ) : Note(id, title)

    data class Todo(
            override val id: String,
            override val title: String,
            val items: List<TodoItem>
    ) : Note(id, title) {
        // TODO: remove
        val itemsAsBody: String by lazy {
            val sb = StringBuilder()
            sb.append("Todo items [\n")
            items.forEach { item ->
                sb.append(item).append('\n')
            }
            sb.append(']')
            sb.toString()
        }
    }

}