package cat.copernic.gamespace.Activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import cat.copernic.gamespace.Utils.Utils
import cat.copernic.gamespace.Utils.Utils.Companion.borrar_espais
import cat.copernic.gamespace.Utils.Utils.Companion.camps_buits_login
import cat.copernic.gamespace.Utils.Utils.Companion.comprova_email
import cat.copernic.gamespace.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Esconder AppBar
        if(supportActionBar != null)
            supportActionBar!!.hide()

        auth= Firebase.auth

        //CLIC EN REGISTRO
        binding.txtRegistro.setOnClickListener{
            startActivity(Intent(this, Registro::class.java))
            finish()
        }

        //CLIC EN CONTRASEÑA OLVIDADA
        binding.txtOlvidarContrasena.setOnClickListener {
            startActivity(Intent(this, RecuperarContrasena::class.java))
            finish()
        }


        //CLIC EN INICIAR SESIÓN
        binding.btnInicioSesion.setOnClickListener {
            var correo = binding.txtInputEditCorreo.text.toString()
            var contra = binding.txtInputEditContrasena.text.toString()

            borrar_espais(binding)

            if(correo.isNotBlank()&&contra.isNotEmpty()&&comprova_email(binding)){
                loguinar(correo, contra, it)
            }else{
                camps_buits_login(this, binding)
            }
        }
    }

    fun loguinar(correo: String, contra: String, it: View) {
        auth.signInWithEmailAndPassword(correo,contra).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                val snack = Snackbar.make(it,"El inicio de sesión ha fallado",Snackbar.LENGTH_LONG)
                snack.show()
            }
        }
    }

    //Funció que guarda la sessió
    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if (user != null) {
            var intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}
