package com.daisy.videostreamapp.domain.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.media3.common.MediaItem
import com.daisy.videostreamapp.data.remote.ApiPath

data class VideoItem(
    val description: String?,

    val subtitle: String,

    val sources: List<String>,

    val thumb: String?,

    val title: String,
) : Parcelable {
    val fullThumbUrl: String?
        get() = thumb?.let { "${ApiPath.IMAGE_BASE_URL}$it" }

    val mediaItem: List<MediaItem>
        get() = sources.map { uri -> MediaItem.fromUri(uri) }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString(),
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(subtitle)
        parcel.writeStringList(sources)
        parcel.writeString(thumb)
        parcel.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<VideoItem> {
        override fun createFromParcel(parcel: Parcel): VideoItem {
            return VideoItem(parcel)
        }

        override fun newArray(size: Int): Array<VideoItem?> {
            return arrayOfNulls(size)
        }
    }
}
