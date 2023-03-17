package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cat.copernic.gamespace.Utils.Utils.Companion.camps_buits_registre
import cat.copernic.gamespace.Utils.Utils.Companion.comprova_email_registre
import cat.copernic.gamespace.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.material.snackbar.Snackbar

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Esconder AppBar
        if(supportActionBar != null)
            supportActionBar!!.hide()

        auth= Firebase.auth

        //CLIC VOLVER INICIO DE SESIÓN
        binding.txtIniciaSesionRegistro.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        //CLIC INICIAR SESIÓN
        binding.btnCreaCuenta.setOnClickListener {
            var correo = binding.txtInputEditCorreoRegistro.text.toString()
            var contra = binding.txtInputEditContrasenaRegistro.text.toString()
            var contraRep = binding.txtInputEditRepetirContrasena.text.toString()

            //si la contrasenya coincideix amb el camp de repetir contrasenya, tots els camps estan plens i
            //la contrasenya es correcte, cridem funció per fer registre.
            if(contra.equals(contraRep)&&campoVacio(correo,contra,contraRep)&&comprova_email_registre(binding)){
                registrar(correo,contra, it)
            }else{
                camps_buits_registre(this, binding)
            }
        }
    }

    //funció per comprovar que els camps del correu electrònic, la contrasenya i el camp de repetir
    // la contrasenya no son buits.
    fun campoVacio(correo:String, contra:String, contraRep:String):Boolean{
        return correo.isNotEmpty()&&contra.isNotEmpty()&&contraRep.isNotEmpty()
    }

    //funció per registrar amb el correu electrònic
    fun registrar(correo: String, contra: String, it: View) {
        auth.createUserWithEmailAndPassword(correo,contra).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                startActivity(Intent(this,Login::class.java))
                finish()
            }else{
                val snack = Snackbar.make(it,"El registro ha fallado",Snackbar.LENGTH_LONG)
                snack.show()
            }
        }
    }


}