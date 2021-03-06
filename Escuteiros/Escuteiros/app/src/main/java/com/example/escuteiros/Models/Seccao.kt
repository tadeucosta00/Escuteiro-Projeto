package com.example.escuteiros.Models

import org.json.JSONObject

class Seccao {
    var id_seccao                   : Int? = null
    var nome_seccao                 : String? = null

    constructor() {

    }

    constructor(
        id_seccao: Int?,
        nome_seccao: String?,

    ) {
        this.id_seccao                  = id_seccao
        this.nome_seccao                = nome_seccao

    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_seccao"                     , id_seccao         )
        jsonObject.put("nome_seccao"                   , nome_seccao       )


        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Seccao {
            val seccao = Seccao()
            seccao.id_seccao          = if (!jsonObject.isNull("id_seccao"          )) jsonObject.getInt   ("id_seccao"          )else null
            seccao.nome_seccao         = if (!jsonObject.isNull("nome_seccao"         )) jsonObject.getString("nome_seccao"         )else null

            return seccao
        }
    }
}