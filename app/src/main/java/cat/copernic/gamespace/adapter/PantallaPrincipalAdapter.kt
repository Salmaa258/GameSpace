package cat.copernic.gamespace.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Fragments.pantalla_principalDirections
import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.dataLists.PantallaPrincipalList
import cat.copernic.gamespace.databinding.ItemPantallaPrincipalBinding
import cat.copernic.gamespace.model.videojuegos
import com.bumptech.glide.Glide
import java.util.*



class PantallaPrincipalAdapter(private val PantallaPrincipalList:MutableList<dataPantallaPrincipal>)  : RecyclerView.Adapter<PantallaPrincipalAdapter.gamesholder>(){

    /*private var filteredList: MutableList<dataPantallaPrincipal> = ArrayList(PantallaPrincipalList)

    init {
        filteredList = PantallaPrincipalList
    }*/

    inner class gamesholder(val binding: ItemPantallaPrincipalBinding): RecyclerView.ViewHolder(binding.root)
    private var binding: ItemPantallaPrincipalBinding? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gamesholder {
        val binding = ItemPantallaPrincipalBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return gamesholder(binding)
    }

    override fun onBindViewHolder(holder: gamesholder, position: Int) {
        with(holder) {
            with(PantallaPrincipalList[position]) {
                binding.txtGame.text = this.txt_game
                Glide.with(binding.root)
                    .load(this.img_game)
                    .into(binding.imageGame)
            }
            //Safe Args
            binding.CardGames.setOnClickListener{ view ->
                navigation(view, PantallaPrincipalList[position].txt_game, PantallaPrincipalList[position].img_game )
            }
        }


    }

    private fun navigation(view: View, title: String, imageResId: String ){
        val action = pantalla_principalDirections.actionPantallaPrincipalToMostrarVideojuego(title, imageResId)
        view.findNavController().navigate(action)
    }

    override fun getItemCount(): Int = PantallaPrincipalList.size

    /*fun filter(query: String) {
        filteredList.clear()
        filteredList.addAll(if (query.isEmpty()) {
            ArrayList(PantallaPrincipalList)
        } else {
            PantallaPrincipalList.filter { it.txt_game.toLowerCase().contains(query.toLowerCase()) }
        })
        notifyDataSetChanged()
    }

    fun updateList(newList: MutableList<dataPantallaPrincipal>) {
        filteredList = newList
        notifyDataSetChanged()
    }

    val searchView = findViewById<SearchView>(R.id.search_view)
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            // Aquí puedes llamar a tu función de búsqueda
            return true
        }
    })*/



}