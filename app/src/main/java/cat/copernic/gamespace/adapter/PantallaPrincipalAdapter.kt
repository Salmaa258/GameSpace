package cat.copernic.gamespace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Fragments.pantalla_principalDirections
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.databinding.ItemPantallaPrincipalBinding
import com.bumptech.glide.Glide


class PantallaPrincipalAdapter(private var PantallaPrincipalList:MutableList<dataPantallaPrincipal>)  : RecyclerView.Adapter<PantallaPrincipalAdapter.gamesholder>(){

    private var filteredList: MutableList<dataPantallaPrincipal> = mutableListOf()

    private var originalList: List<dataPantallaPrincipal> = PantallaPrincipalList

    inner class gamesholder(val binding: ItemPantallaPrincipalBinding): RecyclerView.ViewHolder(binding.root)
    private var binding: ItemPantallaPrincipalBinding? = null


    fun filter(text: String) {
        if (text.isEmpty()) {
            PantallaPrincipalList.clear()
            PantallaPrincipalList.addAll(originalList)
            notifyDataSetChanged()
        } else {
            val filterPattern = text.lowercase().trim()
            filteredList.clear()
            for (item in originalList) {
                if (item.txt_game.lowercase().contains(filterPattern)) {
                    filteredList.add(item)
                }
            }
            PantallaPrincipalList.clear()
            PantallaPrincipalList.addAll(filteredList)
            notifyDataSetChanged()
        }
    }

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


}