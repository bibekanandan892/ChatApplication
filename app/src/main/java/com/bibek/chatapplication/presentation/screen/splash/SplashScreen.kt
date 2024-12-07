package com.bibek.chatapplication.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.bibek.chatapplication.R
@Composable
fun SplashScreen(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(R.drawable.ic_logo), contentDescription = stringResource(R.string.logo))
    }
}