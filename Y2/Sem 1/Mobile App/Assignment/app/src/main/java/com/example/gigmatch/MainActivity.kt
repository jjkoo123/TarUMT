package com.example.gigmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gigmatch.data.GigMatchRepository
import com.example.gigmatch.ui.components.GigMatchScaffold
import com.example.gigmatch.ui.screens.ReviewsScreen
import com.example.gigmatch.ui.theme.GigMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GigMatchTheme {
                GigMatchApp(repository = remember { GigMatchRepository(applicationContext) })
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
)

@Composable
private fun GigMatchApp(repository: GigMatchRepository) {
    val navigationItems = remember {
        listOf(
            NavigationItem("Find Work", Icons.Outlined.Explore),
            NavigationItem("Post Job", Icons.Outlined.AddCircleOutline),
            NavigationItem("Company Detail", Icons.Outlined.Business),
            NavigationItem("Profile", Icons.Outlined.PersonOutline),
        )
    }

    GigMatchScaffold(
        selectedIndex = 3,
        items = navigationItems,
        onItemSelected = { /* Other destinations are implemented by teammates. */ },
    ) { contentPadding ->
        ReviewsScreen(repository, contentPadding)
    }
}
