package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bibek.chatapplication.presentation.screen.search.Message
import com.bibek.chatapplication.presentation.theme.LightGray

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