package cat.copernic.gamespace.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.ExifInterface
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cat.copernic.gamespace.Activitys.MainActivity

import cat.copernic.gamespace.databinding.FragmentEditarPerfilBinding
import cat.copernic.gamespace.model.usuarios
import cat.copernic.gamespace.model.videojuegos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [editar_perfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class editar_perfil : Fragment() {

    private lateinit var binding: FragmentEditarPerfilBinding
    private var bd = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var user: usuarios

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                ponerUsuario()
            }
        }

        binding.imgPerfil.setOnClickListener{
            obreCamara(context)
        }

        //deshabilitem el camp del correu electrònic per a que no sigui editable
        binding.txtInputEditCorreoPerfil.isEnabled = false

        binding.btnGuardar.setOnClickListener {
            modificaUsuari(view)
        }
    }

    fun obreCamara(context: Context?, ) {
        requireNotNull(context) { "El contexto no puede ser nulo" }
        val options = arrayOf("Tomar foto", "Eligir de galeria")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Elige una opción: ")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                }
                1 -> {
                    pickPhotoFromGallery()
                }
            }
        }
        builder.setNegativeButton("Cancelar", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    //Obre la galeria i permet seleccionar una foto
     fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        //només es mostren imatges a escollir
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    //Agafa la foto de la galeria i la guarda i coloca en la mateixa imatge seleccionada
     val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            binding.imgPerfil.setImageURI(data)

            //STORAGE
            //obtenim la referencia a la imatge que volem pujar
            val storageRef = FirebaseStorage.getInstance().reference
            //agafem el nom del usuari que nosaltres introduirem per posar-lo al nom de l'imatge
            var text =  FirebaseAuth.getInstance().currentUser?.email?: "" + ".jpeg"
            //establim la ruta on s'emmagatzemarà l'imatge i establim el nom
            val imageRef = storageRef.child("imatges/usuaris").child(text)
            if (data != null) {
                //pujem l'imatge
                imageRef.putFile(data).addOnSuccessListener { taskSnapshot ->
                    //l'imatge s'ha pujat correctament
                }.addOnFailureListener { exception ->
                    //s'ha produit un error
                }
            }
        }
    }


    //Obre camara de fotos
     val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result : ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent  = result.data
            val imageBitmap = intent?.extras?.get("data") as Bitmap
            binding.imgPerfil.setImageBitmap(imageBitmap)

            //STORAGE
            //obtenim la referencia a la imatge que volem pujar
            val storageRef = FirebaseStorage.getInstance().reference
            //agafem el nom del usuari que nosaltres introduirem per posar-lo al nom de l'imatge
            val text = FirebaseAuth.getInstance().currentUser?.email?: "" + ".jpeg"
            //establim la ruta on s'emmagatzemarà l'imatge i establim el nom
            val imagesRef = storageRef.child("imatges/usuaris").child(text)

            //convertim l'imatge en un array de bytes
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = imagesRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                //l'imatge s'ha pujat correctament
            }.addOnFailureListener {
                //s'ha produit un error
            }

        }

    }

    //Llegim i posem les dades de la base de dades del usuari
    fun ponerUsuario() {
        //Imatge
        //Establim l'instancia de Firebase de l'usuari que està autenticat
        val text = FirebaseAuth.getInstance().currentUser?.email ?: "" + ".jpeg"
        //Agafa l'instancia de Firebase amb la ruta de de colecció
        val storageRef = FirebaseStorage.getInstance().reference.child("imatges/usuaris/$text")
        //Es crea un fitxer temporal per guardar l'imatge descarregada
        val localfile = File.createTempFile("tempImage", "jpeg")
        //quan s'agafa correctamen l'imatge
        storageRef.getFile(localfile).addOnSuccessListener {

            //es decodifica el fitxer en un objecte Bitmap per poder ser mostrat
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)

            // Actualizar la vista de la imagen
            binding.imgPerfil.setImageBitmap(bitmap)
        }

        //Altres dades
        //llegim les dades del usuari i les fiquem dins els camps de l'app
        auth = Firebase.auth
        var actual = auth.currentUser
        bd.collection("Usuaris").document(actual!!.email.toString()).get().addOnSuccessListener {
            binding.txtInputEditNombrePerfil.setText(it.get("nombre").toString())
            binding.txtInputEditApellidosPerfil.setText(it.get("apellidos").toString())
            binding.txtInputEditCorreoPerfil.setText(it.get("correo").toString())
        }
    }

    fun modificaUsuari(view: View){
        var actual = auth.currentUser

        bd.collection("Usuaris").document(actual!!.email.toString()).update(
            "nombre", binding.txtInputEditNombrePerfil.text.toString(),
            "apellidos", binding.txtInputEditApellidosPerfil.text.toString()
        ).addOnSuccessListener {
            Snackbar.make(view, "El usuario se ha guardado correctamente", Snackbar.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditarPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment editar_perfil.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            editar_perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


