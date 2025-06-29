package com.example.playlistmaker.Presentation.model

import android.os.Parcel
import android.os.Parcelable

data class ParcelableTrack(
    val previewUrl: String,
    val trackId: Int,
    val trackName: String, // Название композиции
    val collectionName: String, // Альбом
    val artistName: String, // Имя исполнителя
    val country: String, // Страна
    val primaryGenreName: String, // Жанр
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val releaseDate: String, // Дата релиза
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),  // Чтение id
        parcel.readString()!!,  // Чтение композиции
        parcel.readString()!!, // Чтение альбома
        parcel.readString()!!, // Чтение исполнителя
        parcel.readString()!!, // Чтение страны
        parcel.readString()!!, // Чтение жанра
        parcel.readLong(), // Чтение продолжительности
        parcel.readString()!!, // Чтение ссылки на обложку
        parcel.readString()!! // Чтение даты релиза
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(previewUrl)
        p0.writeInt(trackId)
        p0.writeString(trackName)
        p0.writeString(collectionName)
        p0.writeString(artistName)
        p0.writeString(country)
        p0.writeString(primaryGenreName)
        p0.writeLong(trackTimeMillis)
        p0.writeString(artworkUrl100)
        p0.writeString(releaseDate)
    }

    companion object CREATOR : Parcelable.Creator<ParcelableTrack>{
        override fun createFromParcel(p0: Parcel): ParcelableTrack {
            return ParcelableTrack(p0)
        }

        override fun newArray(size: Int): Array<ParcelableTrack?> {
            return arrayOfNulls<ParcelableTrack>(size)
        }

    }
}
