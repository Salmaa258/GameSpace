package cat.copernic.gamespace.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

import cat.copernic.gamespace.databinding.FragmentEditarPerfilBinding

// TODO: Rename parameter arguments, choose names that match
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

        binding.imgPerfil.setOnClickListener{
            obreCamara(context)
        }

    }

    fun obreCamara(context: Context?) {
        val nonNullContext = requireNotNull(context) { "El contexto no puede ser nulo" }
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
        }
    }


    //Obre camara de fotos
     val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result : ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent  = result.data
            val imageBitmap = intent?.extras?.get("data") as Bitmap
            binding.imgPerfil.setImageBitmap(imageBitmap)
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
        // TODO: Rename and change types and number of parameters
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

