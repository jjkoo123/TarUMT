package com.example.gigmatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gigmatch.data.GigMatchRepository
import com.example.gigmatch.data.Job
import com.example.gigmatch.data.Review
import com.example.gigmatch.data.SkillBadge
import com.example.gigmatch.ui.components.OutlineCard
import com.example.gigmatch.ui.components.RatingStars
import com.example.gigmatch.ui.components.SectionHeading
import com.example.gigmatch.ui.components.StatusChip
import com.example.gigmatch.ui.components.TopHeader
import com.example.gigmatch.ui.components.WorkerAvatar
import com.example.gigmatch.ui.theme.BrandBlue
import com.example.gigmatch.ui.theme.CardBorder
import com.example.gigmatch.ui.theme.DeepBlue
import com.example.gigmatch.ui.theme.MutedText
import com.example.gigmatch.ui.theme.Navy
import com.example.gigmatch.ui.theme.SoftBlue
import com.example.gigmatch.ui.theme.SuccessGreen
import java.util.Locale

@Composable
fun ReviewsScreen(repository: GigMatchRepository, scaffoldPadding: PaddingValues) {
    var showReviewDialog by remember { mutableStateOf(false) }
    var showAll by remember { mutableStateOf(false) }
    val visibleReviews = if (showAll) repository.reviews else repository.reviews.take(3)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(scaffoldPadding),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item { TopHeader() }
        item {
            Column {
                Text("Worker Profile", color = DeepBlue, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Manage your job request, skill badges and employer reviews.",
                    color = MutedText,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        item { WorkerReviewSummary(repository) }
        item {
            SectionHeading(
                "Request a Job",
                "Send a job request directly to the employer.",
            )
        }
        item {
            val job = repository.jobs.first()
            JobRequestCard(
                job = job,
                requested = job.id in repository.requestedJobIds,
                onRequest = { repository.requestJob(job.id) },
            )
        }
        item {
            SectionHeading(
                "Skill Badge System",
                "Badges are earned from completed work and employer feedback.",
            )
        }
        item { SkillBadgeRow(repository.skillBadges()) }
        item { SectionHeading("Review and Rating", "Employers can rate completed work and award one skill badge.") }
        item {
            Button(
                onClick = { showReviewDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(vertical = 13.dp),
            ) {
                Icon(Icons.Outlined.AddComment, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Write a Review", fontWeight = FontWeight.Bold)
            }
        }
        item { SectionHeading("Recent Reviews") }
        items(visibleReviews, key = { it.id }) { review -> ReviewCard(review) }
        if (repository.reviews.size > 3) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { showAll = !showAll },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftBlue,
                            contentColor = DeepBlue,
                        ),
                        shape = RoundedCornerShape(50),
                    ) {
                        Text(if (showAll) "Show Fewer Reviews" else "Load More Reviews")
                    }
                }
            }
        }
    }

    if (showReviewDialog) {
        AddReviewDialog(
            onDismiss = { showReviewDialog = false },
            onSubmit = { company, job, rating, comment, badge ->
                repository.addReview(company, job, rating, comment, badge)
                showReviewDialog = false
                showAll = false
            },
        )
    }
}

@Composable
private fun JobRequestCard(job: Job, requested: Boolean, onRequest: () -> Unit) {
    OutlineCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(SoftBlue),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Outlined.WorkOutline, null, tint = DeepBlue, modifier = Modifier.size(25.dp))
                }
                Spacer(Modifier.width(13.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(job.title, color = Navy, style = MaterialTheme.typography.titleMedium)
                    Text(job.companyName, color = BrandBlue, fontWeight = FontWeight.SemiBold)
                }
                if (requested) {
                    StatusChip("Pending")
                }
            }
            Text(job.description, color = MutedText, style = MaterialTheme.typography.bodyMedium)
            JobInfoRow(Icons.Outlined.LocationOn, job.location)
            JobInfoRow(Icons.Outlined.Schedule, job.schedule)
            JobInfoRow(Icons.Outlined.Payments, job.pay)
            Button(
                onClick = onRequest,
                enabled = !requested,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandBlue,
                    disabledContainerColor = Color(0xFFD9FBE4),
                    disabledContentColor = SuccessGreen,
                ),
                shape = RoundedCornerShape(50),
            ) {
                Icon(
                    if (requested) Icons.Outlined.CheckCircle else Icons.AutoMirrored.Outlined.Send,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(if (requested) "Request Sent to Employer" else "Request This Job")
            }
        }
    }
}

