package Jira.DAO

import Jira.Model.CsvJira
interface JiraDAO {
        fun getJira(): List<CsvJira>
}