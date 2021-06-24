package com.example.escuteiros

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.escuteiros.Models.*
import com.example.escuteiros.databinding.ActivityMenuBinding
import com.example.escuteiros.ui.calendario.calendarios
import com.example.escuteiros.ui.home.Dados
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class Menu : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding

    var atividades : MutableList<Atividades> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMenu.toolbar)

        val navigationView : NavigationView  = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.textViewNome)
        val navUserEmail : TextView = headerView.findViewById(R.id.textViewEmail)
        val navUserSair : TextView = headerView.findViewById(R.id.button4)
        var imageView : ImageView = headerView.findViewById(R.id.imageViewPost)
        navUsername.text = Dados[0].nome
        navUserEmail.text = Dados[0].email

        if(Dados[0].imagem.trim().toString() != "null") {
            Picasso.get().load(Dados[0].imagem).resize(250, 250).into(imageView)
        }
        else
        {
            Picasso.get().load("https://i.imgur.com/V9CRLdo.png").resize(250, 250).into(imageView)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_atividades, R.id.nav_calendario,R.id.nav_catalgo,R.id.nav_inventario,R.id.nav_utilizadores,R.id.nav_item
            ), drawerLayout

        )

        var menu = navigationView.getMenu()
        var  menuUtilizadores = menu.findItem(R.id.nav_utilizadores)
        var  menuItem = menu.findItem(R.id.nav_item)
        var  menuInventario = menu.findItem(R.id.nav_inventario)

        var permissao : Int = 0

        permissao = Dados[0].permissao
        if (permissao == 1)
        {
            menuUtilizadores.setVisible(false)
            menuItem.setVisible(false)
            menuInventario.setVisible(false)
        }
        else if (permissao == 2)
        {
            menuUtilizadores.setVisible(false)
        }


        navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navUserSair.setOnClickListener(){
            logout()
        }

        //binding.navView.setNavigationItemSelectedListener {
          //  when(it.itemId){
            //   R.id.nav_terminarsessao -> logout()
            //}
            //true
        //}

        //carregar datas
        val client = OkHttpClient()
        val request = Request.Builder().url("http://173.212.215.122:5000/api/atividades").build()
        client.newCall(request).execute().use { response ->

            val jsStr : String = response.body!!.string()
            println(jsStr)
            atividades.clear()
            val jsonArrayAtividades = JSONArray(jsStr)

            for ( index in  0 until jsonArrayAtividades.length()) {
                val jsonArticle : JSONObject = jsonArrayAtividades.get(index) as JSONObject
                val atividade = Atividades.fromJson(jsonArticle)
                atividades.add(atividade)
                calendarios.add(Calendario(atividades[index].nome_atividade.toString(), atividades[index].data_inicio.toString(),atividades[index].data_fim.toString(),atividades[index].local_inicio.toString(),atividades[index].tipo_atividade.toString()))
            }
        }

    }



    fun logout(){
        val preferences = getSharedPreferences("Escuteiros", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        finish()
        var intent = Intent(this, Login::class.java).apply {
        }
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}

