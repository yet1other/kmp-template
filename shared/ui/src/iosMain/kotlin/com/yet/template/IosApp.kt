package com.yet.template

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import com.yet.template.ui.App
import platform.Foundation.NSCoder
import platform.UIKit.UIViewController

/**
 * The iOS entry point, owned by the Swift `AppDelegate` (`IosApp()`). Only the UIKit parts live here
 * (ApplicationLifecycle, ComposeUIViewController); the graph, root and state live in `AppRoot` (nativeMain).
 */
class IosApp {
    private val appRoot = AppRoot(ApplicationLifecycle())

    /** `application(_:shouldSaveSecureApplicationState:)` */
    fun saveState(coder: NSCoder) = appRoot.saveState(coder)

    /** `application(_:shouldRestoreSecureApplicationState:)`, called before the UI exists. */
    fun restoreState(coder: NSCoder) = appRoot.restoreState(coder)

    fun mainViewController(): UIViewController = ComposeUIViewController { App(appRoot.component) }
}
