package com.josephgwara.to_dolist.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josephgwara.to_dolist.data.Todo
import com.josephgwara.to_dolist.data.TodoRepository
import com.josephgwara.to_dolist.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import com.josephgwara.to_dolist.util.UIEvent
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
):ViewModel(){
    val todos = repository.getTodos()

    private val _uiEvent = Channel<UIEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo: Todo? = null



    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.OnTodoClick ->{
                sendUIEvent(UIEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))

            }
            is TodoListEvent.OnAddTodoClick ->{

                sendUIEvent(UIEvent.Navigate(Routes.ADD_EDIT_TODO))


            }
            is TodoListEvent.OnUndoDeleteClick ->{
                deletedTodo?.let { todo ->

                viewModelScope.launch {
                    repository.insertTodo(todo)

                }
                }

            }
            is TodoListEvent.OnDeleteTodoClick ->{

                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUIEvent(UIEvent.ShowSnackbar("Todo Deleted",action = "Undo"))

                }

            }
            is TodoListEvent.OnDoneChange ->{
                viewModelScope.launch {

                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }


            }
        }
    }

private fun sendUIEvent(event:UIEvent){
    viewModelScope.launch {
        _uiEvent.send(event)

    }
}

}