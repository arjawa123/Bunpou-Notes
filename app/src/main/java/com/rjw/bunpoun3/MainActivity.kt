package com.rjw.bunpoun3

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.rjw.bunpoun3.data.AppRepository
import com.rjw.bunpoun3.data.Catalog
import com.rjw.bunpoun3.data.CatalogDay
import com.rjw.bunpoun3.data.CatalogExample
import com.rjw.bunpoun3.data.CatalogGrammar
import com.rjw.bunpoun3.data.CatalogWeek
import com.rjw.bunpoun3.ui.theme.BunpouTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BunpouTheme {
                val vm: MainViewModel = viewModel(
                    factory = object : ViewModelProvider.AndroidViewModelFactory(application) {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return MainViewModel(application) as T
                        }
                    },
                )
                val state by vm.uiState.collectAsStateWithLifecycle()
                AppRoot(state = state, vm = vm)
            }
        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository.create(application)
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val random = Random(System.currentTimeMillis())

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            val catalog = repository.loadCatalog()
            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    catalog = catalog,
                )
            }
        }
        viewModelScope.launch {
            repository.observeProgress().collect { progress ->
                _uiState.update { it.copy(completedDayIds = progress) }
            }
        }
        viewModelScope.launch {
            repository.observeSettings().collect { settings ->
                _uiState.update { state ->
                    state.copy(
                        accessGranted = settings[SETTING_ACCESS] == "true",
                        furiganaOn = settings[SETTING_FURIGANA] != "false",
                        romajiOn = settings[SETTING_ROMAJI] != "false",
                    )
                }
            }
        }
    }

    fun goHome() {
        _uiState.update { it.copy(screen = Screen.Home) }
    }

    fun openWeek(weekNum: Int) {
        _uiState.update { it.copy(screen = Screen.Week(weekNum)) }
    }

    fun openLesson(dayId: Int) {
        _uiState.update { it.copy(screen = Screen.Lesson(dayId)) }
    }

    fun submitAccess(input: String) {
        val code = uiState.value.catalog?.accessCode ?: return
        if (input.trim() == code) {
            viewModelScope.launch {
                repository.setSetting(SETTING_ACCESS, "true")
            }
            _uiState.update { it.copy(accessError = null) }
        } else {
            _uiState.update { it.copy(accessError = "Kode salah / コードが違います。") }
        }
    }

    fun toggleFurigana() {
        val next = !uiState.value.furiganaOn
        viewModelScope.launch { repository.setSetting(SETTING_FURIGANA, next.toString()) }
    }

    fun toggleRomaji() {
        val next = !uiState.value.romajiOn
        viewModelScope.launch { repository.setSetting(SETTING_ROMAJI, next.toString()) }
    }

    fun toggleComplete(dayId: Int) {
        val next = !uiState.value.completedDayIds.contains(dayId)
        viewModelScope.launch { repository.setProgress(dayId, next) }
    }

    fun openPrevLesson(day: CatalogDay) {
        dayBy(weekNum = day.weekNum, dayNum = day.dayNum - 1)?.let { openLesson(it.dayId) }
    }

    fun openNextLesson(day: CatalogDay) {
        dayBy(weekNum = day.weekNum, dayNum = day.dayNum + 1)?.let { openLesson(it.dayId) }
    }

    fun startDayQuiz(day: CatalogDay) {
        val questions = buildQuiz(day.grammarPoints.map { grammar -> grammar to day.weekNum })
        _uiState.update {
            it.copy(
                screen = Screen.Quiz,
                quizState = QuizState(
                    title = "第${day.weekNum}週 ${day.dayNum}日目 クイズ",
                    questions = questions,
                ),
            )
        }
    }

    fun startAllQuiz() {
        val catalog = uiState.value.catalog ?: return
        val pool = catalog.weeks.flatMap { week ->
            week.days.flatMap { day -> day.grammarPoints.map { grammar -> grammar to week.weekNum } }
        }.shuffled(random).take(15)
        _uiState.update {
            it.copy(
                screen = Screen.Quiz,
                quizState = QuizState(
                    title = "クイズ全体 / Kuis Semua Materi",
                    questions = buildQuiz(pool),
                ),
            )
        }
    }

    fun answerQuiz(option: String) {
        val quiz = uiState.value.quizState ?: return
        if (quiz.selectedAnswer != null || quiz.finished) return
        val current = quiz.currentQuestion ?: return
        _uiState.update {
            it.copy(
                quizState = quiz.copy(
                    selectedAnswer = option,
                    score = quiz.score + if (option == current.correctPattern) 1 else 0,
                ),
            )
        }
    }

    fun nextQuizQuestion() {
        val quiz = uiState.value.quizState ?: return
        if (quiz.index + 1 >= quiz.questions.size) {
            _uiState.update { it.copy(quizState = quiz.copy(finished = true, selectedAnswer = null)) }
        } else {
            _uiState.update {
                it.copy(
                    quizState = quiz.copy(
                        index = quiz.index + 1,
                        selectedAnswer = null,
                    ),
                )
            }
        }
    }

    fun replayQuiz() {
        val quiz = uiState.value.quizState ?: return
        _uiState.update {
            it.copy(
                quizState = quiz.copy(
                    questions = quiz.questions.shuffled(random),
                    index = 0,
                    score = 0,
                    selectedAnswer = null,
                    finished = false,
                ),
            )
        }
    }

    fun backFromQuiz() {
        when (val previous = uiState.value.screenBeforeQuiz()) {
            is Screen.Lesson -> _uiState.update { it.copy(screen = previous, quizState = null) }
            else -> _uiState.update { it.copy(screen = Screen.Home, quizState = null) }
        }
    }

    private fun UiState.screenBeforeQuiz(): Screen =
        when {
            quizState?.title?.contains("日目") == true -> {
                val currentDay = currentDay() ?: return Screen.Home
                Screen.Lesson(currentDay.dayId)
            }
            else -> Screen.Home
        }

    private fun buildQuiz(source: List<Pair<CatalogGrammar, Int>>): List<QuizQuestion> {
        val catalog = uiState.value.catalog ?: return emptyList()
        val allPatterns = catalog.weeks.flatMap { it.days }.flatMap { it.grammarPoints }.map { it.pattern }
        return source.flatMap { (grammar, _) ->
            grammar.examples.map { example ->
                val distractors = allPatterns.filter { it != grammar.pattern }.shuffled(random).distinct().take(3)
                QuizQuestion(
                    promptHtml = example.japaneseHtml,
                    promptId = example.indonesian,
                    correctPattern = grammar.pattern,
                    options = (distractors + grammar.pattern).shuffled(random),
                    meaningId = grammar.meaningId,
                )
            }
        }.shuffled(random)
    }

    private fun currentDay(): CatalogDay? {
        val screen = uiState.value.screen
        if (screen !is Screen.Lesson) return null
        return uiState.value.catalog?.weeks
            ?.flatMap { it.days }
            ?.firstOrNull { it.dayId == screen.dayId }
    }

    private fun dayBy(weekNum: Int, dayNum: Int): CatalogDay? =
        uiState.value.catalog?.weeks?.firstOrNull { it.weekNum == weekNum }?.days?.firstOrNull { it.dayNum == dayNum }

    companion object {
        private const val SETTING_ACCESS = "access_granted"
        private const val SETTING_FURIGANA = "furigana_on"
        private const val SETTING_ROMAJI = "romaji_on"
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val catalog: Catalog? = null,
    val completedDayIds: Set<Int> = emptySet(),
    val accessGranted: Boolean = false,
    val furiganaOn: Boolean = true,
    val romajiOn: Boolean = true,
    val accessError: String? = null,
    val screen: Screen = Screen.Home,
    val quizState: QuizState? = null,
)

