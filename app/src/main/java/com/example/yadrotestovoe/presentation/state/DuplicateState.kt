package com.example.yadrotestovoe.presentation.state

interface DuplicateState {
    data class Success(val successCode: Int) : DuplicateState
    data class Empty(val emptyCode: Int) : DuplicateState
    data class Error(val errorMessage: String?) : DuplicateState
}