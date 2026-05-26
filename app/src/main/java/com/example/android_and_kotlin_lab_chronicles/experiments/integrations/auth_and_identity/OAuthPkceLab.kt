package com.example.android_and_kotlin_lab_chronicles.experiments.integrations.auth_and_identity

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Experimentos: Implementación manual de OAuth 2.0 con PKCE.
 *
 * Temas:
 * - Code Verifier: Generación de entropía segura.
 * - Code Challenge: Hashing SHA-256 y transformación Base64URL (sin padding).
 * - State: Prevención de ataques CSRF en flujos de autenticación.
 */
object OAuthPkceLab {

    // Aquí documentarás cómo generar estos valores manualmente
    // val codeVerifier = generateRandomCode()
    // val codeChallenge = deriveChallenge(codeVerifier)
}
