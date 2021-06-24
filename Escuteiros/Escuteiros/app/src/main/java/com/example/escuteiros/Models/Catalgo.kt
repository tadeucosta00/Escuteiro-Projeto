package com.example.escuteiros.Models


import org.json.JSONObject

class Catalgo {
    var id_catalogo                : Int? = null
    var id_item                    : Int? = null
    var instrucoes                 : String? = null
    var nome_catalogo                 : String? = null

    constructor() {

    }

    constructor(
        id_catalogo: Int?,
        id_item: Int?,
        instrucoes: String?,
        nome_catalogo: String?

    ) {
        this.id_catalogo                  = id_catalogo
        this.id_item                      = id_item
        this.instrucoes                   = instrucoes
        this.nome_catalogo                = nome_catalogo

    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_catalogo"                  , id_catalogo      )
        jsonObject.put("id_item"                      , id_item          )
        jsonObject.put("instrucoes"                   , instrucoes       )
        jsonObject.put("nome_catalogo"                   , nome_catalogo       )

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Catalgo {
            val catalogos = Catalgo()
            catalogos.id_catalogo          = if (!jsonObject.isNull("id_catalogo"          )) jsonObject.getInt   ("id_catalogo"          )else null
            catalogos.id_item         = if (!jsonObject.isNull("id_item"         )) jsonObject.getInt("id_item"         )else null
            catalogos.instrucoes     = if (!jsonObject.isNull("instrucoes"     )) jsonObject.getString("instrucoes"     )else null
            catalogos.nome_catalogo     = if (!jsonObject.isNull("nome_catalogo"     )) jsonObject.getString("nome_catalogo"     )else null

            return catalogos
        }
    }
}