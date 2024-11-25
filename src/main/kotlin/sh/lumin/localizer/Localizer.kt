package sh.lumin.localizer

import kotlinx.serialization.json.Json
import java.io.File
import java.util.Locale

/**
 * Localization manager supporting multiple file formats (JSON and TOML)
 * Handles loading translations and retrieving messages by ID for different locales
 */
class Localizer {
    // Internal storage of translations
    private val translations: MutableMap<Locale, Map<String, String>> = mutableMapOf()

    /**
     * Load translations from a JSON file
     * @param file File containing translations
     * @param locale Locale for these translations
     */
    fun loadFromJson(file: File, locale: Locale) {
        val jsonText = file.readText()
        val jsonTranslations = Json.decodeFromString<Map<String, String>>(jsonText)
        translations[locale] = jsonTranslations
    }

    /**
     * Get a localized message by its ID
     * @param id Message identifier
     * @param locale Desired locale (defaults to system locale)
     * @param fallbackLocale Fallback locale if translation not found (defaults to English)
     * @return Localized message or the ID if no translation found
     */
    fun getMessage(
        id: String,
        locale: Locale = Locale.getDefault(),
        fallbackLocale: Locale = Locale.ENGLISH
    ): String {
        // Try direct locale match
        translations[locale]?.let {
            return it[id] ?: ""
        }

        // Try fallback locale
        translations[fallbackLocale]?.let {
            return it[id] ?: ""
        }

        // Return ID if no translation found
        return id
    }

    /**
     * Format a message with parameters
     * @param id Message identifier
     * @param params Map of parameter replacements
     * @param locale Desired locale
     * @return Formatted localized message
     */
    fun formatMessage(
        id: String,
        params: Map<String, Any> = emptyMap(),
        locale: Locale = Locale.getDefault()
    ): String {
        var message = getMessage(id, locale)

        params.forEach { (key, value) ->
            message = message.replace("{$key}", value.toString())
        }

        return message
    }

    /**
     * Check if a translation exists for a given ID and locale
     * @param id Message identifier
     * @param locale Locale to check
     * @return Boolean indicating if translation exists
     */
    fun hasTranslation(id: String, locale: Locale = Locale.getDefault()): Boolean {
        return translations[locale]?.containsKey(id) == true
    }

    /**
     * Get all available locales
     * @return Set of loaded locales
     */
    fun getAvailableLocales(): Set<Locale> = translations.keys.toSet()
}