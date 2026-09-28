package kz.zhb.prayer.impl.model

/** Населённый пункт из справочника муфтията. lat/lng — строки как в справочнике. */
internal data class City(
    val id: Long,
    val title: String,
    val lat: String,
    val lng: String,
)
