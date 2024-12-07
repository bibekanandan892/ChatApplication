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
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.BottomNavigationUI
import com.bibek.chatapplication.presentation.component.GenderOption
import com.bibek.chatapplication.presentation.component.Header
import com.bibek.chatapplication.presentation.component.RightAlignText
import com.bibek.chatapplication.presentation.screen.signup.Gender

/**
 * Composable function that represents the Home Screen UI.
 * Displays the header, gender selection options, and bottom navigation.
 *
 * @param uiState The current state of the Home screen, including user preferences like selected gender.
 * @param onEvent A lambda function that handles events triggered by user actions such as selecting a gender or navigating.
 */
@Composable
fun HomeScreen(uiState: HomeState, onEvent: (HomeEvent) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header displaying the text "ابحث عن صديق"
        Header(text = "ابحث عن صديق")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text displayed above the gender selection options
            RightAlignText(
                text = "فضفض مع:", horizontalAlignment = Alignment.CenterHorizontally
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Row containing the gender selection options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Male gender option
                GenderOption(
                    iconRes = R.drawable.ic_male,
                    text = Gender.Male.name,
                    uiState.preferGender,
                    onClick = {
                        onEvent(HomeEvent.OnGenderSelect(Gender.Male))
                    })

                // Both gender option
                GenderOption(
                    iconRes = R.drawable.ic_both_male_female,
                    text = Gender.Both.name,
                    uiState.preferGender,
                    onClick = {
                        onEvent(HomeEvent.OnGenderSelect(Gender.Both))
                    })

                // Female gender option
                GenderOption(
                    iconRes = R.drawable.ic_female,
                    text = Gender.Female.name,
                    uiState.preferGender,
                    onClick = {
                        onEvent(HomeEvent.OnGenderSelect(Gender.Female))
                    })
            }
        }

        // Bottom navigation UI with options for navigating to search, conversations, and account sections
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
