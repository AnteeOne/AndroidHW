package tech.antee.reduxcalculator.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freeletics.coredux.CancellableSideEffect
import com.freeletics.coredux.createStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val actionHistory by lazy {
        ActionHistory()
    }

    val calculationSideEffect =
        CancellableSideEffect<UiState, Action>("Update calculations") { state, action, logger, handler ->
            when (action) {
                is Action.InputNumberAction -> handler { name, output ->
                    launch(Dispatchers.IO) {
                        output.send(Action.LoadingAction)
                        actionHistory.saveToHistory(action)
                        calculationInterceptor()
                        try {
                            val calculatedData = actionHistory.calculateByHistory()
                            output.send(Action.SuccessAction(calculatedData))
                        } catch (e: Exception) {
                            output.send(Action.ErrorAction)
                        }

                    }
                }
                else -> null
            }
        }

    val store = viewModelScope.createStore<UiState, Action>(
        name = "Calculator",
        initialState = UiState(data = Data(0, 0, 0)),
        sideEffects = listOf(calculationSideEffect),
        reducer = { currentState, newAction -> reduce(currentState, newAction) }
    )

    private fun reduce(state: UiState, newAction: Action): UiState {
        return when (newAction) {
            is Action.LoadingAction -> {
                Log.d("APP_TAGG", "111111111")
                state.copy(isLoading = true)
            }
            is Action.ErrorAction -> {
                Log.d("APP_TAGG", "22222")
                state.copy(isLoading = false, isError = true)
            }
            else -> {
                Log.d("APP_TAGG", "3")
                state
            }
        }
    }

    private suspend fun calculationInterceptor() {
        delay(5000)
    }

    data class UiState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val data: Data
    )

    data class Data(val first: Int?, val second: Int?, val sum: Int?)

}