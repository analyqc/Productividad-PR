package GitHubAPI.DAO
import GitHubAPI.Model.GitHub
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface GitHubService {
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<GitHub>
}