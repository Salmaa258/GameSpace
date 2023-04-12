package cat.copernic.gamespace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataBiblioJd
import cat.copernic.gamespace.databinding.ItemBiblioJdBinding

class BiblioJdAdapter(private val BiblioJdList:MutableList<dataBiblioJd>)  : RecyclerView.Adapter<BiblioJdAdapter.gamesholder>(){

    inner class gamesholder(val binding: ItemBiblioJdBinding): RecyclerView.ViewHolder(binding.root)
    private var binding: ItemBiblioJdBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gamesholder {
        val binding = ItemBiblioJdBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return gamesholder(binding)
    }

    override fun onBindViewHolder(holder: gamesholder, position: Int) {
        with(holder) {
            with(BiblioJdList[position]) {
                binding.txtGame.text = this.txt_game
                com.bumptech.glide.Glide.with(binding.root)
                    .load(this.img_game)
                    .into(binding.imageGame)
            }
        }


    }

    override fun getItemCount(): Int = BiblioJdList.size
}