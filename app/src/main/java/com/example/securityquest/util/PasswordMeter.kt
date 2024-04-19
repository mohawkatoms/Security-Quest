package com.example.securityquest.util

fun calculatePasswordStrength(password: String): Int {
    var strength = 0
    val len = password.length

    // Anzahl der Zeichen
    strength += (len * 4)

    // Großbuchstaben
    var uppercaseCount = 0
    for (char in password) {
        if (char.isUpperCase()) {
            uppercaseCount++
        }
    }
    strength += ((len - uppercaseCount) * 2)

    // Kleinbuchstaben
    var lowercaseCount = 0
    for (char in password) {
        if (char.isLowerCase()) {
            lowercaseCount++
        }
    }
    strength += ((len - lowercaseCount) * 2)

    // Zahlen
    var numberCount = 0
    for (char in password) {
        if (char.isDigit()) {
            numberCount++
        }
    }
    strength += (numberCount * 4)

    // Symbole
    val symbolCount = len - (uppercaseCount + lowercaseCount + numberCount)
    strength += (symbolCount * 6)

    // Mittlere Zahlen oder Symbole
    if (len > 2) {
        val middleCount = len - 2
        strength += (middleCount * 2)
    }

    // Anforderungen
    if (len >= 8) {
        strength += 2
    }

    // Abzüge

    // Nur Buchstaben
    if (len == lowercaseCount + uppercaseCount && len > 0) {
        strength -= len
    }

    // Nur Zahlen
    if (len == numberCount && len > 0) {
        strength -= len
    }

    // Wiederholte Zeichen (Groß- und Kleinschreibung ignorieren)
    var repeats = 0
    val charMap = mutableMapOf<Char, Int>()
    for (char in password.toLowerCase()) {
        if (charMap.containsKey(char)) {
            repeats++
        } else {
            charMap[char] = 1
        }
    }
    strength -= (repeats * (repeats - 1))

    // Aufeinanderfolgende Großbuchstaben (ABC...)
    for (i in 0 until len - 1) {
        if (password[i].isUpperCase() && password[i + 1].isUpperCase()) {
            strength -= 2
        }
    }

    // Aufeinanderfolgende Kleinbuchstaben (abc...)
    for (i in 0 until len - 1) {
        if (password[i].isLowerCase() && password[i + 1].isLowerCase()) {
            strength -= 2
        }
    }

    // Aufeinanderfolgende Zahlen (123...)
    for (i in 0 until len - 1) {
        if (password[i].isDigit() && password[i + 1].isDigit()) {
            strength -= 2
        }
    }

    // Fortlaufende Buchstaben (3+)
    for (i in 0 until len - 2) {
        if (password[i] + 1 == password[i + 1] && password[i + 1] + 1 == password[i + 2]) {
            strength -= 3
        }
    }

    // Fortlaufende Zahlen (3+)
    for (i in 0 until len - 2) {
        if (password[i].isDigit() && password[i + 1].isDigit() && password[i + 2].isDigit()) {
            if (password[i].toInt() + 1 == password[i + 1].toInt() && password[i + 1].toInt() + 1 == password[i + 2].toInt()) {
                strength -= 3
            }
        }
    }

    // Sicherstellen, dass die Stärke im Bereich [0, 100] liegt
    strength = strength.coerceIn(1, 100)

    // Überprüfen, ob nur Leerzeichen eingegeben wurden
    if (password.trim().isEmpty()) {
        strength = 1
    }

    return strength
}