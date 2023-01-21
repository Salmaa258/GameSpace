package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.ActivityRegistroBinding

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        //CLIC VOLVER INICIO DE SESIÓN
        binding.txtIniciaSesionRegistro.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        //CLIC INICIAR SESIÓN
        binding.btnCreaCuenta.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }
}