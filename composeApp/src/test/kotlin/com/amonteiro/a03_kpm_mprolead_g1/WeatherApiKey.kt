package com.amonteiro.a03_kmp_mprolead_g1

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherApiKeyTest {

    @Test
    fun `WEATHER_API_KEY est définie et non vide`() {
        val key = BuildConfig.WEATHER_API_KEY
        assertTrue(
            "WEATHER_API_KEY est vide — vérifier le secret GitHub Actions",
            key.isNotBlank()
        )
        assertFalse(
            "WEATHER_API_KEY ne doit pas être 'null' littéral",
            key == "null"
        )
    }
}