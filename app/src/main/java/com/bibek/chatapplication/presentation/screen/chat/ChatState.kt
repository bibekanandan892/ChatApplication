package com.bibek.chatapplication.presentation.screen.chat

import com.bibek.chatapplication.presentation.screen.signup.Gender

data class ChatState(val gender: String = "", val preferGender: Gender? = null, val name: String = "")
