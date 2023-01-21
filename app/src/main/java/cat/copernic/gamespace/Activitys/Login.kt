package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }





    }
}