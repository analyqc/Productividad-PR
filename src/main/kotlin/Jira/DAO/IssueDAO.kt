package Jira.DAO

import Jira.Model.Issue
interface IssueDAO {
        fun getIssue(): List<Issue>
}