package com.bibek.chatapplication.presentation.screen.search

import com.bibek.chatapplication.presentation.screen.signup.Gender

data class SearchState (
val isLoading: Boolean = false,
val gender : Gender? = null,
val name : String = ""
)