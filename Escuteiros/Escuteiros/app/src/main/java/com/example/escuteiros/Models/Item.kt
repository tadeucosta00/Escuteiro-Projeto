package com.example.escuteiros.Models

import org.json.JSONObject

class Item {
    var id_item                   : Int? = null
    var descricao_item            : String? = null
    var nome_item                 : String? = null
    var id_inventario                   : Int? = null

    constructor() {

    }

    constructor(
        id_item: Int?,
        descricao_item: String?,
        nome_item: String?,
        id_inventario: Int?

    ) {
        this.id_item                  = id_item
        this.descricao_item           = descricao_item
        this.nome_item                = nome_item
        this.id_inventario                = id_inventario
    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_item"                     , id_item         )
        jsonObject.put("descricao_item"              , descricao_item  )
        jsonObject.put("nome_item"                   , nome_item       )
        jsonObject.put("id_inventario"                   , id_inventario       )

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Item {
            val itens = Item()
            itens.id_item          = if (!jsonObject.isNull("id_item"          )) jsonObject.getInt   ("id_item"          )else null
            itens.descricao_item         = if (!jsonObject.isNull("descricao_item"         )) jsonObject.getString("descricao_item"         )else null
            itens.nome_item     = if (!jsonObject.isNull("nome_item"     )) jsonObject.getString("nome_item"     )else null
            itens.id_inventario          = if (!jsonObject.isNull("id_inventario"          )) jsonObject.getInt   ("id_inventario"          )else null

            return itens
        }
    }
}