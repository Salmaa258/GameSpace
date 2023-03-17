package cat.copernic.gamespace.dataLists

import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataBiblioJo
import cat.copernic.gamespace.data.dataPantallaPrincipal

class BiblioJoList {
    companion object{
        val BiblioJoList = listOf<dataBiblioJo>(
            dataBiblioJo(R.drawable.mando, "Titulo Videojuego"),
            dataBiblioJo(R.drawable.mando, "Titulo Videojuego2"),
            dataBiblioJo(R.drawable.mando, "Titulo Videojuego3"),

        )
    }
}