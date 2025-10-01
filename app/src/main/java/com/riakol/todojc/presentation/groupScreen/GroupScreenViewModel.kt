package com.riakol.todojc.presentation.groupScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.usecase.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GroupScreenViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        val groupId = savedStateHandle.get<Int>("groupId")

        if (groupId != null) {
            loadTasks(groupId)
        }

    }

    private fun loadTasks(groupId: Int) {
        viewModelScope.launch {
            getTasksUseCase(groupId).collect { tasks ->
                _tasks.value = tasks
            }
        }
    }
}