package IssueXLSX.DAO

import IssueXLSX.Model.Issue
interface IssueDAO {
        fun getIssue(): List<Issue>
}