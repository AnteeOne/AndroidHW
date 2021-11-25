package tech.antee.reduxcalculator.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import tech.antee.reduxcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var textWatchers: MutableList<TextWatcher>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textWatchers = listOf(
            getTextWatcher(
                { viewModel.store.dispatch(Action.InputFirstAction(it)) },
                { onError() }
            ),
            getTextWatcher(
                { viewModel.store.dispatch(Action.InputSecondAction(it)) },
                { onError() }
            ),
            getTextWatcher(
                { viewModel.store.dispatch(Action.InputSumAction(it)) },
                { onError() }
            )
        ).toMutableList()

        viewModel = MainViewModel().also {
            it.store.subscribe {
                updateUi(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        with(binding) {

            first.addTextChangedListener(textWatchers[0])
            second.addTextChangedListener(textWatchers[1])
            sum.addTextChangedListener(textWatchers[2])
        }
    }

    private fun updateUi(uiState: MainViewModel.UiState) {
        with(binding) {
            Log.d("APP_TAG", uiState.toString())
            progressBar.isVisible = uiState.isLoading
            if (!uiState.isError) {
                uiState.data.first?.let { first.setText(it.toString(), textWatchers[0]) }
                uiState.data.second?.let { second.setText(it.toString(), textWatchers[1]) }
                uiState.data.sum?.let { sum.setText(it.toString(), textWatchers[2]) }
            } else {
                onError()
            }
        }
    }

    private fun getTextWatcher(onTextChanged: (Int) -> Unit, onInvalidString: () -> Unit) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            try {
                onTextChanged(
                    p0.toString().toInt()
                )
            } catch (t: Throwable) {
                onInvalidString()
            }
        }
    }

    private fun onError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
    }
}

fun EditText.setText(text: String, textWatcher: TextWatcher) {
    removeTextChangedListener(textWatcher)
    setText(text)
    addTextChangedListener(textWatcher)
}

