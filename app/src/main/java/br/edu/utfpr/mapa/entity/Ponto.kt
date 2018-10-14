package br.edu.utfpr.mapa.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import java.io.Serializable

@Entity
data class Ponto(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    var areaId: Int? = null,

    var latitude: Double? = null,

    var longitude: Double? = null

) : Serializable
