package cat.copernic.gamespace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Fragments.pantalla_principalDirections
import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataPantallaPrincipal
import cat.copernic.gamespace.databinding.ItemPantallaPrincipalBinding

class PantallaPrincipalAdapter(private val PantallaPrincipalList:List<dataPantallaPrincipal>)  : RecyclerView.Adapter<PantallaPrincipalAdapter.gamesholder>(){

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
                binding.imageGame.setImageResource(this.img_game)
            }
            binding.CardGames.setOnClickListener{ view ->
                //view.findNavController().navigate(R.id.mostrar_videojuego)
                navigation(view, PantallaPrincipalList[position].txt_game, PantallaPrincipalList[position].img_game )

            }
        }


    }

    private fun navigation(view: View, title: String, imageResId: Int ){
        val action = pantalla_principalDirections.actionPantallaPrincipalToMostrarVideojuego(title, imageResId)
        view.findNavController().navigate(action)
    }

    override fun getItemCount(): Int = PantallaPrincipalList.size
}