package PullRequestCSV.DAO

import PullRequestCSV.Model.CsvPullRequest

interface CsvDAO {
        fun getCsvPullRequest(): List<CsvPullRequest>
}