package mbenedictt.paba.latihanuas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [daftarAir::class], version = 1)
abstract class daftarAirDB : RoomDatabase() {
    abstract fun fundaftarAirDAO() : daftarAirDAO

    companion object {
        @Volatile
        private var INSTANCE: daftarAirDB? = null

        @JvmStatic
        fun getDatabase(context: Context): daftarAirDB {
            if (INSTANCE == null) {
                synchronized(daftarAirDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        daftarAirDB::class.java, "daftarAir_db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as daftarAirDB
        }

    }

}