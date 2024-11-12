package ui.utils

val translations = mapOf(
    "email" to mapOf (
    "English" to "email",
    "Français" to "courriel",
    "Español" to " correo electrónico"
    ),

     "password" to mapOf(
    "English" to "Password",
    "Français" to "Mot de passe",
    "Español" to "Contraseña"
),


 "login" to mapOf (
    "English" to "Log in",
    "Français" to "Se connecter",
    "Español" to "Iniciar Sesión"

),
 "forgot_password" to mapOf (
    "English" to "Forgot password?",
    "Français" to "Mot de passe oublié ?",
    "Español" to "¿Olvidaste tu contraseña?"
    ),

"dont_have_an_account" to mapOf(
    "English" to "Don't have an account?",
    "Français" to "Vous n'avez pas de compte ?",
    "Español" to "¿No tienes una cuenta?"
    ),

"sign_up" to mapOf (
    "English" to "Sign up",
    "Français" to "S'inscrire",
    "Español" to "Registrarse"
 ),

    "welcome_to_jobder" to mapOf(
        "English" to "Welcome to JobAlba",
        "Français" to "Bienvenue à JobAlba",
        "Español" to "Bienvenido a JobAlba"
    ),
    "join_as_a_user" to mapOf(
        "English" to "Join as a user",
        "Français" to "Rejoindre en tant qu'utilisateur",
        "Español" to "Unirse como usuario"
    ),
    "join_as_a_company" to mapOf(
        "English" to "Join as a Company",
        "Français" to "Rejoindre en tant qu'entreprise",
        "Español" to "Unirse como empresa"
    ),
    "forgot_password" to mapOf(
        "English" to "Forgot password?",
        "Français" to "Mot de passe oublié ?",
        "Español" to "¿Olvidaste tu contraseña?"
    ),
    // Agrega aquí todas las frases y palabras que necesites
)

fun getTranslation(key: String, language: String): String {
    return translations[key]?.get(language) ?: "Translation not found"
}