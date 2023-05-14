package cat.copernic.gamespace.Fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.R
import cat.copernic.gamespace.adapter.AdminAdapter
import cat.copernic.gamespace.adapter.PantallaPrincipalAdapter
import cat.copernic.gamespace.data.dataAdmin
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.dataLists.AdminList
import cat.copernic.gamespace.databinding.FragmentAdminInsertarVideojuegoBinding
import cat.copernic.gamespace.databinding.FragmentPrincipalAdministradorBinding
import cat.copernic.gamespace.model.videojuegos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.checkerframework.checker.units.qual.s


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [principal_administrador.newInstance] factory method to
 * create an instance of this fragment.
 */
class principal_administrador : Fragment() {

    private lateinit var binding: FragmentPrincipalAdministradorBinding
    private lateinit var adaptador: AdminAdapter
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userList: MutableList<dataAdmin>


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
        binding = FragmentPrincipalAdministradorBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        val recycler = binding.recyclerJuegosAdmin
        val textNoHay = binding.txtNoHayJuegos


        val juegosRef = FirebaseFirestore.getInstance().collection("Videojocs")

        juegosRef.get().addOnSuccessListener { querySnapshot ->
            val numDocumentos = querySnapshot.size()

            if (numDocumentos == 0) {
                recycler.visibility = View.GONE
                textNoHay.visibility = View.VISIBLE
            }else{
                recycler.visibility = View.VISIBLE
                textNoHay.visibility = View.GONE
                llamarRecycler()
            }

        }



        //Navegació a la pantalla d'insertar videojoc a travès d'un botó
        binding.imgInsertGameAdmin.setOnClickListener { view ->
            view.findNavController().navigate(R.id.admin_insertar_videojuego)
        }

    }

    fun llamarRecycler() {
        userList = ArrayList<dataAdmin>()
        adaptador = AdminAdapter(userList)


        db.collection("Videojocs").orderBy("títol").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val wallItem = document.toObject(dataAdmin::class.java)
                wallItem.titulo = document["títol"].toString()

                val storageRef = FirebaseStorage.getInstance().reference.child("imatges/videojocs/${wallItem.titulo}.jpeg")

                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    wallItem.imagen = uri.toString()
                    userList.add(wallItem)
                    //ordenem la llista per ordre alfabètic per als jocs que s'afegeixen nous
                    userList.sortBy { it.titulo }
                    adaptador.notifyDataSetChanged() // Notifica al adaptador que se ha añadido un nuevo elemento
                }
            }
            binding.recyclerJuegosAdmin.adapter = adaptador
            binding.recyclerJuegosAdmin.layoutManager = GridLayoutManager(context, 2)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment principal_administrador.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            principal_administrador().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initRecyclerView() {
        //Amb el GridLayout controlem que apareixin 2 celes per fila
        binding.recyclerJuegosAdmin.layoutManager = GridLayoutManager(context, 2)

    }
}