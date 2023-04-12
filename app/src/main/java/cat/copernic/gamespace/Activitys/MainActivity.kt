package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.ActivityMainBinding
import cat.copernic.gamespace.model.usuarios
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseAuth.getInstance()
    private var bd = FirebaseFirestore.getInstance()


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
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

}