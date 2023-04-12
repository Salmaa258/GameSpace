package cat.copernic.gamespace.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.FragmentAdminModificarVideojuegoBinding
import cat.copernic.gamespace.databinding.FragmentEditarPerfilBinding
import cat.copernic.gamespace.model.videojuegos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [admin_modificar_videojuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_modificar_videojuego : Fragment() {
    private lateinit var binding: FragmentAdminModificarVideojuegoBinding
    private var bd = FirebaseFirestore.getInstance()
    private lateinit var juegos: videojuegos
    private val args: admin_modificar_videojuegoArgs by navArgs()


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminModificarVideojuegoBinding.inflate(inflater, container, false)
        return binding.root
        (requireActivity() as MainActivity).title = "Modificar Videojuego"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                ponerJuego()
            }
        }

        //Cridem el Spinner d'insertar
        val spinner: Spinner = binding.spinnerGeneroModificar

        // Inicializa el adaptador del Spinner con las opciones del archivo strings.xml
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.generos_juegos,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Especifica el diseño a usar cuando se despliega la lista de opciones
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Asigna el adaptador al Spinner
            spinner.adapter = adapter
        }

        //deshabilitem el camp del nom del videojoc per a que no sigui editable
        binding.txtInputEditNombreModificar.isEnabled = false

        //Navegació a la pantalla principal d'administrador a travès del botó cancelar
        binding.btnCancelarModificar.setOnClickListener { view ->
            view.findNavController().navigate(R.id.principal_administrador)
        }

        //Obrir la galeria
        binding.imgVideojuegoModificar.setOnClickListener{
            pickPhotoFromGallery()
        }

        binding.btnModificar.setOnClickListener {
            modificaJuego(view)
        }
    }

    //Obre la galeria i permet seleccionar una foto
    fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForActivityGallery.launch(intent)
    }

    //Agafa la foto de la galeria i la guarda i coloca en la mateixa imatge seleccionada
    val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            binding.imgVideojuegoModificar.setImageURI(data)

            //STORAGE
            //obtenim la referencia a la imatge que volem modificar
            val storageRef = FirebaseStorage.getInstance().reference
            //agafem el nom del videojoc que nosaltres introduirem per posar-lo al nom de l'imatge
            var text = binding.txtInputEditNombreModificar.text.toString() + ".jpeg"
            //establim la ruta on s'emmagatzemarà l'imatge i establim el nom
            val imageRef = storageRef.child("imatges/videojocs").child(text)
            if (data != null) {
                //modifiquem l'imatge
                imageRef.putFile(data).addOnSuccessListener { taskSnapshot ->
                    //l'imatge s'ha pujat correctament
                }.addOnFailureListener { exception ->
                    //s'ha produit un error
                }
            }
        }
    }

    //Llegim i posem les dades de la base de dades del videojoc
    fun ponerJuego() {

        //Leer datos
        bd.collection("Videojocs").document(args.idDocument).get().addOnSuccessListener { documentSnapshot ->
            binding.txtInputEditNombreModificar.setText(documentSnapshot.id)
            binding.txtInputEditDescripcionModificar.setText(documentSnapshot.get("descripción").toString())

            //spinner
            val genero = documentSnapshot.get("género").toString()
            val generos = resources.getStringArray(R.array.generos_juegos)
            val indiceGenero = generos.indexOf(genero)
            binding.spinnerGeneroModificar.setSelection(indiceGenero)

            //imatge
            val text = args.idDocument
            val storageRef = FirebaseStorage.getInstance().reference.child("imatges/videojocs/$text" + ".jpeg")
            val localfile = File.createTempFile("tempImage", "jpeg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imgVideojuegoModificar.setImageBitmap(bitmap)
            }
        }
    }

    //modificar datos
    fun modificaJuego(view: View){
        bd.collection("Videojocs").document(args.idDocument).set(
            hashMapOf("títol" to binding.txtInputEditNombreModificar.text.toString(),
            "descripción" to binding.txtInputEditDescripcionModificar.text.toString(),
            "género" to binding.spinnerGeneroModificar.selectedItem.toString())
        ).addOnSuccessListener {
            view.findNavController().navigate(R.id.principal_administrador)
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment admin_modificar_videojuego.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_modificar_videojuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}