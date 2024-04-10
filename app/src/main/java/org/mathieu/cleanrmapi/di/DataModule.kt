package org.mathieu.cleanrmapi.di

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.mathieu.cleanrmapi.data.local.RMDatabase
import org.mathieu.cleanrmapi.data.remote.CharacterApi
import org.mathieu.cleanrmapi.data.remote.EpisodeApi
import org.mathieu.cleanrmapi.data.remote.HttpClient
import org.mathieu.cleanrmapi.data.repositories.CharacterRepositoryImpl
import org.mathieu.cleanrmapi.data.repositories.EpisodeRepositoryImpl
import org.mathieu.cleanrmapi.domain.repositories.CharacterRepository
import org.mathieu.cleanrmapi.domain.repositories.EpisodeRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// URL de base de l'API Rick and Morty.
private const val RMAPI_URL = "https://rickandmortyapi.com/api/"

// Fournit une instance OkHttpClient pour les requêtes HTTP.
private fun provideHttpClient(): OkHttpClient = HttpClient().client

// Configuration de Gson pour la sérialisation/désérialisation JSON.
private val gson = GsonBuilder()
    .serializeNulls() // Permet la sérialisation des valeurs null.
    .create()

private fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(RMAPI_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

// Fournit la base de données Room.
private fun provideDataBase(application: Application): RMDatabase = Room.databaseBuilder(
    application,
    RMDatabase::class.java,
    "rick_and_morty_database"
).fallbackToDestructiveMigration().build() // Gestion de la migration.

// Crée et fournit une instance d'API.
private inline fun <reified T> provideApi(httpClient: OkHttpClient): T = buildRetrofit(
    okHttpClient = httpClient
).create(T::class.java)

// Module Koin pour la configuration des dépendances liées aux données.
val dataModule = module {
    single { provideHttpClient() }

    single { provideApi<CharacterApi>(get()) }
    single { provideApi<EpisodeApi>(get()) }

    single { provideDataBase(get<Application>()) }

    single<CharacterRepository> {
        CharacterRepositoryImpl(get(), get(), get<RMDatabase>().characterDAO(), get())
    }
    single<EpisodeRepository> {
        EpisodeRepositoryImpl(get(), get<RMDatabase>().episodeDAO(), get())
    }
}
