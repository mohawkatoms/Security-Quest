package com.example.securityquest.util

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