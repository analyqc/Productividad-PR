package PullRequestCSV.DAO

import PullRequestCSV.Model.PullRequests

interface PullRequestDAO {
        fun getPullRequests(): List<PullRequests>
}