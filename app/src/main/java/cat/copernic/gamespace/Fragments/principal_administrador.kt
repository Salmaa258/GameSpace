package cat.copernic.gamespace.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.R
import cat.copernic.gamespace.adapter.AdminAdapter
import cat.copernic.gamespace.dataLists.AdminList
import cat.copernic.gamespace.databinding.FragmentPrincipalAdministradorBinding


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [principal_administrador.newInstance] factory method to
 * create an instance of this fragment.
 */
class principal_administrador : Fragment() {

    private var _binding: FragmentPrincipalAdministradorBinding? = null
    private val binding get()= _binding!!

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
        _binding = FragmentPrincipalAdministradorBinding.inflate(inflater)
        var view = binding.root
        return view
        (requireActivity() as MainActivity).title = "Administrador"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)

        //Navegació a la pantalla d'insertar videojoc a travès d'un botó
        binding.imgInsertGameAdmin.setOnClickListener { view ->
            view.findNavController().navigate(R.id.admin_insertar_videojuego)
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

    private fun initRecyclerView(view: View) {
        //Amb el GridLayout controlem que apareixin 2 celes per fila
        binding.recyclerJuegosAdmin.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerJuegosAdmin.adapter = AdminAdapter(AdminList.AdminList.toMutableList())

    }
}