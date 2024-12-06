package com.bibek.chatapplication.presentation.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.bounceClick
import com.bibek.chatapplication.presentation.screen.search.Message
import com.bibek.chatapplication.presentation.screen.search.SearchEvent
import com.bibek.chatapplication.presentation.screen.search.SearchState
import com.bibek.chatapplication.presentation.theme.AppGray
import com.bibek.chatapplication.presentation.theme.LightGray

@Composable
fun ChatScreenUI(uiState: SearchState = SearchState(), onEvent: (SearchEvent) -> Unit = {}) {
    val messages by uiState.allChats.collectAsStateWithLifecycle(emptyList())
    val scrollState = rememberLazyListState()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        UserActionRow(name = uiState.matchUsername, onClick = {
            onEvent(SearchEvent.OnLeaveChatClick)
        })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    LazyColumn(
                        reverseLayout = true,
                        state = scrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                        messages.reversed().forEachIndexed { index, messageEntity ->
                            item(messageEntity.id) {
                                if (!messageEntity.message.isNullOrEmpty()) {
                                    if (messageEntity.userName != uiState.username) {
                                        ReceivedMessage(
                                            message = messageEntity.message,
                                            time = messageEntity.time
                                        )
                                    } else {
                                        SentMessage(
                                            message = messageEntity.message,
                                            time = messageEntity.time,
                                            status = messageEntity.status
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Row(modifier = Modifier.padding(horizontal = 10.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(25.dp))
                        .border(width = 2.dp, shape = RoundedCornerShape(25.dp), color = AppGray)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = uiState.currentMessage,
                        onValueChange = {
                            onEvent(SearchEvent.OnCurrentMessageChange(it))
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Text(
                                    text = "ھلا",
                                    style = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.End // Align user input to the end (right side)
                                    ),
                                )
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.End // Align user input to the end (right side)
                        )
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = "Send Icon",
                        modifier = Modifier
                            .size(40.dp)
                            .bounceClick {
                                onEvent(SearchEvent.SendMessage)
                            }
                    )
                }

            }


            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun ReceivedMessage(message: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFECEFF1), shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        )
                    )
                    .border(
                        width = 1.dp, shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomEnd = 12.dp,
                            bottomStart = 12.dp
                        ), color = LightGray.copy(0.1f)
                    )
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = message, color = Color.Black)

                    Spacer(modifier = Modifier.height(4.dp)) // Add spacing between message and time/status
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = time, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

        }
    }
}

@Composable
fun SentMessage(message: String, time: String, status: Message?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFBBDEFB),
                        shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        )
                    )
                    .border(
                        width = 1.dp, shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            topStart = 12.dp,
                            bottomStart = 12.dp
                        ), color = LightGray.copy(0.1f)
                    )
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = message, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp)) // Add spacing between message and time/status
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = time,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (status) {
                                Message.Sending -> "⏳"
                                Message.Sent -> "✓"
                                Message.Read -> "✓✓"
                                Message.None -> ""
                                else -> ""
                            },
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun UserActionRow(name: String = "", onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Row of icons with text
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(1f)
        ) {
            IconWithText(icon = painterResource(R.drawable.ic_refress), "تجاوز") // Refresh icon
            Spacer(Modifier.width(20.dp))
            IconWithText(icon = painterResource(R.drawable.ic_chat_cloud), "إبلاغ")   // Info icon
            Spacer(Modifier.width(20.dp))
            IconWithText(
                icon = painterResource(R.drawable.ic_person_add),
                "إضافة"
            ) // Add person icon
        }

        // User info with name and avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                modifier = Modifier.padding(end = 8.dp)
            )
            Row(modifier = Modifier.bounceClick(onClick)) {
                Icon(
                    imageVector = Icons.Default.AccountCircle, // Avatar icon
                    contentDescription = "Avatar",
                    tint = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_back_right), // Avatar icon
                    contentDescription = "Avatar",
                    tint = AppGray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(24.dp)
                )
            }

        }
    }
}

@Composable
fun IconWithText(icon: Painter, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = Color(0xFF64B5F6), // Light blue color
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}