@Composable
private fun JobInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MutedText, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(value, color = MutedText, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SkillBadgeRow(badges: List<SkillBadge>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(badges, key = { it.id }) { badge ->
            Surface(
                modifier = Modifier.width(164.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (badge.earned) Color(0xFFD9FBE4) else SoftBlue),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            if (badge.id == "verified") Icons.Outlined.Verified else Icons.Outlined.EmojiEvents,
                            contentDescription = null,
                            tint = if (badge.earned) SuccessGreen else BrandBlue,
                            modifier = Modifier.size(23.dp),
                        )
                    }
                    Text(badge.title, color = Navy, fontWeight = FontWeight.Bold)
                    Text(
                        if (badge.earned) "Earned" else "In progress",
                        color = if (badge.earned) SuccessGreen else BrandBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    if (!badge.earned) {
                        LinearProgressIndicator(
                            progress = { badge.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(CircleShape),
                            color = BrandBlue,
                            trackColor = CardBorder,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkerReviewSummary(repository: GigMatchRepository) {
    OutlineCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 30.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(SoftBlue)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    WorkerAvatar(74.dp)
                }
            }
            Text("Maria Gonzalez", color = Navy, style = MaterialTheme.typography.titleMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusChip("Verified Worker", icon = Icons.Outlined.Verified, green = true)
                StatusChip("Retail & Hospitality")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingStars(repository.averageRating(), size = 24.dp)
                Spacer(Modifier.width(8.dp))
                Text(
                    String.format(Locale.getDefault(), "%.1f", repository.averageRating()),
                    color = Navy,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.width(4.dp))
                Text("(${repository.reviewCount()} Reviews)", color = MutedText)
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    OutlineCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.companyName, color = Navy, fontWeight = FontWeight.Bold)
                    Text(
                        "${review.jobTitle} • ${review.date}",
                        color = MutedText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                RatingStars(review.rating, size = 18.dp)
            }
            Text(
                text = "“${review.comment}”",
                color = Navy,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
            )
            review.badgeId?.let {
                Text(
                    text = "+ ${badgeTitle(it)} badge",
                    color = SuccessGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun AddReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int, String, String?) -> Unit,
) {
    var company by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var selectedBadge by remember { mutableStateOf<String?>(null) }
    val badgeOptions = listOf("reliable", "punctual", "customer_service", "quick_learner", "team_player")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            shadowElevation = 10.dp,
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text("Rate Worker", color = Navy, style = MaterialTheme.typography.titleLarge)
                Text(
                    "This demo form represents an employer reviewing a worker after a completed job.",
                    color = MutedText,
                    style = MaterialTheme.typography.bodyMedium,
                )
                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
                OutlinedTextField(
                    value = job,
                    onValueChange = { job = it },
                    label = { Text("Job title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
                Text("Rating", color = Navy, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    for (star in 1..5) {
                        IconButton(onClick = { rating = star }) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "$star stars",
                                tint = if (star <= rating) SuccessGreen else CardBorder,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }
                }
                Text("Award one skill badge (optional)", color = Navy, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(badgeOptions) { badge ->
                        FilterChip(
                            selected = selectedBadge == badge,
                            onClick = { selectedBadge = if (selectedBadge == badge) null else badge },
                            label = { Text(badgeTitle(badge)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlue,
                                selectedLabelColor = Color.White,
                            ),
                        )
                    }
                }
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Review comment") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onSubmit(company, job, rating, comment, selectedBadge) },
                        enabled = comment.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    ) {
                        Text("Submit Review")
                    }
                }
            }
        }
    }
}

fun badgeTitle(id: String): String = when (id) {
    "verified" -> "Verified Worker"
    "top_rated" -> "Top Rated"
    "reliable" -> "Reliable"
    "punctual" -> "Punctual"
    "customer_service" -> "Customer Service"
    "quick_learner" -> "Quick Learner"
    "team_player" -> "Team Player"
    else -> "Skill"
}
