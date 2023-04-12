package cat.copernic.gamespace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Fragments.pantalla_principalDirections
import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataBiblioJo
import cat.copernic.gamespace.databinding.ItemBiblioJoBinding
import cat.copernic.gamespace.databinding.ItemPantallaPrincipalBinding
import com.bumptech.glide.Glide

class BiblioJoAdapter(private val BiblioJoList:MutableList<dataBiblioJo>)  : RecyclerView.Adapter<BiblioJoAdapter.gamesholder>(){

    inner class gamesholder(val binding: ItemBiblioJoBinding): RecyclerView.ViewHolder(binding.root)
    private var binding: ItemBiblioJoBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gamesholder {
        val binding = ItemBiblioJoBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return gamesholder(binding)
    }

    override fun onBindViewHolder(holder: gamesholder, position: Int) {
        with(holder) {
            with(BiblioJoList[position]) {
                binding.txtGame.text = this.txt_game
                com.bumptech.glide.Glide.with(binding.root)
                    .load(this.img_game)
                    .into(binding.imageGame)
            }
        }
    }

    override fun getItemCount(): Int = BiblioJoList.size
}