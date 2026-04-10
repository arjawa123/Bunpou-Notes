package com.rjw.bunpoun3.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        WeekEntity::class,
        DayEntity::class,
        GrammarEntity::class,
        ExampleEntity::class,
        VerbFormGroupEntity::class,
        VerbFormRowEntity::class,
        LessonProgressEntity::class,
        SettingEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): AppDao
}
