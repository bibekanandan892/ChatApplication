package com.bibek.chatapplication.presentation.screen.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bibek.chatapplication.R
import com.bibek.chatapplication.presentation.component.Button
import com.bibek.chatapplication.presentation.component.GenderOption
import com.bibek.chatapplication.presentation.component.Header
import com.bibek.chatapplication.presentation.component.RightAlignText
import com.bibek.chatapplication.presentation.component.TextBox
import com.bibek.chatapplication.utils.getAndroidId

/**
 * A composable function representing the signup screen in the application.
 *
 * This screen allows the user to input their name, select their gender, and proceed with the signup process.
 * The UI updates based on the current state (`SignupState`), and user actions trigger events via the `onEvent` lambda.
 *
 * @param uiState The current state of the signup process, holding data such as loading state, gender, and the user's name.
 * @param onEvent A lambda function that handles events triggered by user interactions. This function is responsible for dispatching the appropriate event for state changes.
 */
@Composable
fun SignupScreen(uiState: SignupState, onEvent: (SignupEvent) -> Unit = {}) {
    // Get the current context for accessing Android system resources
    val context = LocalContext.current

    // The root Column that arranges the content vertically
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header displaying the greeting text
        Header(text = "حياك بيننا")

        // Column for the name input section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Name input label
            Text(
                text = "اسمك",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            // TextBox for the user to input their name
            TextBox(
                text = uiState.udid,
                hint = "يجب أن لا يقل عن ٣ أحرف ",
                onTextChange = {
                    onEvent(SignupEvent.OnNameChange(it)) // Dispatch event when name changes
                }
            )
        }

        // Column for the gender selection section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Spacer for spacing between elements
            Spacer(modifier = Modifier.height(24.dp))

            // Gender label
            RightAlignText(text = "جنسك")

            // Row for gender option buttons (Male and Female)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Gender option button for Male
                GenderOption(
                    iconRes = R.drawable.ic_male,
                    text = Gender.Male.name,
                    uiState.gender,
                    onClick = {
                        onEvent(SignupEvent.OnGenderSelect(Gender.Male)) // Dispatch event when Male is selected
                    })
                // Gender option button for Female
                GenderOption(
                    iconRes = R.drawable.ic_female,
                    text = Gender.Female.name,
                    uiState.gender,
                    onClick = {
                        onEvent(SignupEvent.OnGenderSelect(Gender.Female)) // Dispatch event when Female is selected
                    })
            }
        }

        // Spacer for spacing
        Spacer(modifier = Modifier.height(24.dp))

        // Column for the terms and conditions message
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "استمرارك يعني موافقتك على الشروط والأحكام",
                style = MaterialTheme.typography.body2,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Continue button that triggers the signup process
        Button(
            text = "استمر",
            onClick = { onEvent(SignupEvent.OnSignupClick(getAndroidId(context = context))) }, // Trigger OnSignupClick event
            modifier = Modifier.fillMaxWidth(),
            isLoading = uiState.isLoading // Show loading state if needed
        )
    }
}



