package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cat.copernic.gamespace.Utils.Utils.Companion.camps_buits_registre
import cat.copernic.gamespace.Utils.Utils.Companion.comprova_email_registre
import cat.copernic.gamespace.databinding.ActivityRegistroBinding
import cat.copernic.gamespace.model.usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth
    private var bd = FirebaseFirestore.getInstance()

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
            if(contra.equals(contraRep)&&campoVacio(correo,contra,contraRep, it)&&comprova_email_registre(binding)&&contraCurta(contra, it)&&check(it)){
                registrar(correo,contra, it)
            }else{
                camps_buits_registre(this, binding)
            }
        }
    }

    //funció per comprovar que els camps del correu electrònic, la contrasenya i el camp de repetir
    // la contrasenya no son buits.
    fun campoVacio(correo:String, contra:String, contraRep:String, it: View):Boolean {
        return correo.isNotEmpty() && contra.isNotEmpty() && contraRep.isNotEmpty()
    }

    //Comprovar que la contrasenya tingui un mínim de 6 caràcters
    fun contraCurta(contra:String, it: View): Boolean{
        var curta =  false
        if (contra.length < 6) {
            Snackbar.make(it, "La contraseña es demasiado corta, mínimo 6 dígitos.", Snackbar.LENGTH_LONG).show()
        }else{
            curta = true
        }
        return curta
    }

    //Comprovar que el checkBox (dels terminis de servei i politica de privacitat) estigui seleccionat
    fun check(it: View):Boolean{
        var check = false
        if(binding.chboxAceptaTerminos.isChecked){
            check = true
        }else{
            check = false
            Snackbar.make(it, "Acepta los términos de servicio y la política de privacidad", Snackbar.LENGTH_LONG).show()
        }
        return check
    }

    //funció per registrar amb el correu electrònic
    fun registrar(correo: String, contra: String, it: View) {

        val usuari =  usuarios (
            //Guardem les dades introduïdes per l'usuari
            nombre = binding.txtInputEditNombre.text.toString(),
            apellidos = binding.txtInputEditApellidos.text.toString(),
            correo = binding.txtInputEditCorreoRegistro.text.toString(),
            admin = false
        )

        auth.createUserWithEmailAndPassword(correo,contra).addOnCompleteListener(this){task ->
            if(task.isSuccessful){

                //Creem colecció usuaris amb el document pasant l'id (correu)
                val usuariosRef =  bd.collection("Usuaris").document(usuari.correo)
                usuariosRef.set(usuari).addOnSuccessListener {  }.addOnFailureListener {  }

                startActivity(Intent(this,Login::class.java))
                finish()
            }else{
                val snack = Snackbar.make(it,"El registro ha fallado",Snackbar.LENGTH_LONG)
                snack.show()
            }
        }
    }




}