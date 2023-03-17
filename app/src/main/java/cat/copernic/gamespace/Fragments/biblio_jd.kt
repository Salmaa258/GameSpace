package cat.copernic.gamespace.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import cat.copernic.gamespace.adapter.BiblioJdAdapter
import cat.copernic.gamespace.dataLists.BiblioJdList
import cat.copernic.gamespace.databinding.FragmentBiblioJdBinding


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [biblio_jd.newInstance] factory method to
 * create an instance of this fragment.
 */
class biblio_jd : Fragment() {

    private var _binding: FragmentBiblioJdBinding? = null
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
        _binding = FragmentBiblioJdBinding.inflate(inflater)
        var view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment biblio_jd.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            biblio_jd().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initRecyclerView(view: View) {
        //Amb el GridLayout controlem que apareixin 2 celes per fila
        binding.recyclerBiblioJd.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerBiblioJd.adapter = BiblioJdAdapter(BiblioJdList.BiblioJdList.toList())

    }
}