package com.bibek.chatapplication.presentation.screen.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.ActionButton
import com.bibek.chatapplication.presentation.component.ExitChatDialog
import com.bibek.chatapplication.presentation.component.TabItem
import com.bibek.chatapplication.presentation.screen.chat.ChatScreenUI
import com.bibek.chatapplication.presentation.theme.DarkGray
import com.bibek.chatapplication.presentation.theme.LightGray
import com.bibek.chatapplication.presentation.theme.Primary

@Composable
fun ChatScreen(
    uiState: SearchState, onEvent: (SearchEvent) -> Unit = {}
) {
    BackHandler {
        onEvent(SearchEvent.OnBackClick)
    }
    ExitChatDialog(showDialog = uiState.isShowDialog,
        onConfirm = {
            onEvent(SearchEvent.OnLeaveChatClick)
        }, onDismiss = {
            onEvent(SearchEvent.OnDialogDismissClick)
        })
    if (uiState.chatState == ChatState.Accepted) {
        ChatScreenUI(uiState = uiState, onEvent = onEvent)
    } else {
        SearchScreen(uiState = uiState, onEvent = onEvent)
    }
}

@Composable
fun SearchScreen(uiState: SearchState, onEvent: (SearchEvent) -> Unit = {}) {
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
            TabItem(text = "ذكر", isSelected = false)
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
            if (uiState.chatState == ChatState.Matching) {
                SearchedProfileLoading()
            } else {
                SearchedProfile(
                    isRequestedForAccepted = uiState.isRequestedForAccepted,
                    username = uiState.matchUsername,
                    onOkClick = {
                        onEvent(SearchEvent.OnAcceptClick)
                    },
                    onTranscendClick = {
                        onEvent(SearchEvent.OnRematchClick)
                    })
            }
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

@Composable
private fun SearchedProfileLoading() {
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

@Composable
private fun SearchedProfile(
    isRequestedForAccepted: Boolean,
    username: String = "ريهانا فاروق",
    onTranscendClick: () -> Unit,
    onOkClick: () -> Unit
) {
    // Report Icon and Profile Image
    Spacer(modifier = Modifier.height(10.dp))
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(onClick = { /* TODO: Handle report action */ }) {
            Icon(
                painter = painterResource(R.drawable.ic_chat_cloud), // Replace with your custom "Report" icon
                contentDescription = "Report",
                tint = Color.White
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(150.dp)
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(120.dp),
            color = Color.Gray // Placeholder background for profile image
        ) {
            Image(painter = painterResource(R.drawable.porson_img), contentDescription = null)
        }
    }

    // Name and Location
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            text = username,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "العربية السعودية",
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
    // Action Buttons
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(text = "تجاوز", backgroundColor = Primary, onClick = onTranscendClick)
        if (isRequestedForAccepted) {
            CircularProgressIndicator(modifier = Modifier.height(60.dp), color = Primary)
        } else {
            ActionButton(text = "موافق", backgroundColor = Primary, onClick = onOkClick)
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
}

