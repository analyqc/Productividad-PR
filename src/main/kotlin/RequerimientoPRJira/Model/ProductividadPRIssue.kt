package RequerimientoPRJira.Model

import IssueXLSX.Model.Issue
import PullRequestCSV.Model.PullRequests

data class ProductividadPRIssue(
    val pullRequest: PullRequests?,
    val issue: Issue?
)
