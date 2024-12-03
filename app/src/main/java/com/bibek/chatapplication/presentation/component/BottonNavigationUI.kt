package com.bibek.chatapplication.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R

@Preview
@Composable
fun BottomNavigationUI(
    onMyAccountClick: () -> Unit = {},
    onUnlockClick: () -> Unit = {},
    onConversationsClick: () -> Unit = {}
) {
    Card(elevation = 7.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First Item
            NavigationItem(
                icon = R.drawable.ic_person, // Replace with actual drawable resource
                label = "حسابي",
                onClick = onMyAccountClick
            )

            // Center Item (Highlighted)
            Box(
                modifier = Modifier
                    .bounceClick(onUnlockClick)
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5252)), // Red background
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "فضفض",
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }

            // Third Item
            NavigationItem(
                icon = R.drawable.ic_chat, // Replace with actual drawable resource
                label = "المحادثات", onClick = onConversationsClick
            )
        }
    }

}

@Composable
fun NavigationItem(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.bounceClick(onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color.Gray,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
    }
}
