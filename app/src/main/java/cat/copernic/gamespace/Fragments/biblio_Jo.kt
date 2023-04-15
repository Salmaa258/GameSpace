package cat.copernic.gamespace.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import cat.copernic.gamespace.adapter.BiblioJoAdapter
import cat.copernic.gamespace.adapter.PantallaPrincipalAdapter
import cat.copernic.gamespace.data.dataBiblioJo
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.dataLists.BiblioJoList
import cat.copernic.gamespace.databinding.FragmentBiblioJoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Biblio_Jo.newInstance] factory method to
 * create an instance of this fragment.
 */
class Biblio_Jo : Fragment() {
    private lateinit var binding: FragmentBiblioJoBinding
    private var bd = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var adaptador: BiblioJoAdapter
    private lateinit var JuegosList: MutableList<dataBiblioJo>

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
        binding = FragmentBiblioJoBinding.inflate(inflater)
        var view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)

        auth = Firebase.auth
        var actual = auth.currentUser

        //controlem si hi ha o no videojocs i avisem
        val recycler = binding.recyclerBiblioJo
        val textNoHay = binding.txtNoHayJuegos

        //agafem la instancia de firestore de la colecció de videojocs
        val juegosRef = bd.collection("Usuaris").document(actual!!.email.toString()).collection("Juegos obtenidos")

        //agafem el numero total de videojocs que hi ha
        juegosRef.get().addOnSuccessListener { querySnapshot ->
            val numDocumentos = querySnapshot.size()

            //si el numero de videojocs es igual a 0, mostrem un misatge en la pantalla dient que no hi ha videojocs
            if (numDocumentos == 0) {
                recycler.visibility = View.GONE
                textNoHay.visibility = View.VISIBLE
                //si no, cridem una funció per mostrar els items del recycler View
            }else{
                recycler.visibility = View.VISIBLE
                textNoHay.visibility = View.GONE
                llamarRecycler()
            }
        }

    }

    fun llamarRecycler() {
        JuegosList = ArrayList<dataBiblioJo>()
        adaptador = BiblioJoAdapter(JuegosList)

        auth = Firebase.auth
        var actual = auth.currentUser

        bd.collection("Usuaris").document(actual!!.email.toString()).collection("Juegos obtenidos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val wallItem = document.toObject(dataBiblioJo::class.java)
                wallItem.txt_game = document["títol"].toString()

                val storageRef = FirebaseStorage.getInstance().reference.child("imatges/videojocs/${wallItem.txt_game}.jpeg")

                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    wallItem.img_game = uri.toString()
                    JuegosList.add(wallItem)
                    //ordenem la llista per ordre alfabètic per als jocs que s'afegeixen nous
                    JuegosList.sortBy { it.txt_game }
                    adaptador.notifyDataSetChanged() // Notifica al adaptador que se ha añadido un nuevo elemento
                }
            }
            binding.recyclerBiblioJo.adapter = adaptador
            binding.recyclerBiblioJo.layoutManager = GridLayoutManager(context, 2)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Biblio_Jo.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Biblio_Jo().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initRecyclerView(view: View) {
        //Amb el GridLayout controlem que apareixin 2 celes per fila
        binding.recyclerBiblioJo.layoutManager = GridLayoutManager(context, 2)

    }

}