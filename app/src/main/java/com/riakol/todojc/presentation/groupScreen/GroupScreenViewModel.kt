package com.riakol.todojc.presentation.groupScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.usecase.task.AddTaskUseCase
import com.riakol.todojc.domain.usecase.group.GetGroupDetailsUseCase
import com.riakol.todojc.domain.usecase.task.GetTasksUseCase
import com.riakol.todojc.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GroupScreenViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val getGroupDetailsUseCase: GetGroupDetailsUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks
    private val _groupDetails  = MutableStateFlow<Group?>(null)
    val groupDetails: StateFlow<Group?> = _groupDetails

    private var currentGroupId: Int? = null

    init {
        currentGroupId = savedStateHandle.get<Int>("groupId")
        currentGroupId?.let { groupId ->
            loadTasks(groupId)
            loadGroupDetails(groupId)
        }
    }

    private fun loadTasks(groupId: Int) {
        viewModelScope.launch {
            getTasksUseCase(groupId).collect { tasks ->
                _tasks.value = tasks
            }
        }
    }

    private fun loadGroupDetails(groupId: Int) {
        viewModelScope.launch {
            getGroupDetailsUseCase(groupId).collect { group ->
                _groupDetails.value = group
            }
        }
    }

    fun addTask(title: String) {
        currentGroupId?.let { groupId ->
            viewModelScope.launch {
                val newTask = Task(
                    id = 0,
                    title = title,
                    description = "",
                    creationDate = 0L,
                    groupId = groupId
                )
                addTaskUseCase(newTask)
            }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTaskUseCase(updatedTask)
        }
    }
}

