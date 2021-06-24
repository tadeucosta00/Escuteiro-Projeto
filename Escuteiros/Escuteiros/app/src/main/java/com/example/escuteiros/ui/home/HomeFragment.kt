package com.example.escuteiros.ui.home

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.escuteiros.Models.Data
import com.example.escuteiros.Models.Posts
import com.example.escuteiros.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


var Dados : MutableList<Data> = arrayListOf()
class HomeFragment : Fragment() {

    var listView : ListView? = null
    lateinit var adapter : PostsAdapter
    var posts : MutableList<Posts> = arrayListOf()
    lateinit var  buttonSave : Button
    lateinit var  textpost : EditText
    lateinit var builder: Notification.Builder
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val menuItem1: MenuItem = menu.findItem(R.id.itemEdit)
        menuItem1.isVisible = false

        val menuItem2: MenuItem = menu.findItem(R.id.itemVerPresenca)
        menuItem2.isVisible = false

        val menuItem: MenuItem = menu.findItem(R.id.itemAdd)
        menuItem.isVisible = false

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        buttonSave = rootView.findViewById<Button>(R.id.botaopost)
        listView = rootView.findViewById<ListView>(R.id.listViewPost)
        textpost = rootView.findViewById<EditText>(R.id.editpost)
        adapter = PostsAdapter()
        listView?.adapter = adapter

        return rootView
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nome :String = Dados[0].nome
        var id :Int = Dados[0].id
        var foto : String = Dados[0].imagem

        posts.clear()
        atualiza()

        buttonSave.setOnClickListener()
        {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)
            println(formatted)
            posts.clear()
            GlobalScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val posts = Posts(
                    null,
                    id,
                    nome,
                    textpost.text.toString(),
                    formatted,
                    foto
                )

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    posts.toJson().toString()
                )
                Log.d("coudelaria", posts.toJson().toString())
                val request = Request.Builder()
                    .url("http://173.212.215.122:5000/api/post")
                    .post(requestBody)
                    .build()
                client.newCall(request).execute().use { response ->
                    Log.d("coudelaria", response.message)
                }
            }
            val timer = object: CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }
                override fun onFinish() {
                    atualiza()
                    textpost.getText().clear()
                }
            }
            timer.start()

        }
    }

    inner class PostsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return posts.size
        }

        override fun getItem(position: Int): Any {
            return posts[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            posts.sortByDescending{it.data}
            val rowView = layoutInflater.inflate(R.layout.rowpost, parent, false)

            val textViewNome = rowView.findViewById<TextView>(R.id.textViewnNome)
            val textViewPost = rowView.findViewById<TextView>(R.id.textViewPost)
            val textViewData = rowView.findViewById<TextView>(R.id.textViewNin)
            val foto = rowView.findViewById<ImageView>(R.id.imageViewPost)

            textViewNome.text = posts[position].nome
            textViewPost.text = posts[position].post
            textViewData.text = posts[position].data

            if(posts[position].url?.trim().toString() != "null") {
                Picasso.get().load(posts[position].url).resize(250, 250).into(foto)
            }
            else
            {
                Picasso.get().load("https://i.imgur.com/V9CRLdo.png").resize(250, 250).into(foto)
            }

            return rowView
        }
    }

    fun atualiza()
    {
        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url("http://173.212.215.122:5000/api/post").build()
            client.newCall(request).execute().use { response ->
                val jsStr : String = response.body!!.string()
                println(jsStr)
                val jsonArrayPosts = JSONArray(jsStr)
                for ( index in  0 until jsonArrayPosts.length()) {
                    val jsonArticle : JSONObject = jsonArrayPosts.get(index) as JSONObject
                    val post = Posts.fromJson(jsonArticle)
                    posts.add(post)
                }
                GlobalScope.launch(Dispatchers.Main){
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}
