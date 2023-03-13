package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import cat.copernic.gamespace.Utils.Utils
import cat.copernic.gamespace.databinding.ActivityRecuperarContrasenaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecuperarContrasena : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperarContrasenaBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarContrasenaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Esconder AppBar
        if(supportActionBar != null)
            supportActionBar!!.hide()

        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        //CLIC VOLVER A INICIO DE SESIÓN
        binding.btnCancelar.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        //CLIC EN ENVIAR PARA RESTABLECER LA CONTRASEÑA
        binding.btnEnviar.setOnClickListener {
            if(Utils.comprova_email_recuperarContra(binding)){
                val email = binding.txtInputEditCorreoRetablecerContrasena.text.toString()
                resetPassword(email)
            }

        }
    }

    //Funció que envia el correu per poder cambiar la contrasenya
    fun resetPassword(correu: String){
        val builder = AlertDialog.Builder(this)

        auth.setLanguageCode("es")
        auth.sendPasswordResetEmail(correu).addOnCompleteListener { task ->

            if(task.isSuccessful()){
                builder.setTitle("Perfecto!")
                builder.setMessage("Se ha enviado el Email con éxito, dirígete a tu Email.")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }else{
                builder.setTitle("Error")
                builder.setMessage("No se ha podido enviar el Correo, porfavor introdúzcalo de nuevo.")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }
        }
    }


}