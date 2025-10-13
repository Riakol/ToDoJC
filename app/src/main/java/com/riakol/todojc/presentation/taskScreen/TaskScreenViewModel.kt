package com.riakol.todojc.presentation.taskScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.usecase.group.GetGroupDetailsUseCase
import com.riakol.todojc.domain.usecase.subtask.AddSubTaskUseCase
import com.riakol.todojc.domain.usecase.subtask.GetSubTasksUseCase
import com.riakol.todojc.domain.usecase.subtask.RemoveSubTaskUseCase
import com.riakol.todojc.domain.usecase.subtask.UpdateSubTaskUseCase
import com.riakol.todojc.domain.usecase.task.GetTaskDetailsUseCase
import com.riakol.todojc.domain.usecase.task.RemoveTaskUseCase
import com.riakol.todojc.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@HiltViewModel
class TaskScreenViewModel @Inject constructor(
    private val getTaskDetailsUseCase: GetTaskDetailsUseCase,
    private val getSubTasksUseCase: GetSubTasksUseCase,
    private val addSubTaskUseCase: AddSubTaskUseCase,
    private val updateSubTaskUseCase: UpdateSubTaskUseCase,
    private val removeSubTaskUseCase: RemoveSubTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getGroupDetailsUseCase: GetGroupDetailsUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _taskDetails = MutableStateFlow<Task?>(null)
    val taskDetails: StateFlow<Task?> = _taskDetails

    private val _subTasks = MutableStateFlow<List<SubTask>>(emptyList())
    val subTasks: StateFlow<List<SubTask>> = _subTasks

    private val _noteText = MutableStateFlow("")
    val noteText: StateFlow<String> = _noteText

    private val _groupDetails = MutableStateFlow<Group?>(null)
    val groupDetails: StateFlow<Group?> = _groupDetails

        init {
            savedStateHandle.get<Int>("taskId")?.let { taskId ->
                viewModelScope.launch {
                    getTaskDetailsUseCase(taskId)
                        .flatMapLatest { task ->
                            _taskDetails.value = task
                            _noteText.value = task?.description ?: ""

                            if (task != null) {
                                getGroupDetailsUseCase(task.groupId)
                            } else {
                                flowOf(null)
                            }
                        }
                        .collect { group ->
                            _groupDetails.value = group
                        }
                }

                viewModelScope.launch {
                    getSubTasksUseCase(taskId).collect { subTasks ->
                        _subTasks.value = subTasks
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
        _taskDetails.value?.id?.let { taskId ->
            viewModelScope.launch {
                val newSubtask = SubTask(
                    id = 0,
                    title = subtask,
                    taskId = taskId
                )
                addSubTaskUseCase(newSubtask)
            }
        }
    }

    fun onSubTaskNameChanged(subtask: SubTask, newTitle: String) {
        viewModelScope.launch {
            updateSubTaskUseCase(subtask.copy(title = newTitle))
        }
    }

    fun onTaskNameChanged(newTitle: String) {
        _taskDetails.value?.let {
            if (newTitle.isNotBlank() && it.title != newTitle) {
                viewModelScope.launch {
                    updateTaskUseCase(it.copy(title = newTitle))
                }
            }
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

    fun toggleSubTaskCompletion(subTask: SubTask) {
        viewModelScope.launch {
            val updatedSubTask = subTask.copy(isCompleted = !subTask.isCompleted)
            updateSubTaskUseCase(updatedSubTask)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTaskUseCase(updatedTask)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            removeTaskUseCase(task)
        }
    }

    fun toggleFavoriteStatus(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isFavourite = !task.isFavourite)
            updateTaskUseCase(updatedTask)
        }
    }
}