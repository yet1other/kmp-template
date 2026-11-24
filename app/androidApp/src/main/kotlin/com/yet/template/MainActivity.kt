package com.yet.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.yet.template.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Retained across configuration changes; back presses reach the stack through its back handler.
        val root = retainedComponent { context ->
            (application as KmpTemplateApplication).graph.rootComponentFactory(context)
        }

        setContent { App(root) }
    }
}
