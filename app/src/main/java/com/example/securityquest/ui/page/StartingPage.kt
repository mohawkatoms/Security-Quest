package com.example.securityquest.ui.page

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
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
import androidx.compose.material.icons.outlined.AddModerator
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Pattern
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.securityquest.R
import com.example.securityquest.model.Game
import com.example.securityquest.ui.components.FilteredOutlinedTextField
import com.example.securityquest.util.calculatePasswordStrength
import com.example.securityquest.util.generatePassword
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.security.MessageDigest

//Font von Uberschrift
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Rubik Glitch")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartingPage(modifier: Modifier = Modifier, onNavigateToTicTacToePage: (Int, String) -> Unit, onNavigateToVierGewinntPage: (Int, String) -> Unit, onNavigateToSnakePage: (Int, String) -> Unit, onNavigateToLeaderboardPage: () -> Unit, password: String) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Box(modifier) {
        var isPasswordDialogOpen by rememberSaveable {
            mutableStateOf(false)
        }
        var isExplanationDialogOpen by rememberSaveable {
            mutableStateOf(false)
        }
        var passwordFromUser by rememberSaveable {
            mutableStateOf(password)
        }
        var howOftenWasPasswordPwned by rememberSaveable {
            mutableIntStateOf(-1)
        }

        fun checkPwned(password: String, onResult: (Int) -> Unit) {
            val client = OkHttpClient()

            val hashedPassword = MessageDigest.getInstance("SHA-1").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
            val request = Request.Builder()
                .url("https://api.pwnedpasswords.com/range/${hashedPassword.take(5)}")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    onResult(-1) // Indicate an error occurred
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            onResult(-1) // Indicate an error occurred
                            throw IOException("Unexpected code $response")
                        }

                        val apiResponse = response.body!!.string().trimIndent()
                        val responseMap = apiResponse.lines().associate { line ->
                            val parts = line.split(":")
                            parts[0] to parts[1].toInt()
                        }
                        Log.w("ResponseMap::", responseMap.toString())
                        val searchString = hashedPassword.substring(5).uppercase()
                        Log.w("Searchstring::", searchString)
                        val number = responseMap[searchString] ?: 0 // Use 0 if not found
                        Log.w("Number::", number.toString())
                        onResult(number) // Pass the result to the callback
                    }
                }
            })
        }

        Row(horizontalArrangement = Arrangement.End) {
            FilledIconToggleButton(
                checked = isPasswordDialogOpen,
                onCheckedChange = { isPasswordDialogOpen = true },
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Pattern, contentDescription = "Create Password")
            }
            Spacer(Modifier.weight(1f))
            FilledIconToggleButton(
                checked = isExplanationDialogOpen,
                onCheckedChange = { isExplanationDialogOpen = true },
                modifier = Modifier.padding(end = 10.dp, top = 10.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Lightbulb, contentDescription = "Explanation")
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            Text(
                fontFamily = fontFamily,
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
                mutableStateOf(Game.TIC_TAC_TOE.nameToString)
            }
            FilteredOutlinedTextField(
                text = passwordFromUser,
                onChanged = { passwordFromUser = it
                            howOftenWasPasswordPwned = -1},
                ignoredRegex = Regex("[\\s-]"),
                label = "Passwort",
                modifier = Modifier.padding(top = 80.dp),
                {
                    IconButton(onClick = { checkPwned(passwordFromUser) {result ->
                        scope.launch {
                        if (context.isOnline()) {
                            if (result == 0) {
                                howOftenWasPasswordPwned = 0
                                snackbarHostState.showSnackbar("Passwort ist in keinem Datenleck aufgetaucht!")

                            } else {
                                howOftenWasPasswordPwned = result
                                snackbarHostState.showSnackbar("Passwort ist in $result Datenlecks aufgetaucht!")
                            }
                        }else {
                            howOftenWasPasswordPwned = -1
                            snackbarHostState.showSnackbar("Stelle eine Internetverbindung her!")
                        }
                        }}}, enabled = passwordFromUser.isNotEmpty()) {
                        Icon(imageVector = Icons.Outlined.AddModerator, contentDescription = "Create Password", tint = when(howOftenWasPasswordPwned){ -1 -> MaterialTheme.colorScheme.onBackground; 0 -> MaterialTheme.colorScheme.primary; in 0..Int.MAX_VALUE -> MaterialTheme.colorScheme.error; else -> MaterialTheme.colorScheme.onBackground })
                    }
                }
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
                        DropdownMenuItem(text = { Text(text = Game.TIC_TAC_TOE.nameToString) }, onClick = {
                            game = Game.TIC_TAC_TOE.nameToString
                            isGameSelectionExpanded = false
                        })
                        DropdownMenuItem(text = { Text(text = Game.VIER_GEWINNT.nameToString) }, onClick = {
                            game = Game.VIER_GEWINNT.nameToString
                            isGameSelectionExpanded = false
                        })
                        DropdownMenuItem(text = { Text(text = Game.SNAKE.nameToString) }, onClick = {
                            game = Game.SNAKE.nameToString
                            isGameSelectionExpanded = false
                        })
                    }
                }
                //um die Tastatur zu schliessen, bevor eine neue Seite gerendert wird 
                val controller = LocalSoftwareKeyboardController.current
                Button(onClick = {
                    val passwordStrength = calculatePasswordStrength(passwordFromUser)
                    when(game) {
                        Game.TIC_TAC_TOE.nameToString -> {
                            controller?.hide()
                            onNavigateToTicTacToePage(passwordStrength, passwordFromUser)
                        }
                        Game.VIER_GEWINNT.nameToString -> {
                            controller?.hide()
                            onNavigateToVierGewinntPage(passwordStrength, passwordFromUser)
                        }
                        Game.SNAKE.nameToString -> {
                            controller?.hide()
                            onNavigateToSnakePage(passwordStrength, passwordFromUser)
                        }
                    }

                }, enabled = passwordFromUser.isNotEmpty()) {
                    Icon(imageVector = Icons.Outlined.LockOpen, contentDescription = null, modifier = Modifier.padding(end = 3.dp))
                    Text(text = "Knacken")
                }
            }
            Row(modifier = Modifier.padding(top = 315.dp)) {
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(
                    onClick = { onNavigateToLeaderboardPage() },
                    modifier = Modifier.padding(end = 25.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Leaderboard, contentDescription = "Leaderboard")
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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
                        text = "Länge $lengthSlider",
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
                    Row {
                        Text(
                            text = "Zurücksetzen",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                        )
                        Text(
                            text = "Erstellen",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 53.dp, top = 10.dp)
                        )
                    }
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
                                imageVector = Icons.Rounded.DeleteSweep,
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
                            modifier = Modifier.padding(start = 90.dp),
                            enabled = useCapitalCharacters || useLowercaseCharacters || useNumbers || useSpecialCharacters
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
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
        if (isExplanationDialogOpen) {
            Dialog(onDismissRequest = { isExplanationDialogOpen = false }) {
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
                        text = "Passwort erstellen",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(13.dp)
                    )
                    Text(
                        text = "Das ist eine Anleitung zur Erstellung sicherer Passwörter",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Länge",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Je länger das Passwort, desto schwieriger ist es zu knacken. Empfohlen werden mindestens 12 Zeichen.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Komplexität",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Ein gutes Passwort enthält eine Mischung aus Groß- und Kleinbuchstaben, Zahlen, Sonderzeichen und gegebenenfalls auch Leerzeichen.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "Einzigartigkeit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, top = 10.dp)
                    )
                    Text(
                        text = "Verwenden Sie niemals dasselbe Passwort für mehrere Konten oder Dienste.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                        lineHeight = 19.sp
                    )
                    Row {
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { isExplanationDialogOpen = false }, modifier = Modifier.padding(top = 10.dp)) {
                            Text(text = "Schließen")
                        }
                    }
                }
            }

        }
    }
}

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}