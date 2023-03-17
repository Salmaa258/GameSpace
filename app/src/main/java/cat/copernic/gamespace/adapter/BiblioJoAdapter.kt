package cat.copernic.gamespace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.data.dataBiblioJo
import cat.copernic.gamespace.databinding.ItemBiblioJoBinding
import cat.copernic.gamespace.databinding.ItemPantallaPrincipalBinding

class BiblioJoAdapter(private val BiblioJoList:List<dataBiblioJo>)  : RecyclerView.Adapter<BiblioJoAdapter.gamesholder>(){

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
                binding.imageGame.setImageResource(this.img_game)
            }

        }


    }

    override fun getItemCount(): Int = BiblioJoList.size
}