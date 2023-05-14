package cat.copernic.gamespace.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.gamespace.Activitys.MainActivity
import cat.copernic.gamespace.adapter.viewPagerAdapter
import cat.copernic.gamespace.databinding.FragmentBibliotecaTabBinding
import com.google.android.material.tabs.TabLayoutMediator


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [biblioteca_tab.newInstance] factory method to
 * create an instance of this fragment.
 */
class biblioteca_tab : Fragment() {

    private var _binding: FragmentBibliotecaTabBinding? = null
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
        _binding = FragmentBibliotecaTabBinding.inflate(inflater)
        var view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment biblioteca_tab.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            biblioteca_tab().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    lateinit var adapter : viewPagerAdapter
    private fun initTab() {
        adapter = viewPagerAdapter(listOf<Fragment>(Biblio_Jo(), biblio_jd()) ,this.childFragmentManager,lifecycle)
        binding.pagerGames.adapter = adapter
        TabLayoutMediator(binding.tabGames, binding.pagerGames) { tab, position ->
            tab.text = when(position){
                0 -> "Juegos Obtenidos"
                1 -> "Juegos Deseados"
                else -> "Juegos Obtenidos"
            }
        }.attach()
    }
}