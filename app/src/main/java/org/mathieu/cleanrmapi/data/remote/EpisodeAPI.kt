package org.mathieu.cleanrmapi.data.remote

import org.mathieu.cleanrmapi.data.remote.responses.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApi {
    @GET("episode/{ids}")
    suspend fun getEpisodes(@Path("ids") ids : List<Int>): List<EpisodeResponse>
}