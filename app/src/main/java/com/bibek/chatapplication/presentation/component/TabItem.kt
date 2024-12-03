package com.bibek.chatapplication.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.screen.signup.Gender

@Composable
fun TabItem(text: String, isSelected: Boolean) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Image(
            painter = when (text) {
                Gender.Both.name -> painterResource(R.drawable.ic_both_male_female)
                Gender.Male.name -> painterResource(R.drawable.ic_male)
                Gender.Female.name -> painterResource(R.drawable.ic_female)
                else -> painterResource(R.drawable.ic_both_male_female)
            }, contentDescription = null, Modifier.size(30.dp)
        )
    }

}