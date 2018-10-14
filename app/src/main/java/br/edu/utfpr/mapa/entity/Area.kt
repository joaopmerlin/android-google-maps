package br.edu.utfpr.mapa.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Area(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var nome: String? = null,

    var perimetro: Double? = null,

    var area: Double? = null,

    @Transient
    var pontos: ArrayList<Ponto> = arrayListOf()

) : Serializable
