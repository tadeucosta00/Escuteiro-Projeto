package com.example.escuteiros.Models

import org.json.JSONObject

class Presenca {

    var id_presenca                   : Int? = null
    var id_utilizador                 : Int? = null
    var presenca                      : String? = null
    var id_atividade                  : Int? = null

    constructor() {

    }

    constructor(
        id_presenca: Int?,
        id_utilizador: Int?,
        presenca: String?,
        id_atividade: Int?,
        ) {
        this.id_presenca                  = id_presenca
        this.id_utilizador                = id_utilizador
        this.presenca                     = presenca
        this.id_atividade                 = id_atividade
    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_presenca"                     , id_presenca         )
        jsonObject.put("id_utilizador"                   , id_utilizador       )
        jsonObject.put("presenca"                        , presenca            )
        jsonObject.put("id_atividade"                        , id_atividade            )

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Presenca {
            val presenca = Presenca()
            presenca.id_presenca          = if (!jsonObject.isNull("id_presenca"           )) jsonObject.getInt   ("id_presenca"          )else null
            presenca.id_utilizador         = if (!jsonObject.isNull("id_utilizador"        )) jsonObject.getInt("id_utilizador"         )else null
            presenca.presenca         = if (!jsonObject.isNull("presenca"                )) jsonObject.getString("presenca"         )else null
            presenca.id_atividade         = if (!jsonObject.isNull("id_atividade"                )) jsonObject.getInt("id_atividade"         )else null

            return presenca
        }
    }
}