package com.bibek.chatapplication.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.BottomNavigationUI
import com.bibek.chatapplication.presentation.component.Header
import com.bibek.chatapplication.presentation.component.RightAlignText
import com.bibek.chatapplication.presentation.screen.signup.Gender
import com.bibek.chatapplication.presentation.screen.signup.GenderOption

@Preview
@Composable
fun HomeScreen(uiState: HomeState = HomeState(), onEvent: (HomeEvent) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header(text = "ابحث عن صديق")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            RightAlignText(
                text = "فضفض مع:", horizontalAlignment = Alignment.CenterHorizontally
            )
            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GenderOption(
                    iconRes = R.drawable.ic_male,
                    text = Gender.Male.name,
                    uiState.preferGender,
                    onClick = {
                        onEvent(HomeEvent.OnGenderSelect(Gender.Male))
                    })
                GenderOption(
                    iconRes = R.drawable.ic_both_male_female,
                    text = Gender.Both.name,
                    uiState.preferGender,
                    onClick = {
                        onEvent(HomeEvent.OnGenderSelect(Gender.Both))
                    })
                GenderOption(
                    iconRes = R.drawable.ic_female,
                    text = Gender.Female.name,
                    uiState.preferGender,
                    onClick = {
                        onEvent(HomeEvent.OnGenderSelect(Gender.Female))
                    })
            }
        }
        BottomNavigationUI(
            onUnlockClick = {
                onEvent(HomeEvent.NavigateToSearch)
            },
            onConversationsClick = {
                onEvent(HomeEvent.OnConversationsClick)
            },
            onMyAccountClick = {
                onEvent(HomeEvent.OnMyAccountClick)
            }
        )
    }
}