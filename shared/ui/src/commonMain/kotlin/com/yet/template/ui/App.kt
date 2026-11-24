package com.yet.template.ui

import androidx.compose.runtime.Composable
import com.yet.template.feature.root.RootComponent
import com.yet.template.resources.AppTheme
import com.yet.template.ui.root.RootContent

/** The UI entry point for every platform: Android's MainActivity and iOS's IosApp. */
@Composable
fun App(root: RootComponent) {
    AppTheme {
        RootContent(root)
    }
}
