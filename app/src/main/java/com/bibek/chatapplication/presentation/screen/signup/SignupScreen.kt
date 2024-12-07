package com.bibek.chatapplication.presentation.screen.signup

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.Button
import com.bibek.chatapplication.presentation.component.Header
import com.bibek.chatapplication.presentation.component.RightAlignText
import com.bibek.chatapplication.presentation.component.TextBox
import com.bibek.chatapplication.presentation.component.bounceClick
import com.bibek.chatapplication.presentation.theme.Primary
import com.bibek.chatapplication.utils.getAndroidId

@Composable
fun SignupScreen(uiState: SignupState, onEvent: (SignupEvent) -> Unit = {}) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header(text = "حياك بيننا")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "اسمك",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            TextBox(text = uiState.udid, hint = "يجب أن لا يقل عن ٣ أحرف ", onTextChange = {
                onEvent(
                    SignupEvent.OnNameChange(it)
                )
            })
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            RightAlignText(
                text = "جنسك",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenderOption(
                    iconRes = R.drawable.ic_male,
                    text = Gender.Male.name,
                    uiState.gender,
                    onClick = {
                        onEvent(SignupEvent.OnGenderSelect(Gender.Male))
                    })
                GenderOption(
                    iconRes = R.drawable.ic_female,
                    text = Gender.Female.name,
                    uiState.gender,
                    onClick = {
                        onEvent(SignupEvent.OnGenderSelect(Gender.Female))
                    })
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "استمرارك يعني موافقتك على الشروط والأحكام",
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        Button(
            text = "استمر",
            onClick = { onEvent(SignupEvent.OnSignupClick(getAndroidId(context = context))) },
            modifier = Modifier
                .fillMaxWidth(), isLoading = uiState.isLoading
        )
    }
}


@Composable
fun GenderOption(
    @DrawableRes iconRes: Int,
    text: String,
    gender: Gender?,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = if (text == gender?.name) Primary else White,
                shape = CircleShape
            )
            .padding(7.dp)
            .bounceClick(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(77.dp)
        )
        Text(text = text, color = Color.Gray)
    }
}