package com.mahmoudshaaban.flowing.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.wajahatkarim3.imagine.model.PhotoUrlsModel
import com.wajahatkarim3.imagine.model.UserModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoModel(
    @Expose val id: String,
    @Expose val created_at: String,
    @Expose val color: String,
    @Expose val description: String,
    @Expose val urls: PhotoUrlsModel,
    @Expose val user: UserModel
) : Parcelable
