package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.ActivityRecuperarContrasenaBinding

class RecuperarContrasena : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperarContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarContrasenaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Esconder AppBar
        if(supportActionBar != null)
            supportActionBar!!.hide()

        //CLIC VOLVER A INICIO DE SESIÓN
        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }


    }
}