package com.deontch.network.model

data class MovieCharacterResponse(
    var name: String? = null,
    var gender: String? = null,
    var culture: String? = null,
    var born: String? = null,
    var died: String? = null,
    var titles: ArrayList<String> = arrayListOf(),
    var aliases: ArrayList<String> = arrayListOf(),
    var tvSeries: ArrayList<String> = arrayListOf(),
    var playedBy: ArrayList<String> = arrayListOf()
)
