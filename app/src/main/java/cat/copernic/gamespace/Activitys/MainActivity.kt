package cat.copernic.gamespace.Activitys

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.ActivityMainBinding
import cat.copernic.gamespace.model.usuarios
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = FirebaseFirestore.getInstance() // Función para obtener el usuario actual
        isAdmin = currentUser.equals(isAdmin)

        //Menú lateral
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.pantalla_principal,
                R.id.editar_perfil,
                R.id.biblioteca_tab,
                R.id.principal_administrador
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //Funció per tancar Sessió
        val signout = binding.navView.menu.findItem(R.id.CerrarSesion)
        signout.setOnMenuItemClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
            true
        }


        //Comprovem si l'usuari es administrador per ocultar l'item del menú administrador o no
        //Obtenim la instància de Firebase
        val db = FirebaseFirestore.getInstance()
        //Obtenim l'usuari actualment autenticat
        val userId = FirebaseAuth.getInstance().currentUser

        //Verifiquem l'autenticació
        if (userId != null) {
            //accedim a la colecció usuaris y comprovem la id (no pot ser nula)
            db.collection("Usuaris").document(userId.email!!)
                //operació de lectura
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    //verifiquem si existeix el document
                    if (documentSnapshot.exists()) {
                        //agafem l'objecte d'usuaris
                        val usuario = documentSnapshot.toObject(usuarios::class.java)
                        val esAdmin = usuario?.admin ?: false
                        //fem referencia al menu
                        val menu = binding.navView.menu
                        //posem l'item visible dependen de si es admin o no
                        menu.findItem(R.id.principal_administrador).isVisible = esAdmin
                    }
                }
        }

        mostrarDialogoValoracion()
    }

    private fun mostrarDialogoValoracion() {
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val dialogShown = sharedPrefs.getBoolean("dialogShown", false)

        if (!dialogShown) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Te gusta esta aplicación? Valórala en Google Play y ayuda a otros a descubrirla")
                .setPositiveButton("Valorar") { _, _ ->
                    irAValoracion()
                    // Guardar el indicador de que se mostró el diálogo
                    sharedPrefs.edit().putBoolean("dialogShown", true).apply()
                }
                .setNegativeButton("Ahora no") { _, _ ->
                    // Guardar el indicador de que se mostró el diálogo
                    sharedPrefs.edit().putBoolean("dialogShown", true).apply()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun irAValoracion() {
        val packageName = packageName // Obtiene el nombre del paquete de la aplicación actual
        val playStoreLink = "market://details?id=$packageName" // Crea el enlace a la página de valoración de Google Play

        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink)))
        } catch (e: ActivityNotFoundException) {
            // En caso de que Google Play Store no esté instalado en el dispositivo, abrir la página de Google Play en el navegador
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

}