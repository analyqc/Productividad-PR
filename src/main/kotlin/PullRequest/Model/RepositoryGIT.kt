package PullRequest.Model

data class RepositoryGIT(
    val email: String,
    val repository: String,
    val branch: String,
    val UserStory : String,
    val PRNumber:Int,
    val PRMerge:String,
    val PRClosed:String,
    val PRId:Int
)
