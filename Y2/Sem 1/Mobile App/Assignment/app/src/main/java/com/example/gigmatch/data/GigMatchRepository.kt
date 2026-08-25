package com.example.gigmatch.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class GigMatchRepository(context: Context) {
    private val preferences = context.getSharedPreferences("gig_match_data", Context.MODE_PRIVATE)

    val jobs = listOf(
        Job(
            id = "job-store-assistant",
            companyId = "fresh-market",
            title = "Store Assistant",
            companyName = "Fresh Market Groceries",
            category = "Retail",
            location = "Kuala Lumpur",
            pay = "RM 90 / day",
            schedule = "Saturday • 9:00 AM–6:00 PM",
            description = "Help arrange products, assist customers and keep the store tidy. No experience required.",
        ),
        Job(
            id = "job-event-usher",
            companyId = "city-event",
            title = "Event Usher",
            companyName = "City Event Staffing",
            category = "Events",
            location = "Petaling Jaya",
            pay = "RM 120 / shift",
            schedule = "Sunday • 2:00 PM–10:00 PM",
            description = "Welcome guests, check tickets and guide visitors during a weekend community event.",
        ),
        Job(
            id = "job-barista-helper",
            companyId = "downtown-cafe",
            title = "Cafe Barista Helper",
            companyName = "Downtown Cafe",
            category = "Food & Beverage",
            location = "Subang Jaya",
            pay = "RM 12 / hour",
            schedule = "Weekdays • 7:00 AM–12:00 PM",
            description = "Support the barista, serve simple orders and keep the counter clean during the morning rush.",
        ),
    )

    private val starterReviews = listOf(
        Review(
            id = "review-1",
            companyName = "Fresh Market Groceries",
            jobTitle = "Store Assistant",
            date = "Oct 12, 2025",
            rating = 5f,
            comment = "Very reliable and hardworking! Maria jumped right in during a busy shift and needed almost no direction. Would hire again instantly.",
            badgeId = "reliable",
        ),
        Review(
            id = "review-2",
            companyName = "Downtown Cafe",
            jobTitle = "Barista Prep",
            date = "Sep 28, 2025",
            rating = 4.5f,
            comment = "Great attitude and very punctual. Learned our POS system very quickly and communicated clearly with the team.",
            badgeId = "punctual",
        ),
        Review(
            id = "review-3",
            companyName = "City Event Staffing",
            jobTitle = "Event Usher",
            date = "Sep 05, 2025",
            rating = 5f,
            comment = "Exceptional customer service skills. Handled a difficult guest with grace and professionalism. Highly recommended.",
            badgeId = "customer_service",
        ),
        Review(
            id = "review-4",
            companyName = "Neighbourhood Learning Hub",
            jobTitle = "Homework Tutor",
            date = "Aug 22, 2025",
            rating = 5f,
            comment = "Patient, well prepared and excellent at explaining difficult ideas in a simple way.",
            badgeId = null,
        ),
        Review(
            id = "review-5",
            companyName = "Community Moving Services",
            jobTitle = "Moving Helper",
            date = "Aug 10, 2025",
            rating = 4f,
            comment = "Worked safely and followed instructions. A dependable helper for a busy moving day.",
            badgeId = null,
        ),
    )

    var requestedJobIds by mutableStateOf(preferences.getStringSet(KEY_REQUESTED_JOBS, emptySet()).orEmpty())
        private set

    val reviews = mutableStateListOf<Review>().apply {
        addAll(loadCustomReviews())
        addAll(starterReviews)
    }

    fun requestJob(jobId: String) {
        if (jobId in requestedJobIds) return
        requestedJobIds = requestedJobIds.toMutableSet().apply { add(jobId) }.toSet()
        preferences.edit().putStringSet(KEY_REQUESTED_JOBS, requestedJobIds).apply()
    }

    fun addReview(companyName: String, jobTitle: String, rating: Int, comment: String, badgeId: String?) {
        val review = Review(
            id = UUID.randomUUID().toString(),
            companyName = companyName.trim().ifBlank { "Community Employer" },
            jobTitle = jobTitle.trim().ifBlank { "Short-Term Worker" },
            date = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date()),
            rating = rating.toFloat(),
            comment = comment.trim(),
            badgeId = badgeId,
        )
        reviews.add(0, review)
        saveCustomReviews()
    }

    fun reviewCount(): Int = 37 + reviews.size

    fun averageRating(): Float {
        val baselineCount = 37
        val baselineTotal = 181.3f
        return (baselineTotal + reviews.sumOf { it.rating.toDouble() }.toFloat()) / reviewCount()
    }

    fun skillBadges(): List<SkillBadge> {
        val awarded = reviews.mapNotNull { it.badgeId }.toSet()
        val rating = averageRating()
        return listOf(
            SkillBadge("verified", "Verified Worker", "Identity and worker profile verified", true, 1f),
            SkillBadge("top_rated", "Top Rated", "Maintain a rating above 4.5", rating >= 4.5f, (rating / 4.5f).coerceAtMost(1f)),
            SkillBadge("reliable", "Reliable", "Earned from positive employer feedback", "reliable" in awarded, if ("reliable" in awarded) 1f else 0.65f),
            SkillBadge("punctual", "Punctual", "Recognised for arriving on time", "punctual" in awarded, if ("punctual" in awarded) 1f else 0.5f),
            SkillBadge("customer_service", "Customer Service", "Recognised for excellent people skills", "customer_service" in awarded, if ("customer_service" in awarded) 1f else 0.4f),
            SkillBadge("quick_learner", "Quick Learner", "Learn new tasks with little guidance", "quick_learner" in awarded, if ("quick_learner" in awarded) 1f else 0.25f),
            SkillBadge("team_player", "Team Player", "Work effectively with a new team", "team_player" in awarded, if ("team_player" in awarded) 1f else 0.2f),
        )
    }

    private fun loadCustomReviews(): List<Review> = runCatching {
        val array = JSONArray(preferences.getString(KEY_REVIEWS, "[]"))
        buildList {
            for (index in 0 until array.length()) {
                val item = array.getJSONObject(index)
                add(
                    Review(
                        id = item.getString("id"),
                        companyName = item.getString("companyName"),
                        jobTitle = item.getString("jobTitle"),
                        date = item.getString("date"),
                        rating = item.getDouble("rating").toFloat(),
                        comment = item.getString("comment"),
                        badgeId = item.optString("badgeId").ifBlank { null },
                    ),
                )
            }
        }
    }.getOrDefault(emptyList())

    private fun saveCustomReviews() {
        val custom = reviews.filterNot { review -> starterReviews.any { it.id == review.id } }
        val array = JSONArray()
        custom.forEach { review ->
            array.put(
                JSONObject().apply {
                    put("id", review.id)
                    put("companyName", review.companyName)
                    put("jobTitle", review.jobTitle)
                    put("date", review.date)
                    put("rating", review.rating)
                    put("comment", review.comment)
                    put("badgeId", review.badgeId.orEmpty())
                },
            )
        }
        preferences.edit().putString(KEY_REVIEWS, array.toString()).apply()
    }

    private companion object {
        const val KEY_REQUESTED_JOBS = "requested_job_ids"
        const val KEY_REVIEWS = "custom_reviews"
    }
}
