package Productivity.AutomatizerFilters.DAO

import AutomatizerFilters.Model.Issue

interface JiraDAO {
    fun getIssues(): List<Issue>
}