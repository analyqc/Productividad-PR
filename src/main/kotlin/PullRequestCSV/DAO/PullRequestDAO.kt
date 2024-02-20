package PullRequestCSV.DAO

import PullRequestCSV.Model.CsvPullRequest

interface PullRequestDAO {
        fun getPullRequests(): List<CsvPullRequest>
}