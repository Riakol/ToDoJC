package com.riakol.todojc.presentation.taskScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.usecase.subtask.AddSubTaskUseCase
import com.riakol.todojc.domain.usecase.subtask.GetSubTasksUseCase
import com.riakol.todojc.domain.usecase.subtask.RemoveSubTaskUseCase
import com.riakol.todojc.domain.usecase.subtask.UpdateSubTaskUseCase
import com.riakol.todojc.domain.usecase.task.GetTaskDetailsUseCase
import com.riakol.todojc.domain.usecase.task.UpdateTaskUseCase
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
    private val removeSubTaskUseCase: RemoveSubTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _taskDetails = MutableStateFlow<Task?>(null)
    val taskDetails: StateFlow<Task?> = _taskDetails
    private val _subTasks = MutableStateFlow<List<SubTask>>(emptyList())
    val subTasks: StateFlow<List<SubTask>> = _subTasks
    private val _noteText = MutableStateFlow("")
    val noteText: StateFlow<String> = _noteText

    init {
        savedStateHandle.get<Int>("taskId")?.let { taskId ->
            loadTaskDetails(taskId)
            viewModelScope.launch {
                getSubTasksUseCase.invoke(taskId).collect { subTasks ->
                    _subTasks.value = subTasks
                }
            }
        }
    }

    private fun loadTaskDetails(taskId: Int) {
        viewModelScope.launch {
            getTaskDetailsUseCase(taskId).collect { task ->
                _taskDetails.value = task
                if (task != null) {
                    _noteText.value = task.description ?: ""
                }
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            val currentTask = _taskDetails.value
            if (currentTask != null && currentTask.description != _noteText.value) {
                updateTaskUseCase(currentTask.copy(description = _noteText.value))
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

    fun removeSubTask(subTask: SubTask) {
        viewModelScope.launch {
            removeSubTaskUseCase(subTask)
        }
    }

    fun onNoteTextChanged(newNote: String) {
        _noteText.value = newNote
    }

    fun toggleCompletion(subTask: SubTask) {
        viewModelScope.launch {
            val updatedSubTask = subTask.copy(isCompleted = !subTask.isCompleted)
            updateSubTaskUseCase(updatedSubTask)
        }
    }
}