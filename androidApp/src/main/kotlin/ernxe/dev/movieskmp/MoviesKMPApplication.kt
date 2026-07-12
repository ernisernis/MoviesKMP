package ernxe.dev.movieskmp

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication
import ernxe.dev.movieskmp.di.MetroGraph

class MoviesKMPApplication : Application(), MetroApplication {
    override val appComponentProviders: MetroAppComponentProviders by lazy { createGraphFactory<MetroGraph.Factory>().create() }
}