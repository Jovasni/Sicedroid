package compose.project.sicedroid.data.local

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import kotlinx.coroutines.runBlocking

class SicenetContentProvider : ContentProvider() {

    private lateinit var database: AppDatabase
    
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(SicenetContract.AUTHORITY, SicenetContract.PATH_CARGA, 1)
        addURI(SicenetContract.AUTHORITY, SicenetContract.PATH_KARDEX, 2)
    }

    override fun onCreate(): Boolean {
        database = AppDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val match = uriMatcher.match(uri)
        val cursor = MatrixCursor(arrayOf(SicenetContract.CargaEntry.COLUMN_DATA, SicenetContract.CargaEntry.COLUMN_LAST_UPDATED))
        
        return when (match) {
            1 -> { // CARGA
                runBlocking {
                    val entity = database.sicenetDao().getCarga()
                    entity?.let { cursor.addRow(arrayOf(it.data, it.lastUpdated)) }
                }
                cursor
            }
            2 -> { // KARDEX
                runBlocking {
                    val entity = database.sicenetDao().getKardex()
                    entity?.let { cursor.addRow(arrayOf(it.data, it.lastUpdated)) }
                }
                cursor
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/${SicenetContract.AUTHORITY}.${SicenetContract.PATH_CARGA}"
            2 -> "vnd.android.cursor.dir/${SicenetContract.AUTHORITY}.${SicenetContract.PATH_KARDEX}"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Insert, Update, Delete implemented as placeholders or using local logic if needed
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = uriMatcher.match(uri)
        return when (match) {
            1 -> {
                val data = values?.getAsString(SicenetContract.CargaEntry.COLUMN_DATA) ?: ""
                runBlocking { database.sicenetDao().insertCarga(CargaEntity(data = data)) }
                context?.contentResolver?.notifyChange(uri, null)
                uri
            }
            2 -> {
                val data = values?.getAsString(SicenetContract.KardexEntry.COLUMN_DATA) ?: ""
                runBlocking { database.sicenetDao().insertKardex(KardexEntity(data = data)) }
                context?.contentResolver?.notifyChange(uri, null)
                uri
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
