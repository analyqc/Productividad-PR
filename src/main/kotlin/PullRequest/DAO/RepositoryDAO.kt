package PullRequest.DAO

import PullRequest.Model.RepositoryGIT

interface RepositoryDAO {
        fun getRepository(): List<RepositoryGIT>
}