sealed interface Screen {
    data object Home : Screen
    data class Week(val weekNum: Int) : Screen
    data class Lesson(val dayId: Int) : Screen
    data object Quiz : Screen
}

data class QuizState(
    val title: String,
    val questions: List<QuizQuestion>,
    val index: Int = 0,
    val score: Int = 0,
    val selectedAnswer: String? = null,
    val finished: Boolean = false,
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(index)
}

data class QuizQuestion(
    val promptHtml: String,
    val promptId: String,
    val correctPattern: String,
    val options: List<String>,
    val meaningId: String,
)

@Composable
private fun AppRoot(
    state: UiState,
    vm: MainViewModel,
) {
    val catalog = state.catalog
    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading || catalog == null -> LoadingScreen()
            !state.accessGranted -> AccessScreen(state.accessError, vm::submitAccess)
            else -> {
                val title = when (val screen = state.screen) {
                    Screen.Home -> "日本語N3 文法"
                    is Screen.Week -> "第${screen.weekNum}週"
                    is Screen.Lesson -> {
                        catalog.weeks.flatMap { it.days }.firstOrNull { it.dayId == screen.dayId }?.title ?: "Lesson"
                    }
                    Screen.Quiz -> state.quizState?.title ?: "Quiz"
                }
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = title,
                            canGoBack = state.screen != Screen.Home,
                            onBack = {
                                when (val screen = state.screen) {
                                    Screen.Home -> Unit
                                    is Screen.Week -> vm.goHome()
                                    is Screen.Lesson -> vm.openWeek(
                                        catalog.weeks.first { week -> week.days.any { it.dayId == screen.dayId } }.weekNum,
                                    )
                                    Screen.Quiz -> vm.backFromQuiz()
                                }
                            },
                            furiganaOn = state.furiganaOn,
                            romajiOn = state.romajiOn,
                            onToggleFurigana = vm::toggleFurigana,
                            onToggleRomaji = vm::toggleRomaji,
                            onHome = vm::goHome,
                            onQuiz = vm::startAllQuiz,
                        )
                    },
                ) { padding ->
                    when (val screen = state.screen) {
                        Screen.Home -> HomeScreen(
                            weeks = catalog.weeks,
                            completedDayIds = state.completedDayIds,
                            onOpenWeek = vm::openWeek,
                            modifier = Modifier.padding(padding),
                        )
                        is Screen.Week -> WeekScreen(
                            week = catalog.weeks.first { it.weekNum == screen.weekNum },
                            completedDayIds = state.completedDayIds,
                            onOpenLesson = vm::openLesson,
                            modifier = Modifier.padding(padding),
                        )
                        is Screen.Lesson -> {
                            val day = catalog.weeks.flatMap { it.days }.first { it.dayId == screen.dayId }
                            LessonScreen(
                                day = day,
                                completed = state.completedDayIds.contains(day.dayId),
                                furiganaOn = state.furiganaOn,
                                romajiOn = state.romajiOn,
                                readings = catalog.readings,
                                romajiMap = catalog.romajiMap,
                                onToggleComplete = { vm.toggleComplete(day.dayId) },
                                onStartQuiz = { vm.startDayQuiz(day) },
                                onPrev = { vm.openPrevLesson(day) },
                                onNext = { vm.openNextLesson(day) },
                                hasPrev = day.dayNum > 1,
                                hasNext = catalog.weeks.first { it.weekNum == day.weekNum }.days.any { it.dayNum == day.dayNum + 1 },
                                modifier = Modifier.padding(padding),
                            )
                        }
                        Screen.Quiz -> QuizScreen(
                            quizState = state.quizState,
                            furiganaOn = state.furiganaOn,
                            readings = catalog.readings,
                            romajiMap = catalog.romajiMap,
                            onAnswer = vm::answerQuiz,
                            onNext = vm::nextQuizQuestion,
                            onReplay = vm::replayQuiz,
                            onHome = vm::goHome,
                            modifier = Modifier.padding(padding),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun AccessScreen(error: String?, onSubmit: (String) -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("日本語N3 文法アプリ", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Masukkan kode akses untuk melanjutkan.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("アクセスコード / Kode Akses") },
                    singleLine = true,
                )
                Button(
                    onClick = { onSubmit(value) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Masuk")
                }
                if (!error.isNullOrBlank()) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    title: String,
    canGoBack: Boolean,
    onBack: () -> Unit,
    furiganaOn: Boolean,
    romajiOn: Boolean,
    onToggleFurigana: () -> Unit,
    onToggleRomaji: () -> Unit,
    onHome: () -> Unit,
    onQuiz: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            IconButton(onClick = onHome) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = onQuiz) {
                Icon(Icons.Default.Quiz, contentDescription = "All quiz")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = furiganaOn,
            onClick = onToggleFurigana,
            label = { Text(if (furiganaOn) "ルビあり" else "ルビなし") },
            leadingIcon = { Icon(Icons.Default.TextFields, contentDescription = null) },
        )
        FilterChip(
            selected = romajiOn,
            onClick = onToggleRomaji,
            label = { Text(if (romajiOn) "ローマ字あり" else "ローマ字なし") },
            leadingIcon = { Icon(Icons.Default.Translate, contentDescription = null) },
        )
    }
}

@Composable
private fun HomeScreen(
    weeks: List<CatalogWeek>,
    completedDayIds: Set<Int>,
    onOpenWeek: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalDays = weeks.sumOf { it.days.size }
    val done = completedDayIds.size
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            ElevatedCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("日本語総まとめ N3 文法", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Belajar tata bahasa Jepang N3 — 6 minggu, 36 pelajaran")
                    LinearProgressIndicator(progress = { done.toFloat() / totalDays.toFloat() }, modifier = Modifier.fillMaxWidth())
                    Text("$done / $totalDays pelajaran selesai", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        items(weeks) { week ->
            val daysDone = week.days.count { completedDayIds.contains(it.dayId) }
            ElevatedCard(onClick = { onOpenWeek(week.weekNum) }) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("第${week.weekNum}週", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    Text(week.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(week.titleId, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LinearProgressIndicator(
                        progress = { daysDone.toFloat() / week.days.size.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text("$daysDone/${week.days.size} selesai", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun WeekScreen(
    week: CatalogWeek,
    completedDayIds: Set<Int>,
    onOpenLesson: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("第${week.weekNum}週", color = MaterialTheme.colorScheme.primary)
                    Text(week.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(week.titleId)
                }
            }
        }
        items(week.days) { day ->
            val completed = completedDayIds.contains(day.dayId)
            ElevatedCard(onClick = { onOpenLesson(day.dayId) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("${day.dayNum}日目", color = MaterialTheme.colorScheme.primary)
                        Text(day.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            day.grammarPoints.joinToString("・") { it.pattern },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            if (completed) "✓" else day.dayNum.toString(),
                            color = if (completed) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonScreen(
    day: CatalogDay,
    completed: Boolean,
    furiganaOn: Boolean,
    romajiOn: Boolean,
    readings: Map<String, String>,
    romajiMap: Map<String, String>,
    onToggleComplete: () -> Unit,
    onStartQuiz: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    hasPrev: Boolean,
    hasNext: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("第${day.weekNum}週 › ${day.dayNum}日目", color = MaterialTheme.colorScheme.primary)
                Text(day.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(day.titleId)
            }
        }
        day.grammarPoints.forEach { grammar ->
            GrammarCard(grammar, furiganaOn, romajiOn, readings, romajiMap)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (hasPrev) {
                OutlinedButton(onClick = onPrev, modifier = Modifier.weight(1f)) {
                    Text("← ${day.dayNum - 1}日目")
                }
            }
            Button(onClick = onToggleComplete, modifier = Modifier.weight(1f)) {
                Text(if (completed) "✓ 完了済み（取消）" else "完了 ✓")
            }
            Button(onClick = onStartQuiz, modifier = Modifier.weight(1f)) {
                Text("クイズ 🎯")
            }
            if (hasNext) {
                OutlinedButton(onClick = onNext, modifier = Modifier.weight(1f)) {
                    Text("${day.dayNum + 1}日目 →")
                }
            }
        }
    }
}

@Composable
private fun GrammarCard(
    grammar: CatalogGrammar,
    furiganaOn: Boolean,
    romajiOn: Boolean,
    readings: Map<String, String>,
    romajiMap: Map<String, String>,
) {
    var showVerbForm by rememberSaveable(grammar.grammarId) { mutableStateOf(false) }
    var showExtras by rememberSaveable(grammar.grammarId + "-extras") { mutableStateOf(false) }
    ElevatedCard {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(grammar.pattern, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                AssistChip(onClick = {}, label = { Text(grammar.badge) })
            }
            Text(grammar.meaning, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(
                    grammar.meaningId,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            grammar.verbForm?.let { verbForm ->
                OutlinedButton(onClick = { showVerbForm = !showVerbForm }) {
                    Text(if (showVerbForm) "Sembunyikan pola bentuk" else "Cara pembentukan")
                }
                if (showVerbForm) {
                    OutlinedCard {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(verbForm.title, fontWeight = FontWeight.Bold)
                            verbForm.rows.forEach { row ->
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(row.label, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                    Text(row.body)
                                    Text(row.bodyId, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    if (row.example.isNotBlank()) {
                                        Text("例: ${row.example}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Text("Contoh Kalimat / 例文", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            grammar.examples.forEach { example ->
                ExampleCard(example, furiganaOn, romajiOn, readings, romajiMap, false)
            }
            if (grammar.extraExamples.isNotEmpty()) {
                OutlinedButton(onClick = { showExtras = !showExtras }) {
                    Text(if (showExtras) "Sembunyikan contoh tambahan" else "Lihat contoh tambahan (${grammar.extraExamples.size})")
                }
                if (showExtras) {
                    grammar.extraExamples.forEach { example ->
                        ExampleCard(example, furiganaOn, romajiOn, readings, romajiMap, true)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExampleCard(
    example: CatalogExample,
    furiganaOn: Boolean,
    romajiOn: Boolean,
    readings: Map<String, String>,
    romajiMap: Map<String, String>,
    isExtra: Boolean,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isExtra) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            RubyText(example.japaneseHtml, furiganaOn, readings)
            if (romajiOn) {
                Text(
                    toRomaji(example.japaneseHtml, readings, romajiMap),
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Text(example.indonesian, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QuizScreen(
    quizState: QuizState?,
    furiganaOn: Boolean,
    readings: Map<String, String>,
    romajiMap: Map<String, String>,
    onAnswer: (String) -> Unit,
    onNext: () -> Unit,
    onReplay: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (quizState == null) return
    if (quizState.finished) {
        val percentage = if (quizState.questions.isEmpty()) 0 else (quizState.score * 100 / quizState.questions.size)
        val result = when {
            percentage >= 80 -> Triple("Luar biasa! Hebat sekali!", "素晴らしい！", "🎉")
            percentage >= 60 -> Triple("Bagus! Terus semangat!", "よくできました！", "😊")
            percentage >= 40 -> Triple("Lumayan! Masih bisa lebih baik.", "もう少し頑張りましょう！", "🙂")
            else -> Triple("Terus berlatih ya!", "もっと練習しましょう！", "😢")
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(result.third, fontSize = 56.sp)
            Spacer(Modifier.height(12.dp))
            Text("${quizState.score}/${quizState.questions.size}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(result.first, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium)
            Text(result.second, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(20.dp))
            LinearProgressIndicator(progress = { percentage / 100f }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(20.dp))
            Button(onClick = onReplay, modifier = Modifier.fillMaxWidth()) { Text("もう一度 / Ulangi") }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) { Text("ホームへ / Beranda") }
        }
        return
    }

    val question = quizState.currentQuestion ?: return
    val answered = quizState.selectedAnswer != null
    val isCorrect = quizState.selectedAnswer == question.correctPattern
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            ElevatedCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(quizState.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${quizState.score}/${quizState.index} 正解", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LinearProgressIndicator(
                        progress = { quizState.index.toFloat() / quizState.questions.size.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        item {
            ElevatedCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("問題 ${quizState.index + 1} / ${quizState.questions.size}")
                    RubyText(question.promptHtml, furiganaOn, readings)
                    Text(question.promptId, color = MaterialTheme.colorScheme.primary)
                    Text(
                        toRomaji(question.promptHtml, readings, romajiMap),
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
        items(question.options) { option ->
            val selected = quizState.selectedAnswer == option
            val correct = option == question.correctPattern
            val colors = when {
                !answered -> CardDefaults.elevatedCardColors()
                correct -> CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                selected -> CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                else -> CardDefaults.elevatedCardColors()
            }
            ElevatedCard(
                onClick = { onAnswer(option) },
                enabled = !answered,
                colors = colors,
            ) {
                Text(option, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Medium)
            }
        }
        if (answered) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer,
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(if (isCorrect) "正解！ Benar! ✓" else "不正解 Salah. ✗", fontWeight = FontWeight.Bold)
                        if (!isCorrect) Text("正解: ${question.correctPattern}", fontWeight = FontWeight.SemiBold)
                        Text(question.meaningId)
                    }
                }
            }
            item {
                Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                    Text(if (quizState.index + 1 < quizState.questions.size) "次の問題 →" else "結果を見る 🎉")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RubyText(
    html: String,
    showFurigana: Boolean,
    readings: Map<String, String>,
) {
    val segments = remember(html) { parseStrongSegments(stripTagsExceptStrong(html)) }
    val sorted = remember(readings) { readings.entries.sortedByDescending { it.key.length } }
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        segments.forEach { segment ->
            tokenize(segment.text, sorted).forEach { token ->
                if (showFurigana && token.reading != null && token.surface.any { isKanji(it) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            token.reading,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace,
                        )
                        Text(
                            token.surface,
                            fontWeight = if (segment.bold) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 18.sp,
                        )
                    }
                } else {
                    Text(
                        token.surface,
                        fontWeight = if (segment.bold) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 18.sp,
                    )
                }
            }
        }
    }
}

private data class StrongSegment(val text: String, val bold: Boolean)
private data class RubyToken(val surface: String, val reading: String?)

private fun stripTagsExceptStrong(html: String): String =
    html.replace(Regex("</?(?!strong\\b)[^>]+>"), "")

private fun stripTags(html: String): String = html.replace(Regex("<[^>]+>"), "")

private fun parseStrongSegments(html: String): List<StrongSegment> {
    val result = mutableListOf<StrongSegment>()
    val regex = Regex("(<strong>|</strong>)")
    var last = 0
    var bold = false
    regex.findAll(html).forEach { match ->
        val text = html.substring(last, match.range.first)
        if (text.isNotEmpty()) result += StrongSegment(text, bold)
        bold = match.value == "<strong>"
        last = match.range.last + 1
    }
    val tail = html.substring(last)
    if (tail.isNotEmpty()) result += StrongSegment(tail, bold)
    return result
}

private fun tokenize(text: String, sortedReadings: List<Map.Entry<String, String>>): List<RubyToken> {
    val tokens = mutableListOf<RubyToken>()
    val plain = StringBuilder()
    var index = 0
    while (index < text.length) {
        val matched = sortedReadings.firstOrNull { (word, _) -> text.startsWith(word, index) }
        if (matched != null) {
            if (plain.isNotEmpty()) {
                tokens += RubyToken(plain.toString(), null)
                plain.clear()
            }
            tokens += RubyToken(matched.key, matched.value)
            index += matched.key.length
        } else {
            plain.append(text[index])
            index++
        }
    }
    if (plain.isNotEmpty()) tokens += RubyToken(plain.toString(), null)
    return tokens
}

private fun isKanji(ch: Char): Boolean = ch.code in 0x4E00..0x9FFF

private fun toRomaji(
    html: String,
    readings: Map<String, String>,
    romajiMap: Map<String, String>,
): String {
    val plain = stripTags(html)
    val sorted = readings.entries.sortedByDescending { it.key.length }
    val kana = buildString {
        tokenize(plain, sorted).forEach { token -> append(token.reading ?: token.surface) }
    }.map { ch ->
        if (ch.code in 0x30A1..0x30F6) (ch.code - 0x60).toChar() else ch
    }.joinToString("")

    val output = StringBuilder()
    var i = 0
    while (i < kana.length) {
        val two = if (i + 1 < kana.length) "${kana[i]}${kana[i + 1]}" else null
        if (two != null && romajiMap.containsKey(two)) {
            output.append(romajiMap.getValue(two))
            i += 2
            continue
        }
        val ch = kana[i]
        if (ch == 'っ') {
            val nextTwo = if (i + 2 < kana.length) "${kana[i + 1]}${kana[i + 2]}" else null
            val nextOne = if (i + 1 < kana.length) kana[i + 1].toString() else ""
            val r = when {
                nextTwo != null && romajiMap.containsKey(nextTwo) -> romajiMap.getValue(nextTwo)
                romajiMap.containsKey(nextOne) -> romajiMap.getValue(nextOne)
                else -> ""
            }
            if (r.isNotEmpty()) output.append(r.first())
            i++
            continue
        }
        when {
            romajiMap.containsKey(ch.toString()) -> output.append(romajiMap.getValue(ch.toString()))
            ch == '。' -> output.append(". ")
            ch == '、' -> output.append(", ")
            ch == '「' || ch == '『' || ch == '（' -> output.append('"')
            ch == '」' || ch == '』' || ch == '）' -> output.append('"')
            ch == '・' || ch == '　' -> output.append(' ')
            ch == '〜' || ch == '～' -> output.append('~')
            ch == '！' -> output.append("! ")
            ch == '？' -> output.append("? ")
            else -> output.append(ch)
        }
        i++
    }
    return output.toString()
        .replace("niha", "niwa")
        .replace("deha", "dewa")
        .replace("toha", "towa")
        .replace("moha", "mowa")
        .replace("kaha", "kawa")
        .replace("nha", "nwa")
        .replace("iha", "iwa")
        .replace("eha", "ewa")
        .replace("uha", "uwa")
        .replace("haha", "\u0000HAHA\u0000")
        .replace("aha", "awa")
        .replace("\u0000HAHA\u0000", "haha")
        .replace("oha", "owa")
        .replace(Regex("([aiueon])he"), "$1e")
        .trim()
}
