package com.bibek.chatapplication.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bibek.chatapplication.presentation.component.TabItem
import com.bibek.chatapplication.presentation.theme.DarkGray
import com.bibek.chatapplication.presentation.theme.LightGray

@Composable
fun SearchScreen(
    uiState: SearchState, onEvent: (SearchEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(57.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Top Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = LightGray, shape = RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(text = "أنثى", isSelected = true)
            TabItem(text ="ذكر", isSelected = false)
        }

        // Profile Image with Progress

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .background(color = LightGray, shape = RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFF7C344),
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(170.dp)
                )
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(120.dp),
                    color = Color.Gray // Placeholder background
                ) {
                    // Replace with an image painter for real image
                    BasicText(
                        text = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Text below image
            Text(
                text = "البحث عن أفضل تطابق لك",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }


        // Gems Count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = LightGray, shape = RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ماسي",
                color = Color.White,
                fontSize = 16.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "200",
                    color = Color.Yellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Star, // Replace with gem icon
                    contentDescription = "Gem Icon",
                    tint = Color.Yellow,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}