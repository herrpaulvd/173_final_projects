package com.notesapp.feature_note.domain.use_cases

data class NoteUseCases(
    val getNote: GetNotesUseCase,
    val deleteNote: DeleteNoteUseCase,
    val addNote: AddNoteUseCase,
    val getSingleNote: GetNoteUseCase
)
