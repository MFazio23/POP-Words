package dev.mfazio.popwords.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(
    entities = [AttemptEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class HFWDatabase : RoomDatabase() {

    abstract fun hfwDao(): HFWDao

    companion object {
        @Volatile
        private var dbInstance: HFWDatabase? = null
        private const val dbName = "HFWDatabase"

        fun getInstance(context: Context): HFWDatabase =
            this.dbInstance ?: synchronized(this) {
                this.dbInstance ?: Room.databaseBuilder(context, HFWDatabase::class.java, dbName)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            Executors.newSingleThreadExecutor().execute {
                                getInstance(context).hfwDao()
                            }
                        }
                    }).build()
            }
    }

}