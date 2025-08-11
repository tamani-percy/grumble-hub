package com.example.grumblehub.features.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.grumblehub.R
import com.example.grumblehub.core.AppNavHost

@Composable
fun GrievanceItem(
    modifier: Modifier = Modifier,
    image: Int,
    text: String,
    navController: NavController,
    onClick: () -> Unit
) {
    val sizeResolver = rememberConstraintsSizeResolver()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 20.dp,
                vertical = 5.dp
            )
            .height(140.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Project Deadline Missed",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Missed the deadline for the quarterly report due to unforeseen technical issues.",
                maxLines = 3,
                style = MaterialTheme.typography.bodyMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                for (i in 0 until 2) {
                    GrievanceChip(
                        modifier = Modifier.padding(end = 4.dp),
                        text = when (i) {
                            0 -> "Work"
                            1 -> "Relationship"
                            else -> "Other"
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                contentScale = ContentScale.Crop,
                contentDescription = "Rounded Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}