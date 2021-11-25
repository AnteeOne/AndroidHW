package tech.antee.reduxcalculator.ui

class ActionHistory {
    private var lastFirstAction: Action.InputNumberAction? = null
    private var lastSecondAction: Action.InputNumberAction? = null

    fun saveToHistory(lastFirst: Action.InputNumberAction, lastSecond: Action.InputNumberAction) {
        lastFirstAction = lastFirst
        lastSecondAction = lastSecond
    }

    fun saveToHistory(lastAction: Action.InputNumberAction) {
        lastFirstAction = lastSecondAction
        lastSecondAction = lastAction
    }

    fun calculateByHistory(): MainViewModel.Data {
        val actionList = listOf(lastFirstAction, lastSecondAction)
        var firstNumber = actionList.find { action -> action is Action.InputFirstAction }?.number
        var secondNumber = actionList.find { action -> action is Action.InputSecondAction }?.number
        var resultNumber = actionList.find { action -> action is Action.InputSumAction }?.number
        firstNumber ?: run {
            firstNumber = resultNumber!! - secondNumber!!
        }
        secondNumber ?: run {
            secondNumber = resultNumber!! - firstNumber!!
        }
        resultNumber ?: run {
            resultNumber = firstNumber!! + secondNumber!!
        }
        return MainViewModel.Data(firstNumber, secondNumber, resultNumber)
    }
}