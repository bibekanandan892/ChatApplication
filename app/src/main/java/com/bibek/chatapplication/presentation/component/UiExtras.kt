/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bibek.chatapplication.presentation.component

import androidx.compose.runtime.Composable

@Composable
fun FunctionalityNotAvailablePopup(onDismiss: () -> Unit) {
    androidx.compose.material.AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            androidx.compose.material.Text(
                text = "Functionality not available \uD83D\uDE48",
            )
        },
        confirmButton = {
            androidx.compose.material.TextButton(onClick = onDismiss) {
                androidx.compose.material.Text(text = "CLOSE")
            }
        }
    )
}
