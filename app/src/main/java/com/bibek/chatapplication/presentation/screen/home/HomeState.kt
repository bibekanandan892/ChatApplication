package com.bibek.chatapplication.presentation.screen.home

import com.bibek.chatapplication.presentation.screen.signup.Gender

data class HomeState(val gender: String = "", val preferGender: Gender? = null, val name: String = "")
