package com.rjw.bunpoun3.data

import android.content.Context
import androidx.room.Room
import com.rjw.bunpoun3.model.VerbFormSeed
import com.rjw.bunpoun3.model.WeekSeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.math.max

class AppRepository private constructor(
    private val context: Context,
    private val db: AppDatabase,
) {
    private val dao = db.dao()
    private val json = Json { ignoreUnknownKeys = true }

    fun observeProgress(): Flow<Set<Int>> =
        dao.observeProgress().map { items -> items.filter { it.isDone }.map { it.dayId }.toSet() }

    fun observeSettings(): Flow<Map<String, String>> =
        dao.observeSettings().map { items -> items.associate { it.key to it.value } }

    fun observeQuizProgress(): Flow<Map<Int, DayQuizProgress>> =
        dao.observeQuizProgress().map { items ->
            items.associate { entity ->
                entity.dayId to DayQuizProgress(
                    dayId = entity.dayId,
                    attempts = entity.attempts,
                    bestPercentage = entity.bestPercentage,
                    lastPercentage = entity.lastPercentage,
                    passed = entity.passed,
                    updatedAt = entity.updatedAt,
                )
            }
        }

    suspend fun ensureSeeded() {
        if (dao.weekCount() > 0) return

        val weeks = json.decodeFromString<List<WeekSeed>>(assetText("weeks.json"))
        val extraExamples = json.decodeFromString<Map<String, List<com.rjw.bunpoun3.model.ExampleSeed>>>(assetText("extra_examples.json"))
        val verbForms = json.decodeFromString<Map<String, VerbFormSeed>>(assetText("verbforms.json"))

        val weekEntities = mutableListOf<WeekEntity>()
        val dayEntities = mutableListOf<DayEntity>()
        val grammarEntities = mutableListOf<GrammarEntity>()
        val exampleEntities = mutableListOf<ExampleEntity>()

        weeks.forEach { week ->
            weekEntities += WeekEntity(week.num, week.title, week.titleId)
            week.days.forEach { day ->
                val dayId = dayId(week.num, day.day)
                dayEntities += DayEntity(dayId, week.num, day.day, day.title, day.titleId)
                day.grammar.forEachIndexed { grammarIndex, grammar ->
                    val grammarId = "$dayId-$grammarIndex"
                    grammarEntities += GrammarEntity(
                        grammarId = grammarId,
                        dayId = dayId,
                        position = grammarIndex,
                        pattern = grammar.pattern,
                        badge = grammar.badge,
                        meaning = grammar.meaning,
                        meaningId = grammar.meaningId,
                    )
                    grammar.examples.forEachIndexed { index, example ->
                        exampleEntities += ExampleEntity(
                            exampleId = "$grammarId-main-$index",
                            grammarId = grammarId,
                            position = index,
                            isExtra = false,
                            japaneseHtml = example.ja,
                            indonesian = example.id,
                        )
                    }
                    extraExamples[grammar.pattern].orEmpty().forEachIndexed { index, example ->
                        exampleEntities += ExampleEntity(
                            exampleId = "$grammarId-extra-$index",
                            grammarId = grammarId,
                            position = index,
                            isExtra = true,
                            japaneseHtml = example.ja,
                            indonesian = example.id,
                        )
                    }
                }
            }
        }

        val groupEntities = verbForms.map { (badge, group) ->
            VerbFormGroupEntity(badge = badge, title = group.title)
        }
        val rowEntities = verbForms.flatMap { (badge, group) ->
            group.rows.mapIndexed { index, row ->
                VerbFormRowEntity(
                    rowId = "$badge-$index",
                    badge = badge,
                    position = index,
                    label = row.label,
                    body = row.body,
                    bodyId = row.id,
                    example = row.ex,
                )
            }
        }

        dao.insertWeeks(weekEntities)
        dao.insertDays(dayEntities)
        dao.insertGrammar(grammarEntities)
        dao.insertExamples(exampleEntities)
        dao.insertVerbFormGroups(groupEntities)
        dao.insertVerbFormRows(rowEntities)
    }

    suspend fun loadCatalog(): Catalog {
        val weeks = dao.getWeeks()
        val days = dao.getDays()
        val grammar = dao.getGrammar()
        val examples = dao.getExamples()
        val groups = dao.getVerbFormGroups().associateBy { it.badge }
        val rows = dao.getVerbFormRows().groupBy { it.badge }

        val examplesByGrammar = examples.groupBy { it.grammarId }
        val grammarByDay = grammar.groupBy { it.dayId }
        val daysByWeek = days.groupBy { it.weekNum }

        val weekItems = weeks.map { week ->
            CatalogWeek(
                weekNum = week.weekNum,
                title = week.title,
                titleId = week.titleId,
                days = daysByWeek[week.weekNum].orEmpty().sortedBy { it.dayNum }.map { day ->
                    CatalogDay(
                        dayId = day.dayId,
                        weekNum = day.weekNum,
                        dayNum = day.dayNum,
                        title = day.title,
                        titleId = day.titleId,
                        grammarPoints = grammarByDay[day.dayId].orEmpty().sortedBy { it.position }.map { point ->
                            CatalogGrammar(
                                grammarId = point.grammarId,
                                pattern = point.pattern,
                                badge = point.badge,
                                meaning = point.meaning,
                                meaningId = point.meaningId,
                                examples = examplesByGrammar[point.grammarId].orEmpty()
                                    .filterNot { it.isExtra }
                                    .sortedBy { it.position }
                                    .map { CatalogExample(it.japaneseHtml, it.indonesian) },
                                extraExamples = examplesByGrammar[point.grammarId].orEmpty()
                                    .filter { it.isExtra }
                                    .sortedBy { it.position }
                                    .map { CatalogExample(it.japaneseHtml, it.indonesian) },
                                verbForm = groups[point.badge]?.let { group ->
                                    CatalogVerbForm(
                                        title = group.title,
                                        rows = rows[point.badge].orEmpty().sortedBy { it.position }.map {
                                            CatalogVerbFormRow(
                                                label = it.label,
                                                body = it.body,
                                                bodyId = it.bodyId,
                                                example = it.example,
                                            )
                                        },
                                    )
                                },
                            )
                        },
                    )
                },
            )
        }

        return Catalog(
            weeks = weekItems,
            accessCode = assetText("access_code.txt").trim(),
            readings = json.decodeFromString<Map<String, String>>(assetText("readings.json")),
            romajiMap = json.decodeFromString<Map<String, String>>(assetText("romaji.json")),
        )
    }

    suspend fun setProgress(dayId: Int, done: Boolean) {
        if (done) dao.upsertProgress(LessonProgressEntity(dayId, true)) else dao.deleteProgress(dayId)
    }

    suspend fun recordDayQuizResult(
        dayId: Int,
        percentage: Int,
        passed: Boolean,
    ) {
        val current = dao.getQuizProgress(dayId)
        dao.upsertQuizProgress(
            QuizProgressEntity(
                dayId = dayId,
                attempts = (current?.attempts ?: 0) + 1,
                bestPercentage = max(current?.bestPercentage ?: 0, percentage),
                lastPercentage = percentage,
                passed = current?.passed == true || passed,
                updatedAt = System.currentTimeMillis(),
            ),
        )
        if (passed) {
            dao.upsertProgress(LessonProgressEntity(dayId = dayId, isDone = true))
        }
    }

    suspend fun setSetting(key: String, value: String) {
        dao.upsertSetting(SettingEntity(key, value))
    }

    private fun assetText(name: String): String =
        context.assets.open(name).bufferedReader().use { it.readText() }

    companion object {
        fun create(context: Context): AppRepository {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "bunpou_n3.db",
            ).addMigrations(MIGRATION_1_2).build()
            return AppRepository(context.applicationContext, db)
        }

        fun dayId(weekNum: Int, dayNum: Int): Int = weekNum * 100 + dayNum
    }
}

