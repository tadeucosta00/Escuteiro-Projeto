package com.example.escuteiros.Models

import org.json.JSONObject

class Inventario {
    var id_inventario                 : Int? = null
    var nome_inventario               : String? = null

    constructor() {

    }

    constructor(
        id_inventario: Int?,
        nome_inventario : String?


    ) {
        this.id_inventario          = id_inventario
        this.nome_inventario        = nome_inventario

    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_inventario"               , id_inventario       )
        jsonObject.put("nome_inventario"             , nome_inventario     )


        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Inventario {
            val inventarios = Inventario()
            inventarios.id_inventario          = if (!jsonObject.isNull("id_inventario"        )) jsonObject.getInt   ("id_inventario"      )else null
            inventarios.nome_inventario        = if (!jsonObject.isNull("nome_inventario"     )) jsonObject.getString("nome_inventario"   )else null

            return inventarios
        }
    }
}