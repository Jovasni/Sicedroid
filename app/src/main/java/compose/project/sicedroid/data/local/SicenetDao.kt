package compose.project.sicedroid.data.local

import androidx.room.*

@Dao
interface SicenetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Unit

    @Query("SELECT * FROM profile_data WHERE id = 1")
    suspend fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarga(carga: CargaEntity): Unit

    @Query("SELECT * FROM carga_academica WHERE id = 1")
    suspend fun getCarga(): CargaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKardex(kardex: KardexEntity): Unit

    @Query("SELECT * FROM kardex WHERE id = 1")
    suspend fun getKardex(): KardexEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalifUnidad(calif: CalifUnidadEntity): Unit

    @Query("SELECT * FROM calificaciones_unidades WHERE id = 1")
    suspend fun getCalifUnidad(): CalifUnidadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalifFinal(calif: CalifFinalEntity): Unit

    @Query("SELECT * FROM calificaciones_finales WHERE id = 1")
    suspend fun getCalifFinal(): CalifFinalEntity?
}
