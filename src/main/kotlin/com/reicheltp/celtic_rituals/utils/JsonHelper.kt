package com.reicheltp.celtic_rituals.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.Integer.parseInt

/**
 * Parses a json element to a color represented as int.
 *
 * Thus, 16253176 and "F800F8" are both valid colors.
 */
val JsonElement.asColor: Int
    get() {
        if (!this.isJsonPrimitive) {
            throw JsonParseException("color should be number or string")
        }

        val value = this.asJsonPrimitive

        return when {
            value.isNumber -> value.asInt
            value.isString -> parseInt(value.asString, 16)
            else -> throw JsonParseException("color should be number or string")
        }
    }
