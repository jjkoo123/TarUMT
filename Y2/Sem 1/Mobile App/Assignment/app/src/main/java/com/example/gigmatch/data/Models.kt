package com.example.gigmatch.data

data class Job(
    val id: String,
    val companyId: String,
    val title: String,
    val companyName: String,
    val category: String,
    val location: String,
    val pay: String,
    val schedule: String,
    val description: String,
)

data class Review(
    val id: String,
    val companyName: String,
    val jobTitle: String,
    val date: String,
    val rating: Float,
    val comment: String,
    val badgeId: String? = null,
)

data class SkillBadge(
    val id: String,
    val title: String,
    val description: String,
    val earned: Boolean,
    val progress: Float,
)
