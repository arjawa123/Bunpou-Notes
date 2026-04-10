package com.rjw.bunpoun3.data

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        WeekEntity::class,
        DayEntity::class,
        GrammarEntity::class,
        ExampleEntity::class,
        VerbFormGroupEntity::class,
        VerbFormRowEntity::class,
        LessonProgressEntity::class,
        QuizProgressEntity::class,
        SettingEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AppDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `quiz_progress` (
                `dayId` INTEGER NOT NULL,
                `attempts` INTEGER NOT NULL,
                `bestPercentage` INTEGER NOT NULL,
                `lastPercentage` INTEGER NOT NULL,
                `passed` INTEGER NOT NULL,
                `updatedAt` INTEGER NOT NULL,
                PRIMARY KEY(`dayId`)
            )
            """.trimIndent(),
        )
    }
}