data class Catalog(
    val weeks: List<CatalogWeek>,
    val accessCode: String,
    val readings: Map<String, String>,
    val romajiMap: Map<String, String>,
)

data class CatalogWeek(
    val weekNum: Int,
    val title: String,
    val titleId: String,
    val days: List<CatalogDay>,
)

data class CatalogDay(
    val dayId: Int,
    val weekNum: Int,
    val dayNum: Int,
    val title: String,
    val titleId: String,
    val grammarPoints: List<CatalogGrammar>,
)

data class CatalogGrammar(
    val grammarId: String,
    val pattern: String,
    val badge: String,
    val meaning: String,
    val meaningId: String,
    val examples: List<CatalogExample>,
    val extraExamples: List<CatalogExample>,
    val verbForm: CatalogVerbForm?,
)

data class CatalogExample(
    val japaneseHtml: String,
    val indonesian: String,
)

data class CatalogVerbForm(
    val title: String,
    val rows: List<CatalogVerbFormRow>,
)

data class CatalogVerbFormRow(
    val label: String,
    val body: String,
    val bodyId: String,
    val example: String,
)

data class DayQuizProgress(
    val dayId: Int,
    val attempts: Int,
    val bestPercentage: Int,
    val lastPercentage: Int,
    val passed: Boolean,
    val updatedAt: Long,
)
