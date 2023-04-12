package cat.copernic.gamespace.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.R
import cat.copernic.gamespace.adapter.PantallaPrincipalAdapter
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.dataLists.PantallaPrincipalList
import cat.copernic.gamespace.databinding.FragmentPrincipalInicioBinding
import cat.copernic.gamespace.model.videojuegos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [pantalla_principal.newInstance] factory method to
 * create an instance of this fragment.
 */
class pantalla_principal : Fragment() {
    private lateinit var binding: FragmentPrincipalInicioBinding
    private lateinit var SearchView: SearchView
    private lateinit var adaptador: PantallaPrincipalAdapter
    private lateinit var JuegosList: MutableList<dataPantallaPrincipal>
    private val db = FirebaseFirestore.getInstance()

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
        binding = FragmentPrincipalInicioBinding.inflate(inflater)
        val view = binding.root
        return view
        (requireActivity() as MainActivity).title = "Inicio"


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        val recycler = binding.recyclerJuegos
        val textNoHay = binding.txtNoHay


        val juegosRef = FirebaseFirestore.getInstance().collection("Videojocs")

        juegosRef.get().addOnSuccessListener { querySnapshot ->
            val numDocumentos = querySnapshot.size()

            if (numDocumentos == 0) {
                recycler.visibility = View.GONE
                textNoHay.visibility = View.VISIBLE
            } else {
                recycler.visibility = View.VISIBLE
                textNoHay.visibility = View.GONE
                llamarRecycler()
            }
        }

    }

        fun llamarRecycler() {
        JuegosList = ArrayList<dataPantallaPrincipal>()
        adaptador = PantallaPrincipalAdapter(JuegosList)


        db.collection("Videojocs").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val wallItem = document.toObject(dataPantallaPrincipal::class.java)
                wallItem.txt_game = document["títol"].toString()

                val storageRef = FirebaseStorage.getInstance().reference.child("imatges/videojocs/${wallItem.txt_game}.jpeg")

                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    wallItem.img_game = uri.toString()
                    JuegosList.add(wallItem)
                    adaptador.notifyDataSetChanged() // Notifica al adaptador que se ha añadido un nuevo elemento
                }
            }
            binding.recyclerJuegos.adapter = adaptador
            binding.recyclerJuegos.layoutManager = GridLayoutManager(context, 2)

        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment pantalla_principal.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            pantalla_principal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initRecyclerView() {
            //Amb el GridLayout controlem que apareixin 2 celes per fila
            binding.recyclerJuegos.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerJuegos.adapter = PantallaPrincipalAdapter(PantallaPrincipalList.PantallaPrincipalList.toMutableList())

    }
}


