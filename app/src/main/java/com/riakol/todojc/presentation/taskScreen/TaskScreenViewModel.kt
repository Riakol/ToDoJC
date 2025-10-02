package com.riakol.todojc.presentation.taskScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.usecase.GetTaskDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskScreenViewModel @Inject constructor(
    private val getTaskDetailsUseCase: GetTaskDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _taskDetails = MutableStateFlow<Task?>(null)
    val taskDetails: StateFlow<Task?> = _taskDetails

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            viewModelScope.launch {
                getTaskDetailsUseCase.invoke(taskId).collect { task ->
                    _taskDetails.value = task
                }
            }
        }
    }
}