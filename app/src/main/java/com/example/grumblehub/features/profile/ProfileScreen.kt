package com.example.grumblehub.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.grumblehub.R
import com.example.grumblehub.features.profile.components.ProfileTopBar

data class Setting(
    val iconId: Int,
    val iconContentDescription: String,
    val text: String
)

val profileSetting = listOf(
    Setting(
        iconId = R.drawable.baseline_notifications_24,
        iconContentDescription = "Notifications",
        text = "Notifications"
    ),
    Setting(
        iconId = R.drawable.baseline_help_24,
        iconContentDescription = "Help and Support",
        text = "Help & Support"
    )
)


@Composable
fun ProfileScreen(modifier: Modifier, navController: NavController) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ProfileTopBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ProfileHeader("Account")
            ProfileAccount(
                image = "https://www.shreksadventure.com/media/2ckim5b2/shrek_still_sq600-sc50-f111_13.jpg",
                email = "tamanielliot@gmail.com",
                username = "ashenone"
            )
            ProfileSetting(
                iconId = R.drawable.baseline_lock_24,
                iconContentDescription = "Change password",
                text = "Change password"
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileHeader("Preferences")
            LazyColumn {
                items(profileSetting) { item ->
                    ProfileSetting(
                        iconId = item.iconId,
                        iconContentDescription = item.iconContentDescription,
                        text = item.text
                    )
                }
            }

        }
    }
}

@Composable
fun ProfileHeader(text: String) {
    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ProfileAccount(image: String, email: String, username: String) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .clickable { }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)

                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(200.dp))
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = image,
                    contentDescription = "User profile",
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = email,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
fun ProfileSetting(iconId: Int, iconContentDescription: String, text: String) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .clickable { }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)

                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(iconId),
                    contentDescription = iconContentDescription,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = text,
                fontSize = 18.sp,
            )
        }
    }
}