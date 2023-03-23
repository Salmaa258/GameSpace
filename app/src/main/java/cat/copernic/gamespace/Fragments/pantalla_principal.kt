package cat.copernic.gamespace.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.R
import cat.copernic.gamespace.adapter.AdminAdapter
import cat.copernic.gamespace.adapter.PantallaPrincipalAdapter
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.dataLists.PantallaPrincipalList
import cat.copernic.gamespace.databinding.FragmentPrincipalInicioBinding


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [pantalla_principal.newInstance] factory method to
 * create an instance of this fragment.
 */
class pantalla_principal : Fragment() {
    private var _binding: FragmentPrincipalInicioBinding? = null
    private val binding get()= _binding!!
    private lateinit var SearchView: SearchView

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
        _binding = FragmentPrincipalInicioBinding.inflate(inflater)
        val view = binding.root
        return view
        (requireActivity() as MainActivity).title = "Inicio"


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        /*
        SEARCHVIEW NO FUNCIONAL
        val juegos = listOf<dataPantallaPrincipal>(
        )

        var recyclerView = binding.recyclerJuegos
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager



        var adapter = PantallaPrincipalAdapter(juegos)
        recyclerView.adapter = adapter


        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                gridLayoutManager.spanCount = if (newText.isNullOrEmpty()) 2 else 2
                return true
            }
        })*/



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
            binding.recyclerJuegos.adapter = PantallaPrincipalAdapter(PantallaPrincipalList.PantallaPrincipalList.toList())

    }
}


