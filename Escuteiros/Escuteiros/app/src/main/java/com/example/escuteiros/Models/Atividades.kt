package com.example.escuteiros.Models

import org.json.JSONObject

class Atividades {

    var id_atividade              : Int? = null
    var id_localizacao            : Int? = null
    var id_inventario             : Int? = null
    var id_calendario             : Int? = null
    var id_utilizador             : Int? = null
    var nome_atividade            : String? = null
    var tipo_atividade            : String? = null
    var descricao                 : String? = null
    var historico_atividade       : String? = null
    var data_inicio               : String? = null
    var data_fim                  : String? = null
    var custo                     : Int? = null
    var local_inicio              : String? = null
    var local_fim                 : String? = null

    constructor() {

    }

    constructor(
         id_atividade            : Int?,
         id_localizacao          : Int?,
         id_inventario           : Int?,
         id_calendario           : Int?,
         id_utilizador           : Int?,
         nome_atividade          : String?,
         tipo_atividade          : String?,
         descricao               : String?,
         historico_atividade     : String?,
         data_inicio             : String?,
         data_fim                : String?,
         custo                   : Int?,
         local_inicio            : String?,
         local_fim               : String?

    ) {
        this.id_atividade                = id_atividade
        this.id_localizacao              = id_localizacao
        this.id_inventario               = id_inventario
        this.id_calendario               = id_calendario
        this.id_utilizador               = id_utilizador
        this.nome_atividade              = nome_atividade
        this.tipo_atividade              = tipo_atividade
        this.descricao                   = descricao
        this.historico_atividade         = historico_atividade
        this.data_inicio                 = data_inicio
        this.data_fim                    = data_fim
        this.custo                       = custo
        this.local_inicio                = local_inicio
        this.local_fim                   = local_fim



    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_atividade"               , id_atividade                  )
        jsonObject.put("id_localizacao"               , id_localizacao         )
        jsonObject.put("id_inventario"               , id_inventario                  )
        jsonObject.put("id_calendario"               , id_calendario               )
        jsonObject.put("id_utilizador"               , id_utilizador                    )
        jsonObject.put("nome_atividade"               , nome_atividade                 )
        jsonObject.put("tipo_atividade"               , tipo_atividade                )
        jsonObject.put("descricao"               , descricao                 )
        jsonObject.put("historico_atividade"               , historico_atividade                 )
        jsonObject.put("data_inicio"               , data_inicio               )
        jsonObject.put("data_fim"               , data_fim              )
        jsonObject.put("custo"               , custo              )
        jsonObject.put("local_inicio"               , local_inicio                   )
        jsonObject.put("local_fim"               , local_fim                   )

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Atividades {
            val atividade = Atividades()
            atividade.id_atividade  =      if (!jsonObject.isNull("id_atividade"          )) jsonObject.getInt   ("id_atividade"          )else null
            atividade.id_localizacao            = if (!jsonObject.isNull("id_localizacao"         )) jsonObject.getInt("id_localizacao"         )else null
            atividade.id_inventario       = if (!jsonObject.isNull("id_inventario"     )) jsonObject.getInt("id_inventario"     )else null
            atividade.id_calendario           = if (!jsonObject.isNull("id_calendario"              )) jsonObject.getInt("id_calendario"              )else null
            atividade.id_utilizador         = if (!jsonObject.isNull("id_utilizador"                 )) jsonObject.getInt   ("id_utilizador"                 )else null
            atividade.nome_atividade      = if (!jsonObject.isNull("nome_atividade"          )) jsonObject.getString   ("nome_atividade"          )else null
            atividade.tipo_atividade            = if (!jsonObject.isNull("tipo_atividade"                 )) jsonObject.getString   ("tipo_atividade"                 )else null
            atividade.descricao                 = if (!jsonObject.isNull("descricao"         )) jsonObject.getString("descricao"         )else null
            atividade.historico_atividade      = if (!jsonObject.isNull("historico_atividade"                 )) jsonObject.getString  ("historico_atividade"           )else null
            atividade.data_inicio                = if (!jsonObject.isNull("data_inicio"                 )) jsonObject.getString   ("data_inicio"           )else null
            atividade.data_fim                 = if (!jsonObject.isNull("data_fim"              )) jsonObject.getString("data_fim"              )else null
            atividade.custo                    = if (!jsonObject.isNull("custo"              )) jsonObject.getInt("custo"              )else null
            atividade.local_inicio            = if (!jsonObject.isNull("local_inicio"     )) jsonObject.getString("local_inicio"     )else null
            atividade.local_fim                = if (!jsonObject.isNull("local_fim"     )) jsonObject.getString("local_fim"     )else null

            return atividade
        }
    }
}