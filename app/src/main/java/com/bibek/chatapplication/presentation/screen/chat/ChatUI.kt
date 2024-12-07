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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.IconWithText
import com.bibek.chatapplication.presentation.component.ReceivedMessage
import com.bibek.chatapplication.presentation.component.SentMessage
import com.bibek.chatapplication.presentation.component.bounceClick
import com.bibek.chatapplication.presentation.screen.search.SearchEvent
import com.bibek.chatapplication.presentation.screen.search.SearchState
import com.bibek.chatapplication.presentation.theme.AppGray

/**
 * UI composable function representing the main chat screen layout.
 * It displays the chat messages, input field, and user action row.
 *
 * @param uiState The current state of the UI containing chat data.
 * @param onEvent Lambda function to handle user interactions like message send or leaving chat.
 */
@Composable
fun ChatUI(uiState: SearchState = SearchState(), onEvent: (SearchEvent) -> Unit = {}) {
    // Collect messages state from the UI state
    val messages by uiState.allChats.collectAsStateWithLifecycle(emptyList())
    val scrollState = rememberLazyListState()

    // Scaffold for main layout, including top bar with user action row
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        UserActionRow(name = uiState.matchUsername, onClick = {
            onEvent(SearchEvent.OnLeaveChatClick)
        })
    }) { innerPadding ->
        // Main content Box for chat and input section
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
                    // LazyColumn for displaying messages
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
                                // Conditional rendering for received or sent messages
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
            // Input and send message row at the bottom of the screen
            Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(25.dp))
                        .border(width = 2.dp, shape = RoundedCornerShape(25.dp), color = AppGray)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Message input field
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
                    // Send button
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

/**
 * Composable function to display the row of user actions at the top of the chat screen.
 *
 * @param name The name of the user in the chat.
 * @param onClick Lambda function triggered when user interacts with the back button.
 */
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
        // Row of icons with text (refresh, report, add)
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
                // Avatar icon
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    tint = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Back button icon
                Icon(
                    painter = painterResource(R.drawable.ic_back_right),
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
