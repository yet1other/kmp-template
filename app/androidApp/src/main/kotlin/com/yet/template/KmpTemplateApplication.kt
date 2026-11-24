package com.yet.template

import android.app.Application
import com.yet.template.di.AppGraph
import com.yet.template.di.createAndroidAppGraph

/** Owns the app graph for the process; activities take the root factory from it. */
class KmpTemplateApplication : Application() {
    val graph: AppGraph by lazy { createAndroidAppGraph(this) }
}
