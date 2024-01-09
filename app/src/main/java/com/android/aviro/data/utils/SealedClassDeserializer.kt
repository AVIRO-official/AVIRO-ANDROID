package com.android.aviro.data.entity

import android.util.Log
import com.android.aviro.data.entity.auth.AuthResponseDTO
import com.google.gson.*

import java.lang.reflect.Type



class SealedClassDeserializer<T : Any>(private val sealedClass: Class<T>) : JsonDeserializer<T> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
        Log.d("json","${json}")

        val statusCode = json.asJsonObject.get("statusCode")?.asInt
        Log.d("statusCode","${statusCode}")

        return if (statusCode != null) {
            // Decide which sealed class to use based on "statusCode" value
            if (statusCode == 200) {
                context.deserialize(json, AuthResponseDTO::class.java)
            } else {
                context.deserialize(json, AuthResponseDTO::class.java)
            }
        } else {
            // Handle missing "statusCode" field
            throw JsonParseException("Missing 'statusCode' field in the JSON object")
        }

        /*
        val item = sealedClass.classes.find {
            it.simpleName == type.split('_').joinToString("", transform = String::capitalize)
        }

         */

       // return context.deserialize(json, item)
    }
}