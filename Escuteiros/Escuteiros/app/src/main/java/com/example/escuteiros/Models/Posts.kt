package com.example.escuteiros.Models

import org.json.JSONObject

class Posts {
    var idPost                : Int? = null
    var id                    : Int? = null
    var nome                  : String? = null
    var post                  : String? = null
    var data                  : String? = null
    var url                  : String? = null
    constructor() {

    }

    constructor(
        idPost: Int?,
        id: Int?,
        nome: String?,
        post: String?,
        data: String?,
        url: String?

    ) {
        this.idPost                = idPost
        this.id               = id
        this.nome                = nome
        this.post             = post
        this.data             = data
        this.url             = url
    }

    fun toJson() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put("id_post"                  , idPost           )
        jsonObject.put("id_utilizador"             , id          )
        jsonObject.put("nome_utilizador"         , nome      )
        jsonObject.put("post"                  , post              )
        jsonObject.put("date"                  , data              )
        jsonObject.put("url"                  , url              )

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject) : Posts {
            val posts = Posts()
            posts.idPost          = if (!jsonObject.isNull("id_post"          )) jsonObject.getInt   ("id_post"          )else null
            posts.id         = if (!jsonObject.isNull("id_utilizador"         )) jsonObject.getInt("id_utilizador"         )else null
            posts.nome     = if (!jsonObject.isNull("nome_utilizador"     )) jsonObject.getString("nome_utilizador"     )else null
            posts.post             = if (!jsonObject.isNull("post"              )) jsonObject.getString("post"              )else null
            posts.data             = if (!jsonObject.isNull("date"              )) jsonObject.getString("date"              )else null
            posts.url             = if (!jsonObject.isNull("url"              )) jsonObject.getString("url"              )else null

            return posts
        }
    }
}