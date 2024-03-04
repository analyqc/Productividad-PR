package GitHubAPI.Model
import com.google.gson.annotations.SerializedName
data class GitHub(
    val login: String,
    val id: Int,
    @SerializedName("avatar_url") val avatarUrl: String,
    val name: String?,
    val bio: String?,
    val location: String?,
    val email: String?,
    @SerializedName("public_repos") val publicRepos: Int,
    @SerializedName("followers") val followersCount: Int,
    @SerializedName("following") val followingCount: Int
)