package com.github.rozag.redux.notes.repository

import com.github.rozag.redux.notes.model.Note

class CompositeNotesRepository(
        private val localRepo: NotesRepository,
        private val remoteRepo: NotesRepository
) : NotesRepository {

    override fun getNotes(): List<Note> {
        val remoteNotes = remoteRepo.getNotes()
        val localNotes = remoteRepo.getNotes()
        return localNotes.union(remoteNotes).toList()
    }

    override fun addNote(note: Note) {
        remoteRepo.addNote(note)
        localRepo.addNote(note)
    }

    override fun updateNote(note: Note) {
        remoteRepo.updateNote(note)
        localRepo.updateNote(note)
    }

    override fun deleteNote(note: Note) {
        remoteRepo.deleteNote(note)
        localRepo.deleteNote(note)
    }

}