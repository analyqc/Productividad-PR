package GitHubAPI.DAO.implement

import GitHubAPI.DAO.GitHubService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitHubDAO {
    private val BASE_URL = "https://api.github.com/"
    private val service: GitHubService = crearServicio()

    private fun crearServicio(): GitHubService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(GitHubService::class.java)
    }

    fun getUser() {
        val call = service.getUser("TatianaQ12")
        val response = call.execute()
        if (response.isSuccessful) {
            val user = response.body()
            println("Nombre de usuario: ${user?.login}")
            println("ID: ${user?.id}")
            println("Nombre: ${user?.name}")
            println("Biografía: ${user?.bio}")
            println("Ubicación: ${user?.location}")
            println("Correo electrónico: ${user?.email}")
            println("Número de repositorios públicos: ${user?.publicRepos}")
            println("Número de seguidores: ${user?.followersCount}")
            println("Número de seguidos: ${user?.followingCount}")
            println("Avatar URL: ${user?.avatarUrl}")
        } else {
            println("Error al obtener los datos: ${response.message()}")
        }
    }
}
