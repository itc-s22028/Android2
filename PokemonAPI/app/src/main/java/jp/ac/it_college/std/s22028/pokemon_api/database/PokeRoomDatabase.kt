package jp.ac.it_college.std.s22028.pokemon_api.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jp.ac.it_college.std.s22028.pokemon_api.database.dao.PokeDao
import jp.ac.it_college.std.s22028.pokemon_api.database.entity.Poke


@Database(
    version = 1,
    entities = [
        Poke::class,
    ]
)
abstract class PokeRoomDatabase : RoomDatabase() {
    // PokeDao を取得する用のメソッド
    abstract fun pokeDao(): PokeDao

    companion object{
        @Volatile
        private var INSTANCE: PokeRoomDatabase? = null

        fun getDatabase(context: Context): PokeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokeRoomDatabase::class.java,
                    "poke_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}