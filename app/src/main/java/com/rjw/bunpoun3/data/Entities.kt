package com.rjw.bunpoun3.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "weeks")
data class WeekEntity(
    @PrimaryKey val weekNum: Int,
    val title: String,
    val titleId: String,
)

@Entity(
    tableName = "days",
    indices = [Index(value = ["weekNum", "dayNum"], unique = true)],
)
data class DayEntity(
    @PrimaryKey val dayId: Int,
    val weekNum: Int,
    val dayNum: Int,
    val title: String,
    val titleId: String,
)

@Entity(tableName = "grammar_points")
data class GrammarEntity(
    @PrimaryKey val grammarId: String,
    val dayId: Int,
    val position: Int,
    val pattern: String,
    val badge: String,
    val meaning: String,
    val meaningId: String,
)

@Entity(tableName = "examples")
data class ExampleEntity(
    @PrimaryKey val exampleId: String,
    val grammarId: String,
    val position: Int,
    val isExtra: Boolean,
    val japaneseHtml: String,
    val indonesian: String,
)

@Entity(tableName = "verb_form_groups")
data class VerbFormGroupEntity(
    @PrimaryKey val badge: String,
    val title: String,
)

@Entity(tableName = "verb_form_rows")
data class VerbFormRowEntity(
    @PrimaryKey val rowId: String,
    val badge: String,
    val position: Int,
    val label: String,
    val body: String,
    val bodyId: String,
    val example: String,
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val dayId: Int,
    val isDone: Boolean,
)

@Entity(tableName = "quiz_progress")
data class QuizProgressEntity(
    @PrimaryKey val dayId: Int,
    val attempts: Int,
    val bestPercentage: Int,
    val lastPercentage: Int,
    val passed: Boolean,
    val updatedAt: Long,
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val key: String,
    val value: String,
)
