package com.bibek.chatapplication.presentation.screen.chat

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bibek.chatapplication.presentation.component.bounceClick
import com.bibek.chatapplication.presentation.screen.search.SearchEvent
import com.bibek.chatapplication.presentation.screen.search.SearchState
import com.bibek.chatapplication.presentation.theme.LightGray

@Preview
@Composable
fun ChatScreenUI(uiState: SearchState = SearchState(), onEvent: (SearchEvent) -> Unit = {}) {
    val messages by uiState.allChats.collectAsStateWithLifecycle(emptyList())
    val scrollState = rememberLazyListState()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "User Icon",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = uiState.matchUsername,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .bounceClick {
                        onEvent(SearchEvent.OnLeaveChatClick)
                    },
                tint = Color.Gray
            )
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF2F2F2))
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Bar

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
                            item {
                                if (!messageEntity.message.isNullOrEmpty()) {
                                    if (messageEntity.userName != uiState.username) {
                                        ReceivedMessage(messageEntity.message)
                                    } else {
                                        SentMessage(messageEntity.message)
                                    }
                                }
                            }
                        }


                    }

                    // Input Section

                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = uiState.currentMessage,
                    onValueChange = {
                        onEvent(SearchEvent.OnCurrentMessageChange(it))
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "ھلا") },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Icon",
                    tint = Color.Blue,
                    modifier = Modifier
                        .size(24.dp)
                        .bounceClick {
                            onEvent(SearchEvent.SendMessage)
                        }
                )

            }
        }

    }

}

@Composable
fun ReceivedMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
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
            Text(text = message, color = Color.Black)
        }
    }
}

@Composable
fun SentMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
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
            Text(text = message, color = Color.Black)
        }
    }
}

