package cat.copernic.gamespace.Utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.util.PatternsCompat
import cat.copernic.gamespace.databinding.ActivityLoginBinding
import cat.copernic.gamespace.databinding.ActivityRegistroBinding

class Utils {
    companion object{
        //Login
        fun borrar_espais(binding: ActivityLoginBinding){
            binding.txtInputEditCorreo.text.toString().replace(" ", "")
            binding.txtInputEditContrasena.text.toString().replace(" ", "")
        }

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



    }

}