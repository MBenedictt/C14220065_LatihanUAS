package mbenedictt.paba.latihanuas.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface daftarAirDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(daftarAir: daftarAir)

    @Query("UPDATE daftarAir SET nama=:isi_nama, berat=:isi_berat, air=:isi_air WHERE id=:pilihid")
    fun update(isi_nama: String, isi_berat: String, isi_air: String, pilihid: Int)

    @Delete
    fun delete(daftar: daftarAir)

    @Query("SELECT * FROM daftarAir ORDER BY id asc")
    fun selectAll() : MutableList<daftarAir>

    @Query("SELECT * FROM daftarAir WHERE id=:isi_id")
    suspend fun getItem(isi_id: Int) : daftarAir
}