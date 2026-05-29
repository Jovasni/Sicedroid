package compose.project.sicedroid.data.local

import android.net.Uri

object SicenetContract {
    const val AUTHORITY = "compose.project.sicedroid.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    const val PATH_CARGA = "carga"
    const val PATH_KARDEX = "kardex"

    object CargaEntry {
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARGA).build()
        const val COLUMN_DATA = "data"
        const val COLUMN_LAST_UPDATED = "lastUpdated"
    }

    object KardexEntry {
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_KARDEX).build()
        const val COLUMN_DATA = "data"
        const val COLUMN_LAST_UPDATED = "lastUpdated"
    }
}
