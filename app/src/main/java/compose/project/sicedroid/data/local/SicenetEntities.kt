package compose.project.sicedroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_data")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    val data: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "carga_academica")
data class CargaEntity(
    @PrimaryKey val id: Int = 1,
    val data: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "kardex")
data class KardexEntity(
    @PrimaryKey val id: Int = 1,
    val data: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "calificaciones_unidades")
data class CalifUnidadEntity(
    @PrimaryKey val id: Int = 1,
    val data: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "calificaciones_finales")
data class CalifFinalEntity(
    @PrimaryKey val id: Int = 1,
    val data: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
