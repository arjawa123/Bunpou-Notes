package com.rjw.bunpoun3.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT COUNT(*) FROM weeks")
    suspend fun weekCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeks(items: List<WeekEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(items: List<DayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrammar(items: List<GrammarEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamples(items: List<ExampleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerbFormGroups(items: List<VerbFormGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerbFormRows(items: List<VerbFormRowEntity>)

    @Query("SELECT * FROM weeks ORDER BY weekNum")
    suspend fun getWeeks(): List<WeekEntity>

    @Query("SELECT * FROM days ORDER BY weekNum, dayNum")
    suspend fun getDays(): List<DayEntity>

    @Query("SELECT * FROM grammar_points ORDER BY dayId, position")
    suspend fun getGrammar(): List<GrammarEntity>

    @Query("SELECT * FROM examples ORDER BY grammarId, isExtra, position")
    suspend fun getExamples(): List<ExampleEntity>

    @Query("SELECT * FROM verb_form_groups")
    suspend fun getVerbFormGroups(): List<VerbFormGroupEntity>

    @Query("SELECT * FROM verb_form_rows ORDER BY badge, position")
    suspend fun getVerbFormRows(): List<VerbFormRowEntity>

    @Query("SELECT * FROM lesson_progress")
    fun observeProgress(): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(item: LessonProgressEntity)

    @Query("DELETE FROM lesson_progress WHERE dayId = :dayId")
    suspend fun deleteProgress(dayId: Int)

    @Query("SELECT * FROM settings")
    fun observeSettings(): Flow<List<SettingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSetting(item: SettingEntity)
}
