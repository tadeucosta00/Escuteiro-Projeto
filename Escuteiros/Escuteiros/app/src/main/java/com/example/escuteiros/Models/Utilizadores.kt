package com.example.escuteiros.Models

import org.json.JSONObject

class Utilizadores {



    var codId                : Int? = null
    var nomeUtilizador       : String? = null
    var email                : String? = null
    var password             : String? = null
    var morada               : String? = null
    var contato              : Int? = null
    var genero               : String? = null
    var data                 : String? = null
    var permissao            : Int? = null
    var id_secção            : Int? = null
    var nin                  : Int? = null
    var imagem               : String? = null
    var nome_grupo           : String? = null
    var inscrito             : String? = null
    var data_inscricao       : String? = null

    constructor() {

    }

    constructor(
        codId: Int?,
        nomeUtilizador: String?,
        email: String?,
        password: String?,
        nin: Int?,
        morada: String?,
        contato:Int,
        genero: String?,
        data: String?,
        permissao:Int?,
        id_secção:Int?,
        imagem:String?,
        nome_grupo: String?,
        inscrito: String?,
        data_inscricao: String?

    ) {
        this.codId                = codId
        this.nomeUtilizador       = nomeUtilizador
        this.email                = email
        this.password             = password
        this.nin                  = nin
        this.morada               = morada
        this.contato              = contato
        this.genero               = genero
        this.data                 = data
        this.permissao            = permissao
        this.id_secção            = id_secção
        this.imagem               = imagem
        this.nome_grupo           = nome_grupo
        this.inscrito             = inscrito
        this.data_inscricao       = data_inscricao
    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_utilizador"              , codId                  )
        jsonObject.put("nome"                       , nomeUtilizador         )
        jsonObject.put("email"                      , email                  )
        jsonObject.put("password"                   , password               )
        jsonObject.put("nin"                        , nin                    )
        jsonObject.put("morada"                     , morada                 )
        jsonObject.put("contato"                    , contato                )
        jsonObject.put("genero"                     , genero                 )
        jsonObject.put("imagem"                     , imagem                 )
        jsonObject.put("id_seccao"                  , id_secção              )
        jsonObject.put("permissao"                  , permissao              )
        jsonObject.put("data"                       , data                   )
        jsonObject.put("nome_grupo"                 , nome_grupo             )
        jsonObject.put("inscrito"                   , inscrito               )
        jsonObject.put("data_inscricao"             , data_inscricao         )

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Utilizadores {
            val utilizador = Utilizadores()
            utilizador.codId          = if (!jsonObject.isNull("id_utilizador"          )) jsonObject.getInt   ("id_utilizador"          )else null
            utilizador.nomeUtilizador         = if (!jsonObject.isNull("nome"         )) jsonObject.getString("nome"         )else null
            utilizador.email     = if (!jsonObject.isNull("email"     )) jsonObject.getString("email"     )else null
            utilizador.password             = if (!jsonObject.isNull("password"              )) jsonObject.getString("password"              )else null
            utilizador.nin                = if (!jsonObject.isNull("nin"                 )) jsonObject.getInt   ("nin"                 )else null
            utilizador.morada          = if (!jsonObject.isNull("morada"          )) jsonObject.getString   ("morada"          )else null
            utilizador.contato                = if (!jsonObject.isNull("contato"                 )) jsonObject.getInt   ("contato"                 )else null
            utilizador.genero         = if (!jsonObject.isNull("genero"         )) jsonObject.getString("genero"         )else null
            utilizador.imagem                = if (!jsonObject.isNull("imagem"                 )) jsonObject.getString  ("imagem"           )else null
            utilizador.id_secção             = if (!jsonObject.isNull("id_seccao"              )) jsonObject.getInt("id_seccao"              )else null
            utilizador.permissao             = if (!jsonObject.isNull("permissao"              )) jsonObject.getInt("permissao"              )else null
            utilizador.data     = if (!jsonObject.isNull("data"     )) jsonObject.getString("data"     )else null
            utilizador.nome_grupo     = if (!jsonObject.isNull("nome_grupo"     )) jsonObject.getString("nome_grupo"     )else null
            utilizador.inscrito     = if (!jsonObject.isNull("inscrito"     )) jsonObject.getString("inscrito"     )else null
            utilizador.data_inscricao     = if (!jsonObject.isNull("data_inscricao"     )) jsonObject.getString("data_inscricao"     )else null

            return utilizador
        }
    }

}