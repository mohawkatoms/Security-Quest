package com.example.securityquest.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilteredOutlinedTextField(
    text: String,
    onChanged: (String) -> Unit,
    ignoredRegex: Regex,
    label: String,
    modifier: Modifier
) {
    OutlinedTextField(value = text,
        onValueChange = {
            if (!it.contains(ignoredRegex)) onChanged(it)
        },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
    )
}