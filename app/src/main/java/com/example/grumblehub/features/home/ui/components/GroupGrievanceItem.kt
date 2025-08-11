package com.example.grumblehub.features.home.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.grumblehub.features.grievance.data.Grievance
import com.example.grumblehub.utils.convertAndFormatStringToLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupGrievanceItem(
    modifier: Modifier = Modifier,
    image: Int,
    grievance: Grievance
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
            .height(120.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            convertAndFormatStringToLocalDateTime(grievance.createdAt)?.let {
                Text(
                    text = it,
                    maxLines = 3,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = grievance.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = grievance.grievance,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 3,
                style = MaterialTheme.typography.bodyMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GrievanceChip(
                    modifier = Modifier.padding(end = 4.dp),
                    text = grievance.tag.name
                )

            }

        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {

            AsyncImage(
                filterQuality = FilterQuality.None,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "Rounded Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}