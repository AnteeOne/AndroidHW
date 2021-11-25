package tech.antee.reduxcalculator.ui

sealed class Action {
    sealed class InputNumberAction(open val number: Int) : Action()
    data class InputFirstAction(override val number: Int) : InputNumberAction(number)
    data class InputSecondAction(override val number: Int) : InputNumberAction(number)
    data class InputSumAction(override val number: Int) : InputNumberAction(number)
    data class SuccessAction(val data: MainViewModel.Data) : Action()
    object LoadingAction : Action()
    object ErrorAction : Action()
}
