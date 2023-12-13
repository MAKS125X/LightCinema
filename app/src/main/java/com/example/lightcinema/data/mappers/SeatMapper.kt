package com.example.lightcinema.data.mappers

import androidx.compose.runtime.mutableStateOf
import com.example.lightcinema.data.visitor.network.responses.SeatsCollectionResponse
import com.example.lightcinema.ui.screens.cinemahall.SeatModel
import com.example.lightcinema.ui.screens.cinemahall.SeatsModelCollection

object SeatMapper : Mapper<SeatsCollectionResponse, SeatsModelCollection> {
    override fun toModel(value: SeatsCollectionResponse): SeatsModelCollection {
        val prices = value.seats.map { it.price }.toSet()
        val baseCost = prices.minOrNull() ?: 0
        val vipCost = prices.maxOrNull() ?: 0

        val seatModels = value.seats.groupBy { it.row }
            .mapValues { (_, seats) ->
                seats.sortedBy { it.number }.map { seatResponse ->
                    SeatModel(
                        id = seatResponse.id,
                        row = seatResponse.row,
                        number = seatResponse.number,
                        isVip = seatResponse.price == vipCost,
                        reserved = seatResponse.reserved,
                        selected = mutableStateOf(false)
                    )
                }.toTypedArray()
            }
            .toSortedMap()

        val seatsMatrix = seatModels.values.toTypedArray()

        return SeatsModelCollection(seatsMatrix, baseCost, vipCost)
    }

    override fun fromModel(value: SeatsModelCollection): SeatsCollectionResponse {
        TODO("Not yet implemented")
    }
}