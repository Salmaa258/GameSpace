package cat.copernic.gamespace.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.R
import cat.copernic.gamespace.Utils.Utils
import cat.copernic.gamespace.databinding.FragmentAdminInsertarVideojuegoBinding
import cat.copernic.gamespace.model.videojuegos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [admin_insertar_videojuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_insertar_videojuego : Fragment() {
    private lateinit var binding: FragmentAdminInsertarVideojuegoBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        auth= Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Cridem el Spinner d'insertar
        val spinner: Spinner = binding.spinnerGeneroInsertar

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
        

        //Navegació a la pantalla principal d'administrador a travès del botó cancelar
        binding.btnCancelarInsert.setOnClickListener { view ->
            view.findNavController().navigate(R.id.principal_administrador)
        }

        binding.btnCrear.setOnClickListener {
            //Assignem els camps als atributs del objecte videojoc
            val juego = videojuegos(
                títol = binding.txtInputEditNombreInsertar.text.toString(),
                descripción = binding.txtInputEditDescripcionInsertar.text.toString(),
                género = binding.spinnerGeneroInsertar.selectedItem.toString()
            )

            //Comprovem que el camp titol no sigui null
            if(juego.títol.isNullOrEmpty()){
                //Cridem la funció que es troba al Utils per mostrar el AlertDialog
                Utils.titolNull(requireContext())
            }else{
                db.collection("Videojocs").document(juego.títol).get()
                .addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot.exists()){
                        Utils.jocExistent(requireContext())
                    }else{
                        //Creem la colecció "videojocs", establim document amb el nom del joc i li afegim els atributs corresponents
                         db.collection("Videojocs").document(juego.títol).set(juego)
                         .addOnSuccessListener {
                         //tornem a la pantalla principal de l'administrador
                         view.findNavController().navigate(R.id.principal_administrador)
                         }
                    }
                }
            }
        }

        //Obrir la galeria
        binding.imgVideojuego.setOnClickListener{
            pickPhotoFromGallery()
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
            binding.imgVideojuego.setImageURI(data)

            //STORAGE
            //obtenim la referencia a la imatge que volem pujar
            val storageRef = FirebaseStorage.getInstance().reference
            //agafem el nom del videojoc que nosaltres introduirem per posar-lo al nom de l'imatge
            var text = binding.txtInputEditNombreInsertar.text.toString() + ".jpeg"
            //establim la ruta on s'emmagatzemarà l'imatge i establim el nom
            val imageRef = storageRef.child("imatges/videojocs").child(text)
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminInsertarVideojuegoBinding.inflate(inflater, container, false)
        return binding.root
        (requireActivity() as MainActivity).title = "Insertar Videojuego"


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment admin_insertar_videojuego.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_insertar_videojuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}

