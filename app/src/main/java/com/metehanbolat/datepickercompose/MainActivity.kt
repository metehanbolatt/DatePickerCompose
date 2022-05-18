package com.metehanbolat.datepickercompose

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.metehanbolat.datepickercompose.ui.theme.DatePickerComposeTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatePickerComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    OutlinedDatePicker(value = LocalDate.now(), onValueChange = { LocalDate.now() })
                }
            }
        }
    }
}

@Composable
fun OutlinedDatePicker(
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    value: LocalDate,
    formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    icon: ImageVector = Icons.Filled.DateRange,
    iconContentDescription: String = stringResource(id = R.string.date_icon_description),
    onValueChange: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isDatePickerDisplayed = remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onValueChange(LocalDate.of(year, month, dayOfMonth))
            isDatePickerDisplayed.value = false
            focusManager.clearFocus()
        }, value.year, value.monthValue, value.dayOfMonth
    )
    datePickerDialog.setOnDismissListener {
        isDatePickerDisplayed.value = false
        focusManager.clearFocus()
    }
    if (isDatePickerDisplayed.value) {
        datePickerDialog.show()
    }

    OutlinedTextField(
        modifier = modifier.onFocusChanged { isDatePickerDisplayed.value = it.isFocused },
        label = label,
        value = value.format(formatter),
        onValueChange = { onValueChange(LocalDate.parse(it, formatter)) },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.clickable { isDatePickerDisplayed.value = true }
            )
        }
    )
}
