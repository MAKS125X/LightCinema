package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.toModel
import com.example.lightcinema.data.mappers.MovieMapper
import com.example.lightcinema.data.mappers.ProfileMapper
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.network.responses.MovieItemResponse
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.data.visitor.network.responses.ProfileResponse
import com.example.lightcinema.data.visitor.network.responses.ReserveResponse
import com.example.lightcinema.data.visitor.network.responses.SessionDateResponse
import com.example.lightcinema.data.visitor.network.responses.SessionTimeResponse
import com.example.lightcinema.ui.screens.visitor.movie_info.MovieModel
import com.example.lightcinema.ui.screens.visitor.profile.ProfileModel
import com.example.lightcinema.ui.screens.visitor.reserving_screen.AnotherSessionModel
import com.example.lightcinema.ui.screens.visitor.reserving_screen.SeatsModelCollection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody

class VisitorRepositoryMock(
    override val remoteDataSource: VisitorService
) : VisitorRepository {

    override suspend fun getMovieCollection(
        withSession: Boolean,
        date: String
    ): Flow<ApiResponse<MovieCollectionResponse>> {
//        return MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Success())
        return flow {
            emit(ApiResponse.Loading)
            delay(2000L)
            if (date == "Today") {
                emit(
                    ApiResponse.Success(
                        MovieCollectionResponse(
                            listOf(
                                MovieItemResponse(
                                    1,
                                    "khjgn",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "khjgn",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "khjgn",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "khjgn",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "khjgn",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                            )
                        )
                    )
                )
            } else if (date == "Tomorrow") {
                emit(
                    ApiResponse.Success(
                        MovieCollectionResponse(
                            listOf(
                                MovieItemResponse(
                                    1,
                                    "gfdlkfhgjkl",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "gfdlkfhgjkl",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "gfdlkfhgjkl",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "gfdlkfhgjkl",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "gfdlkfhgjkl",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                            )
                        )
                    )
                )
            } else {
                emit(
                    ApiResponse.Success(
                        MovieCollectionResponse(
                            listOf(
                                MovieItemResponse(
                                    1,
                                    "asdasdasd",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "asdasdasd",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "asdasdasd",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "asdasdasd",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                                MovieItemResponse(
                                    1,
                                    "asdasdasd",
                                    listOf("комедия", "взрослое кино", "пантомима"),
                                    "https://sun9-71.userapi.com/impg/ck60z-8Y_vF8B5urhqeQoifuShrfSpdKGC4g6w/1jVcP3dUl50.jpg?size=729x1080&quality=96&sign=e005de3b8f377ab81d270111c896f16f&c_uniq_tag=Kis5yX7bgJvKFm7TS5ZCrg5X28Mb9VC6uIAf3mLe98g&type=album",
                                    listOf(
                                        SessionTimeResponse(1, "9:00", 300),
                                        SessionTimeResponse(2, "11:00", 300),
                                        SessionTimeResponse(3, "13:00", 300),
                                        SessionTimeResponse(4, "15:00", 300),
                                        SessionTimeResponse(5, "17:00", 300),
                                        SessionTimeResponse(6, "19:00", 300),
                                        SessionTimeResponse(7, "21:00", 300),
                                        SessionTimeResponse(8, "23:00", 300),
                                    )
                                ),
                            )
                        )
                    )
                )
            }
        }
    }

    override suspend fun getMovieInfo(
        id: Int
    ): Flow<ApiResponse<MovieModel>> {
        return flow {
            emit(ApiResponse.Loading)
            delay(2000L)
            emit(
                ApiResponse.Success(
                    MovieLongResponse(
                        1,
                        "Drive",
                        "Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание",
                        listOf("комедия", "взрослое кино", "пантомима"),
                        2011,
                        listOf("Россия"),
                        true,
                        "https://4.bp.blogspot.com/-pMdtPxE2iEk/Ty8RwlalpCI/AAAAAAAAF18/UaaLXMQIwCI/s1600/driceempire.jpg",
                        listOf(
                            SessionDateResponse(
                                1,
                                "2023-12-10 12:00",
                                300
                            ),
                            SessionDateResponse(
                                2,
                                "2023-12-10 14:00",
                                300
                            ),
                            SessionDateResponse(
                                3,
                                "2023-12-11 12:00",
                                300
                            ),
                            SessionDateResponse(
                                4,
                                "2023-12-11 14:00",
                                300
                            ),
                            SessionDateResponse(
                                5,
                                "2023-12-10 16:00",
                                400
                            ),
                            SessionDateResponse(
                                6,
                                "2023-12-11 16:00",
                                500
                            ),
                            SessionDateResponse(
                                5,
                                "2023-12-10 18:00",
                                400
                            ),
                            SessionDateResponse(
                                6,
                                "2023-12-11 18:00",
                                500
                            ),
                            SessionDateResponse(
                                5,
                                "2023-12-10 20:00",
                                400
                            ),
                            SessionDateResponse(
                                6,
                                "2023-12-11 20:00",
                                500
                            ),
                            SessionDateResponse(
                                5,
                                "2023-12-10 22:00",
                                400
                            ),
                            SessionDateResponse(
                                6,
                                "2023-12-11 22:00",
                                500
                            )
                        )
                    )
                )
            )
        }.toModel(MovieMapper)
    }

    override suspend fun getProfileInfo(): Flow<ApiResponse<ProfileModel>> {
        return flow {
            emit(ApiResponse.Loading)
            delay(2000L)
            emit(
                ApiResponse.Success(
                    ProfileResponse(
                        "aboba",
                        listOf(
                            ReserveResponse(1, 2, "Driveeeeee", 3, 4, 5, true, "2023-12-14 12:00"),
                            ReserveResponse(1, 3, "Driveeeeee", 3, 4, 6, false, "2023-12-14 12:00"),
                            ReserveResponse(1, 4, "Driveeeeee", 3, 4, 7, true, "2023-12-14 12:00"),
                        )
                    )
                )
            )
        }.toModel(ProfileMapper)
    }

    override suspend fun unreserveSeatById(
        sessionId: Int,
        seatId: Int
    ): Flow<ApiResponse<ResponseBody>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSessionSeatsById(sessionId: Int): Flow<ApiResponse<SeatsModelCollection>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAnotherMovieSessions(sessionId: Int): Flow<ApiResponse<AnotherSessionModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun reserveSeats(
        sessionId: Int,
        seats: List<Int>
    ): Flow<ApiResponse<ResponseBody>> {
        TODO("Not yet implemented")
    }
}

