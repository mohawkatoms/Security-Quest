package com.example.securityquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.securityquest.ui.theme.SecurityQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SecurityQuestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StartingPage()
                }
            }
        }
    }
}

fun generatePassword(
    useCapitalCharacters: Boolean,
    useLowercaseCharacters: Boolean,
    useNumbers: Boolean,
    useSpecialCharacters: Boolean,
    lengthSlider: Int
): String {
    val capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
    val numbers = "0123456789"
    val specialChars = "!@#$%^&*()_+{}[]|\\:;<>,.?/~"

    val allowedChars = StringBuilder()
    if (useCapitalCharacters) allowedChars.append(capitalChars)
    if (useLowercaseCharacters) allowedChars.append(lowercaseChars)
    if (useNumbers) allowedChars.append(numbers)
    if (useSpecialCharacters) allowedChars.append(specialChars)

    // Ensure minimum length is 8
    val passwordLength = if (lengthSlider < 8) 8 else lengthSlider
    val password = StringBuilder()
    val random = java.util.Random()

    repeat(passwordLength) {
        val randomIndex = random.nextInt(allowedChars.length)
        password.append(allowedChars[randomIndex])
    }

    return password.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartingPage(modifier: Modifier = Modifier) {
    Box(modifier) {
        var isPasswordDialogOpen by rememberSaveable {
            mutableStateOf(false)
        }
        var passwordFromUser by rememberSaveable {
            mutableStateOf("")
        }
        Row(horizontalArrangement = Arrangement.End) {
            FilledIconButton(
                onClick = { isPasswordDialogOpen = true },
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Rounded.Star, contentDescription = "Create Password")
            }
            Spacer(Modifier.weight(1f))
            FilledIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(end = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = "Settings")
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            Text(
                text = "Security Quest",
                fontWeight = FontWeight.Bold,
                fontSize = 80.sp,
                lineHeight = 80.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 130.dp),
                color = if (isSystemInDarkTheme()) {
                    dynamicDarkColorScheme(context).primary
                } else {
                    dynamicLightColorScheme(context).primary
                },
            )

            var isGameSelectionExpanded by rememberSaveable {
                mutableStateOf(false)
            }

            var game by rememberSaveable {
                mutableStateOf("Tic Tac Toe ")
            }
            OutlinedTextField(
                value = passwordFromUser,
                onValueChange = { passwordFromUser = it },
                label = { Text("Passwort") },
                modifier = Modifier.padding(top = 80.dp)
            )
            Row(modifier = Modifier.padding(15.dp)) {
                ExposedDropdownMenuBox(
                    expanded = isGameSelectionExpanded,
                    onExpandedChange = { isGameSelectionExpanded = it }) {
                    InputChip(
                        selected = false,
                        onClick = { },
                        label = { Text(text = game, fontSize = 16.sp, lineHeight = 16.sp) },
                        modifier = Modifier
                            .menuAnchor()
                            .padding(end = 15.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isGameSelectionExpanded
                            )
                        })
                    ExposedDropdownMenu(
                        expanded = isGameSelectionExpanded,
                        onDismissRequest = { isGameSelectionExpanded = false }) {
                        DropdownMenuItem(text = { Text(text = "Tic Tac Toe") }, onClick = {
                            game = "Tic Tac Toe "
                            isGameSelectionExpanded = false
                        })
                        DropdownMenuItem(text = { Text(text = "Vier Gewinnt") }, onClick = {
                            game = "Vier Gewinnt"
                            isGameSelectionExpanded = false
                        })
                        DropdownMenuItem(text = { Text(text = "Brick") }, onClick = {
                            game = "    Brick   "
                            isGameSelectionExpanded = false
                        })
                    }
                }
                Button(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null)
                    Text(text = "Spielen")
                }
            }
        }
        if (isPasswordDialogOpen) {
            Dialog(onDismissRequest = { isPasswordDialogOpen = false }) {
                var lengthSlider by remember { mutableIntStateOf(8) }
                var useCapitalCharacters by remember { mutableStateOf(false) }
                var useLowercaseCharacters by remember { mutableStateOf(false) }
                var useSpecialCharacters by remember { mutableStateOf(false) }
                var useNumbers by remember { mutableStateOf(false) }
                var generatedPassword by remember {
                    mutableStateOf("")
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(16.dp), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    Text(
                        text = "Passwort generieren",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(13.dp)
                    )
                    Text(
                        text = "Generieren Sie ein sicheres Passwort nach Ihren Vorgaben",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp)
                    )
                    Text(
                        text = "Länge ${lengthSlider.toString()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Row(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                        Slider(
                            value = lengthSlider.toFloat(),
                            onValueChange = {
                                lengthSlider =
                                    it.toInt()
                            },
                            valueRange = 8f..32f,
                            steps = 24
                        )
                    }
                    Row {
                        Text(
                            text = "Großbuchstaben",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "Kleinbuchstaben",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 33.dp, top = 10.dp)
                        )
                    }
                    Row(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                        Switch(
                            checked = useCapitalCharacters,
                            onCheckedChange = { useCapitalCharacters = it })
                        Switch(
                            modifier = Modifier.padding(start = 92.dp),
                            checked = useLowercaseCharacters,
                            onCheckedChange = { useLowercaseCharacters = it })
                    }
                    Row {
                        Text(
                            text = "Zahlen",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "Sonderzeichen",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 97.dp, top = 10.dp)
                        )
                    }
                    Row(modifier = Modifier.padding(start = 13.dp, end = 13.dp)) {
                        Switch(
                            checked = useNumbers,
                            onCheckedChange = { useNumbers = it })
                        Switch(
                            modifier = Modifier.padding(start = 92.dp),
                            checked = useSpecialCharacters,
                            onCheckedChange = { useSpecialCharacters = it })
                    }
                    Text(
                        text = "Zurücksetzen & Erstellen",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Row {
                        IconButton(
                            onClick = {
                                useCapitalCharacters = false
                                useLowercaseCharacters = false
                                useNumbers = false
                                useSpecialCharacters = false
                                lengthSlider = 8
                                generatedPassword = ""
                            },
                            modifier = Modifier.padding(start = 13.dp),
                            enabled = useCapitalCharacters || useLowercaseCharacters || useNumbers || useSpecialCharacters || lengthSlider > 8
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Zurücksetzen"
                            )
                        }
                        IconButton(
                            onClick = {
                                generatedPassword = generatePassword(
                                    useCapitalCharacters,
                                    useLowercaseCharacters,
                                    useNumbers,
                                    useSpecialCharacters,
                                    lengthSlider
                                )
                            },
                            modifier = Modifier.padding(start = 5.dp),
                            enabled = useCapitalCharacters || useLowercaseCharacters || useNumbers || useSpecialCharacters
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Erstellen"
                            )
                        }
                    }
                    Text(
                        text = generatedPassword,
                        modifier = Modifier
                            .padding(start = 13.dp, end = 13.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            lineBreak = LineBreak.Paragraph
                        )
                    )
                    Row {
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { isPasswordDialogOpen = false }) {
                            Text(text = "Abbrechen")
                        }
                        TextButton(enabled = generatedPassword.isNotEmpty(), onClick = {
                            isPasswordDialogOpen = false
                            passwordFromUser = generatedPassword
                        }) {
                            Text(text = "Einsetzen")
                        }
                    }
                }
            }

        }
    }
}

