package com.bibek.chatapplication.presentation.screen.signup

data class SignupState(
    val isLoading: Boolean = false,
    val gender : Gender? = null,
    val udid : String = ""
    )

sealed class Gender(val name : String){
    data object Male: Gender(name ="ذكر")
    data object Female : Gender(name =  "أنثى")
    data object Both : Gender(name = "الكل")
}
