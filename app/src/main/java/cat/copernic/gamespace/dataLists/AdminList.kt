package cat.copernic.gamespace.dataLists

import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataAdmin

class AdminList {
    companion object{
        val AdminList = listOf<dataAdmin>(
            dataAdmin(R.drawable.mando, "Titulo Videojuego"),
            dataAdmin(R.drawable.mando, "Titulo Videojuego2"),
            dataAdmin(R.drawable.mando, "Titulo Videojuego3")

            ).toMutableList()
    }
}