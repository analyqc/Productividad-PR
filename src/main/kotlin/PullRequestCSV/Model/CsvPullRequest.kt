package PullRequestCSV.Model

data class CsvPullRequest(
    val Username: String,
    val Email: String,
    val Repository: String,
    val Branch: String,
    val UserStory: String,
    val PRNumber: Int,
    val PRTitle: String,
    val PRState: String,
    val PRCreated: String,
    val PRMerged: String,
    val PRClosed: String,
    val Id: Int,
    val PRReviewers: Int
)
