package com.example.gigmatch.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigmatch.NavigationItem
import com.example.gigmatch.ui.theme.AppBackground
import com.example.gigmatch.ui.theme.BrandBlue
import com.example.gigmatch.ui.theme.BrightGreen
import com.example.gigmatch.ui.theme.CardBorder
import com.example.gigmatch.ui.theme.DeepBlue
import com.example.gigmatch.ui.theme.MutedText
import com.example.gigmatch.ui.theme.Navy
import com.example.gigmatch.ui.theme.SoftBlue
import com.example.gigmatch.ui.theme.SuccessGreen
import kotlin.math.floor

@Composable
fun GigMatchScaffold(
    selectedIndex: Int,
    items: List<NavigationItem>,
    onItemSelected: (Int) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(82.dp)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            item = item,
                            selected = selectedIndex == index,
                            onClick = { onItemSelected(index) },
                        )
                    }
                }
            }
        },
        content = content,
    )
}

@Composable
private fun BottomNavigationItem(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val foreground = if (selected) Color.White else MutedText
    val labelColor = if (selected) Color.White else Navy
    Column(
        modifier = Modifier
            .width(if (selected) 78.dp else 82.dp)
            .height(68.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(if (selected) BrandBlue else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(31.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = foreground,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = item.label,
            color = labelColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Menu,
            contentDescription = "Menu",
            tint = MutedText,
            modifier = Modifier.size(27.dp),
        )
        Spacer(Modifier.width(22.dp))
        Text(
            text = "Gig-Match",
            color = DeepBlue,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f),
        )
        WorkerAvatar(size = 38.dp)
    }
}

@Composable
fun WorkerAvatar(size: Dp = 76.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(SoftBlue),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Worker profile",
            tint = DeepBlue,
            modifier = Modifier.size(size * 0.62f),
        )
    }
}

@Composable
fun SectionHeading(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Navy,
            style = MaterialTheme.typography.titleLarge,
        )
        subtitle?.let {
            Spacer(Modifier.height(5.dp))
            Text(
                text = it,
                color = MutedText,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CardBorder),
        )
    }
}

@Composable
fun OutlineCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(22.dp),
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun RatingStars(
    rating: Float,
    modifier: Modifier = Modifier,
    size: Dp = 22.dp,
) {
    Row(modifier = modifier) {
        for (index in 1..5) {
            val icon: ImageVector = when {
                rating >= index -> Icons.Filled.Star
                rating - floor(rating) >= 0.5f && index == floor(rating).toInt() + 1 -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Outlined.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(size),
            )
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    icon: ImageVector? = null,
    green: Boolean = false,
) {
    val background = if (green) BrightGreen else SoftBlue
    val foreground = if (green) SuccessGreen else DeepBlue
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .padding(horizontal = 11.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        icon?.let {
            Icon(it, contentDescription = null, tint = foreground, modifier = Modifier.size(14.dp))
        }
        Text(
            text = text,
            color = foreground,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
