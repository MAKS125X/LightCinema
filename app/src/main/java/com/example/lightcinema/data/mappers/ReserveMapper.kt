package com.example.lightcinema.data.mappers

import com.example.lightcinema.data.visitor.network.responses.ProfileResponse
import com.example.lightcinema.data.visitor.network.responses.ReserveResponse
import com.example.lightcinema.ui.screens.profile.ProfileModel
import com.example.lightcinema.ui.screens.profile.ReserveModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object ProfileMapper : Mapper<ProfileResponse, ProfileModel> {

    override fun toModel(value: ProfileResponse): ProfileModel {
        return ProfileModel(
            value.login,
            value.reserves.map { ReserveMapper.toModel(it) }
        )
    }

    override fun fromModel(value: ProfileModel): ProfileResponse {
        TODO("Not yet implemented")
    }
}


object ReserveMapper : Mapper<ReserveResponse, ReserveModel> {
    override fun toModel(value: ReserveResponse): ReserveModel {
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDateTime.parse(value.dateTime, formatterDate).toLocalDate()
        val time = LocalDateTime.parse(value.dateTime, formatterDate).toLocalTime()
        return ReserveModel(
            value.sessionId,
            value.seatId,
            value.movieName.uppercase(Locale.getDefault()),
            value.hall,
            value.row,
            value.number,
            value.canUnreserve,
            date.format(
                DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy", Locale.getDefault()
                )
            ),
            time.format(
                DateTimeFormatter.ofPattern(
                    "HH:mm", Locale.getDefault()
                )
            )
        )

    }

    override fun fromModel(value: ReserveModel): ReserveResponse {
        TODO("Not yet implemented")
    }

}