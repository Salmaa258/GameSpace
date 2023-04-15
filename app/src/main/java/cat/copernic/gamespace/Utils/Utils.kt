package cat.copernic.gamespace.Utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.util.PatternsCompat
import cat.copernic.gamespace.Fragments.admin_insertar_videojuego
import cat.copernic.gamespace.databinding.ActivityLoginBinding
import cat.copernic.gamespace.databinding.ActivityRecuperarContrasenaBinding
import cat.copernic.gamespace.databinding.ActivityRegistroBinding

class Utils {
    companion object{
        //Login
        //Elimina els espais en blanc
        fun borrar_espais(binding: ActivityLoginBinding){
            binding.txtInputEditCorreo.text.toString().replace(" ", "")
            binding.txtInputEditContrasena.text.toString().replace(" ", "")
        }

        //Comprova que no hagui camps buits
        fun camps_buits_login(context: Context, binding: ActivityLoginBinding){
            var correo = binding.txtInputEditCorreo.text.toString()
            var contra = binding.txtInputEditContrasena.text.toString()

            val builder = AlertDialog.Builder(context)

            if(correo.isBlank()&&contra.isNotBlank()){
                builder.setTitle("Error")
                builder.setMessage("El correo no puede estar vacío")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }else if (correo.isNotBlank()&&contra.isBlank()){
                builder.setTitle("Error")
                builder.setMessage("La contraseña no puede estar vacía")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }else if (correo.isBlank()&&contra.isBlank()){
                builder.setTitle("Error")
                builder.setMessage("Los campos no pueden estar vacios")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }
        }

        //Comprova que el correu electrònic sigui vàlid
        fun comprova_email(binding: ActivityLoginBinding): Boolean {
            val email = binding.txtInputEditCorreo.text.toString()
            return if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                binding.txtInputEditCorreo.error = "El correo no es válido"
                false
            }else{
                binding.txtInputEditCorreo.error = null
                true
            }
        }

        //Registro
        //Comprova que no hagui camps buits
        fun camps_buits_registre(context: Context, binding: ActivityRegistroBinding){
            var correo = binding.txtInputEditCorreoRegistro.text.toString()
            var contra = binding.txtInputEditContrasenaRegistro.text.toString()
            var contraRep = binding.txtInputEditRepetirContrasena.text.toString()

            val builder = AlertDialog.Builder(context)

            if(correo.isBlank()&&contra.isNotEmpty()){
                builder.setTitle("Error")
                builder.setMessage("El correo no puede estar vacío")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }else if(!contra.equals(contraRep)){
                builder.setTitle("Error")
                builder.setMessage("La contraseña no coincide")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }else if(contra.isBlank()&&correo.isNotEmpty()){
                builder.setTitle("Error")
                builder.setMessage("La contraseña no puede estar vacía")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }else if(correo.isEmpty()&&contra.isEmpty()){
                builder.setTitle("Error")
                builder.setMessage("Los campos no pueden estar vacíos")
                builder.setPositiveButton("Aceptar", null)
                val dialog = builder.create()
                dialog.show()
            }
        }

        //Comprova que el correu electrònic sigui vàlid
        fun comprova_email_registre(binding: ActivityRegistroBinding): Boolean {
            val email = binding.txtInputEditCorreoRegistro.text.toString()
            return if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                binding.txtInputEditCorreoRegistro.error = "El correo no es válido"
                false
            }else{
                binding.txtInputEditCorreoRegistro.error = null
                true
            }
        }

        //Comprova que el correu electrònic sigui vàlid
        fun comprova_email_recuperarContra(binding: ActivityRecuperarContrasenaBinding): Boolean {
            val email = binding.txtInputEditCorreoRetablecerContrasena.text.toString()
            return if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                binding.txtInputEditCorreoRetablecerContrasena.error = "El correo no es válido"
                false
            }else{
                binding.txtInputEditCorreoRetablecerContrasena.error = null
                true
            }
        }

        //Comprovar que el camp de Titol a l'hora d'insertar el videjoc no està buit.
        fun titolNull(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("El título del videojuego no puede estar vacío")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
        }

        //Avisar si un joc existeix
        fun jocExistent(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("El videojuego ya existe")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
        }

        //Comprovar que el camp de l'imatge a l'hora d'insertar el videjoc no està buit.
        fun imatgeNull(context: Context){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Error")
            builder.setMessage("Inserta una imágen para la portada del videojuego")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
        }


    }

}