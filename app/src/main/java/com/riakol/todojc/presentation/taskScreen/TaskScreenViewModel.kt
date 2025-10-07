package com.riakol.todojc.presentation.taskScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.usecase.subtask.AddSubTaskUseCase
import com.riakol.todojc.domain.usecase.subtask.GetSubTasksUseCase
import com.riakol.todojc.domain.usecase.subtask.UpdateSubTaskUseCase
import com.riakol.todojc.domain.usecase.task.GetTaskDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TaskScreenViewModel @Inject constructor(
    private val getTaskDetailsUseCase: GetTaskDetailsUseCase,
    private val getSubTasksUseCase: GetSubTasksUseCase,
    private val addSubTaskUseCase: AddSubTaskUseCase,
    private val updateSubTaskUseCase: UpdateSubTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _taskDetails = MutableStateFlow<Task?>(null)
    val taskDetails: StateFlow<Task?> = _taskDetails
    private val _subTasks = MutableStateFlow<List<SubTask>>(emptyList())
    val subTasks: StateFlow<List<SubTask>> = _subTasks

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            viewModelScope.launch {
                getTaskDetailsUseCase.invoke(taskId).collect { task ->
                    _taskDetails.value = task
                }
            }
            viewModelScope.launch {
                getSubTasksUseCase.invoke(taskId).collect { subTasks ->
                    _subTasks.value = subTasks
                }
            }
        }
    }

    fun addSubtask(subtask: String) {
        viewModelScope.launch {
            val newSubtask = SubTask(
                id = 0,
                title = subtask,
                taskId = taskDetails.value?.id ?: 0
            )
            addSubTaskUseCase(newSubtask)
        }
    }

    fun onSubTaskChanged(subtask: SubTask, newTitle: String) {
        viewModelScope.launch {
            updateSubTaskUseCase(subtask.copy(title = newTitle))
        }
    }
}