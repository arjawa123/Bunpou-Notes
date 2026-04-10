package com.rjw.bunpoun3

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.core.view.WindowCompat
import com.rjw.bunpoun3.data.AppRepository
import com.rjw.bunpoun3.data.Catalog
import com.rjw.bunpoun3.data.CatalogDay
import com.rjw.bunpoun3.data.CatalogExample
import com.rjw.bunpoun3.data.CatalogGrammar
import com.rjw.bunpoun3.data.CatalogWeek
import com.rjw.bunpoun3.data.DayQuizProgress
import com.rjw.bunpoun3.ui.theme.BunpouTheme
import com.rjw.bunpoun3.ui.theme.ThemeMode
import com.rjw.bunpoun3.ui.theme.ThemePreset
import com.rjw.bunpoun3.ui.theme.appColorSchemeFor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: MainViewModel = viewModel(
                factory = object : ViewModelProvider.AndroidViewModelFactory(application) {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(application) as T
                    }
                },
            )
            val state by vm.uiState.collectAsStateWithLifecycle()
            BunpouTheme(
                themeMode = state.themeMode,
                preset = state.themePreset,
            ) {
                val view = LocalView.current
                val isDarkTheme = when (state.themeMode) {
                    ThemeMode.Light -> false
                    ThemeMode.Dark -> true
                    ThemeMode.System -> isSystemInDarkTheme()
                }
                SideEffect {
                    val window = view.context.findActivity().window
                    WindowCompat.getInsetsController(window, view).apply {
                        isAppearanceLightStatusBars = !isDarkTheme
                        isAppearanceLightNavigationBars = !isDarkTheme
                    }
                }
                AppRoot(state = state, vm = vm)
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> error("No Activity found for context")
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
            repository.observeQuizProgress().collect { progress ->
                _uiState.update { it.copy(quizProgressByDay = progress) }
            }
        }
        viewModelScope.launch {
            repository.observeSettings().collect { settings ->
                _uiState.update { state ->
                    state.copy(
                        accessGranted = settings[SETTING_ACCESS] == "true",
                        furiganaOn = settings[SETTING_FURIGANA] != "false",
                        romajiOn = settings[SETTING_ROMAJI] != "false",
                        themeMode = settings[SETTING_THEME_MODE]
                            ?.let { value -> ThemeMode.entries.firstOrNull { it.name == value } }
                            ?: ThemeMode.Light,
                        themePreset = settings[SETTING_THEME_PRESET]
                            ?.let { value -> ThemePreset.entries.firstOrNull { it.name == value } }
                            ?: ThemePreset.Classic,
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

    fun openThemeSettings() {
        _uiState.update { it.copy(settingsReturnScreen = it.screen, screen = Screen.Settings) }
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

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.setSetting(SETTING_THEME_MODE, mode.name) }
    }

    fun updateThemePreset(preset: ThemePreset) {
        viewModelScope.launch { repository.setSetting(SETTING_THEME_PRESET, preset.name) }
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
                quizReturnScreen = Screen.Lesson(day.dayId),
                quizState = QuizState(
                    title = "第${day.weekNum}週 ${day.dayNum}日目 クイズ",
                    questions = questions,
                    dayId = day.dayId,
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
                quizReturnScreen = when (val current = it.screen) {
                    Screen.Settings, Screen.Quiz -> Screen.Home
                    else -> current
                },
                quizState = QuizState(
                    title = "クイズ全体",
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
            if (quiz.dayId != null && quiz.questions.isNotEmpty()) {
                val percentage = quiz.score * 100 / quiz.questions.size
                viewModelScope.launch {
                    repository.recordDayQuizResult(
                        dayId = quiz.dayId,
                        percentage = percentage,
                        passed = percentage >= DAILY_QUIZ_PASS_PERCENTAGE,
                    )
                }
            }
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
        _uiState.update {
            it.copy(
                screen = it.quizReturnScreen,
                quizState = null,
            )
        }
    }

    fun handleBack() {
        when (val screen = uiState.value.screen) {
            Screen.Home -> Unit
            is Screen.Week -> goHome()
            is Screen.Lesson -> openWeek(screen.weekNum())
            Screen.Quiz -> backFromQuiz()
            Screen.Settings -> closeThemeSettings()
        }
    }

    private fun Screen.Lesson.weekNum(): Int =
        uiState.value.catalog?.weeks
            ?.firstOrNull { week -> week.days.any { it.dayId == dayId } }
            ?.weekNum
            ?: 1

    private fun closeThemeSettings() {
        _uiState.update {
            it.copy(
                screen = it.settingsReturnScreen,
            )
        }
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
        private const val SETTING_THEME_MODE = "theme_mode"
        private const val SETTING_THEME_PRESET = "theme_preset"
        private const val DAILY_QUIZ_PASS_PERCENTAGE = 80
    }
}

data class UiState(
    val isLoading: Boolean = true,
    val catalog: Catalog? = null,
    val completedDayIds: Set<Int> = emptySet(),
    val quizProgressByDay: Map<Int, DayQuizProgress> = emptyMap(),
    val accessGranted: Boolean = false,
    val furiganaOn: Boolean = true,
    val romajiOn: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.Light,
    val themePreset: ThemePreset = ThemePreset.Classic,
    val accessError: String? = null,
    val screen: Screen = Screen.Home,
    val quizReturnScreen: Screen = Screen.Home,
    val settingsReturnScreen: Screen = Screen.Home,
    val quizState: QuizState? = null,
)

sealed interface Screen {
    data object Home : Screen
    data class Week(val weekNum: Int) : Screen
    data class Lesson(val dayId: Int) : Screen
    data object Quiz : Screen
    data object Settings : Screen
}

data class QuizState(
    val title: String,
    val questions: List<QuizQuestion>,
    val dayId: Int? = null,
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
    BackHandler(
        enabled = state.accessGranted && state.screen != Screen.Home,
        onBack = vm::handleBack,
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                            MaterialTheme.colorScheme.background,
                        ),
                    ),
                ),
        ) {
            AppBackdrop()
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
                        Screen.Settings -> "Pengaturan"
                    }
                    val showBottomBar = state.screen != Screen.Settings
                    val density = LocalDensity.current
                    val statusBarInset = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
                    val navigationBarInset = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }
                    val contentTopPadding = statusBarInset + 66.dp
                    val contentBottomPadding = if (showBottomBar) navigationBarInset + 92.dp else navigationBarInset

                    Box(modifier = Modifier.fillMaxSize()) {
                        AnimatedContent(
                            targetState = state.screen,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = contentTopPadding, bottom = contentBottomPadding),
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(280)) + scaleIn(initialScale = 0.985f, animationSpec = tween(280)))
                                    .togetherWith(fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 0.985f, animationSpec = tween(180)))
                            },
                            label = "screen_transition",
                        ) { screen ->
                            when (screen) {
                                Screen.Home -> HomeScreen(
                                    weeks = catalog.weeks,
                                    completedDayIds = state.completedDayIds,
                                    onOpenWeek = vm::openWeek,
                                )
                                is Screen.Week -> WeekScreen(
                                    week = catalog.weeks.first { it.weekNum == screen.weekNum },
                                    completedDayIds = state.completedDayIds,
                                    quizProgressByDay = state.quizProgressByDay,
                                    onOpenLesson = vm::openLesson,
                                )
                                is Screen.Lesson -> {
                                    val day = catalog.weeks.flatMap { it.days }.first { it.dayId == screen.dayId }
                                    LessonScreen(
                                        day = day,
                                        completed = state.completedDayIds.contains(day.dayId),
                                        quizProgress = state.quizProgressByDay[day.dayId],
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
                                )
                                Screen.Settings -> ThemeSettingsScreen(
                                    furiganaOn = state.furiganaOn,
                                    romajiOn = state.romajiOn,
                                    themeMode = state.themeMode,
                                    themePreset = state.themePreset,
                                    onToggleFurigana = vm::toggleFurigana,
                                    onToggleRomaji = vm::toggleRomaji,
                                    onSelectMode = vm::updateThemeMode,
                                    onSelectPreset = vm::updateThemePreset,
                                )
                            }
                        }

                        AppTopBar(
                            title = title,
                            canGoBack = state.screen != Screen.Home,
                            onBack = vm::handleBack,
                            onOpenThemeSettings = vm::openThemeSettings,
                            showThemeAction = state.screen != Screen.Settings,
                        )

                        if (showBottomBar) {
                            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                                AppBottomBar(
                                    state = state,
                                    onHome = vm::goHome,
                                    onOpenLearn = vm::openWeek,
                                    onQuiz = vm::startAllQuiz,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ElevatedCard(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CircularProgressIndicator()
                Text("Menyiapkan materi belajar...", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun AccessScreen(error: String?, onSubmit: (String) -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                SummaryPill("Akses privat")
                Text("日本語N3 文法アプリ", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    "Masukkan kode akses untuk membuka materi belajar, kuis, dan progress mingguan.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
    onOpenThemeSettings: () -> Unit,
    showThemeAction: Boolean,
) {
    val topBase = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.background, 0.06f)
    val topAccent = lerp(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer, 0.34f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        topBase,
                        lerp(topBase, topAccent, 0.38f),
                        Color.Transparent,
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .padding(bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (showThemeAction) {
                        IconButton(onClick = onOpenThemeSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        }
    } 
}

@Composable
private fun completionContainerColor(strong: Boolean = false): Color =
    lerp(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        if (strong) 0.56f else 0.4f,
    )

@Composable
private fun completionContentColor(): Color =
    lerp(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, 0.32f)

private fun lessonStateText(
    completed: Boolean,
    quizProgress: DayQuizProgress?,
): String = when {
    completed && quizProgress?.passed == true -> "Lulus latihan ${quizProgress.bestPercentage}%"
    completed -> "Pelajaran selesai"
    quizProgress != null -> "Latihan ${quizProgress.attempts}x • terbaik ${quizProgress.bestPercentage}%"
    else -> "Sedang dipelajari"
}

@Composable
private fun LessonQuickActionDock(
    completed: Boolean,
    onToggleComplete: () -> Unit,
    onStartQuiz: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilledTonalIconButton(
            onClick = onToggleComplete,
            colors = androidx.compose.material3.IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = if (completed) {
                    completionContainerColor(strong = true)
                } else {
                    lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.6f)
                },
                contentColor = if (completed) {
                    completionContentColor()
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            ),
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = if (completed) "Batalkan selesai" else "Tandai selesai",
            )
        }
        FilledIconButton(onClick = onStartQuiz) {
            Icon(Icons.Default.Quiz, contentDescription = "Mulai kuis")
        }
    }
}

@Composable
private fun LessonStatusBadge(
    completed: Boolean,
    quizProgress: DayQuizProgress?,
) {
    val container = if (completed) {
        completionContainerColor(strong = true)
    } else {
        lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.secondaryContainer, 0.45f)
    }
    val content = if (completed) completionContentColor() else MaterialTheme.colorScheme.onSecondaryContainer
    val title = if (completed) "Pelajaran selesai" else "Sedang dipelajari"
    val subtitle = when {
        completed && quizProgress?.passed == true -> "Lulus mode latihan dengan skor terbaik ${quizProgress.bestPercentage}%."
        completed -> "Materi ini sudah ditandai selesai dan tersimpan offline."
        quizProgress != null -> "Sudah latihan ${quizProgress.attempts}x, skor terbaik ${quizProgress.bestPercentage}%."
        else -> "Materi ini masih aktif untuk sesi belajar."
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(container)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f), RoundedCornerShape(999.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.82f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = content,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                title,
                color = content,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                subtitle,
                color = content.copy(alpha = 0.82f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun LessonPagerButton(
    text: String,
    emphasized: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (emphasized) {
                MaterialTheme.colorScheme.primary
            } else {
                lerp(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.secondaryContainer, 0.3f)
            },
            contentColor = if (emphasized) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            },
        ),
    ) {
        Text(text)
    }
}

@Composable
private fun RowScope.FloatingBottomTab(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val iconTint = when {
        !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        selected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val labelTint = when {
        !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
        selected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(24.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(
                    if (selected) {
                        lerp(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer, 0.16f)
                    } else {
                        Color.Transparent
                    },
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint)
        }
        Text(text, color = labelTint, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun ThemeSettingsScreen(
    furiganaOn: Boolean,
    romajiOn: Boolean,
    themeMode: ThemeMode,
    themePreset: ThemePreset,
    onToggleFurigana: () -> Unit,
    onToggleRomaji: () -> Unit,
    onSelectMode: (ThemeMode) -> Unit,
    onSelectPreset: (ThemePreset) -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewDark = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    SummaryPill("Pengaturan aplikasi")
                    Text("Atur cara belajar dan tampilan app dari satu tempat.", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        "Kontrol furigana, romaji, mode light/dark, dan preset warna sekarang dikumpulkan di menu ini supaya layar belajar tetap fokus ke materi.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SummaryPill(if (furiganaOn) "Furigana aktif" else "Furigana off")
                        SummaryPill(if (romajiOn) "Romaji aktif" else "Romaji off")
                        SummaryPill("Mode: ${themeMode.label()}")
                        SummaryPill("Preset: ${themePreset.label()}")
                    }
                }
            }
        }
        item {
            SectionTitle("Preferensi belajar")
        }
        item {
            SettingsSectionCard {
                SettingsToggleRow(
                    icon = Icons.Default.TextFields,
                    title = "Furigana",
                    subtitle = "Tampilkan ruby kecil di atas kanji saat membaca materi.",
                    checked = furiganaOn,
                    onToggle = onToggleFurigana,
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                SettingsToggleRow(
                    icon = Icons.Default.Translate,
                    title = "Romaji",
                    subtitle = "Tampilkan transliterasi latin di bawah contoh kalimat.",
                    checked = romajiOn,
                    onToggle = onToggleRomaji,
                )
            }
        }
        item {
            SectionTitle("Tema aplikasi")
        }
        item {
            SettingsSectionCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Mode tampilan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Pilih light, dark, atau ikuti sistem perangkat.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ThemeMode.entries.forEach { mode ->
                            FilterChip(
                                selected = themeMode == mode,
                                onClick = { onSelectMode(mode) },
                                label = { Text(mode.label()) },
                            )
                        }
                    }
                }
            }
        }
        item { SectionTitle("Preset warna") }
        items(ThemePreset.entries.toList()) { preset ->
            ThemePresetCard(
                preset = preset,
                selected = themePreset == preset,
                previewDark = previewDark,
                onClick = { onSelectPreset(preset) },
            )
        }
    }
}

@Composable
private fun SettingsSectionCard(
    content: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content,
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(lerp(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.secondaryContainer, 0.36f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}

@Composable
private fun ThemePresetCard(
    preset: ThemePreset,
    selected: Boolean,
    previewDark: Boolean,
    onClick: () -> Unit,
) {
    val scheme = appColorSchemeFor(preset, dark = previewDark)
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (selected) {
                lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primaryContainer, 0.78f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(preset.label(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        preset.description(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                if (selected) {
                    SummaryPill("Aktif")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ThemeSwatch(scheme.primary)
                ThemeSwatch(scheme.secondary)
                ThemeSwatch(scheme.tertiary)
                ThemeSwatch(scheme.background)
                ThemeSwatch(scheme.surfaceVariant)
            }
        }
    }
}

@Composable
private fun ThemeSwatch(color: Color) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
    )
}

@Composable
private fun BlendedHeaderCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primaryContainer
    val secondary = MaterialTheme.colorScheme.secondaryContainer
    val background = MaterialTheme.colorScheme.background

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            primary,
                            lerp(primary, secondary, 0.42f),
                            lerp(secondary, background, 0.62f),
                        ),
                    ),
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                content = content,
            )
        }
    }
}

private fun ThemeMode.label(): String =
    when (this) {
        ThemeMode.Light -> "Light"
        ThemeMode.Dark -> "Dark"
        ThemeMode.System -> "System"
    }

private fun ThemePreset.label(): String =
    when (this) {
        ThemePreset.Classic -> "Classic HTML"
        ThemePreset.Ocean -> "Ocean"
        ThemePreset.Sakura -> "Sakura"
        ThemePreset.Forest -> "Forest"
        ThemePreset.Sunset -> "Sunset"
    }

private fun ThemePreset.description(): String =
    when (this) {
        ThemePreset.Classic -> "Cream, navy, dan light blue seperti versi HTML bawaan."
        ThemePreset.Ocean -> "Biru-teal bersih dan modern."
        ThemePreset.Sakura -> "Pink lembut dengan nuansa hangat."
        ThemePreset.Forest -> "Hijau tenang untuk sesi belajar panjang."
        ThemePreset.Sunset -> "Oranye hangat dengan aksen senja."
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
    val currentWeek = weeks.firstOrNull { week -> week.days.any { it.dayId !in completedDayIds } } ?: weeks.lastOrNull()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryPill("Belajar bertahap")
                    Text("日本語総まとめ N3 文法", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(
                        "Belajar tata bahasa Jepang N3 dengan alur mingguan, progress yang jelas, dan latihan kuis cepat.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    LinearProgressIndicator(
                        progress = { done.toFloat() / totalDays.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(999.dp)),
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SummaryPill("$done / $totalDays selesai")
                        currentWeek?.let { SummaryPill("Lanjut: Minggu ${it.weekNum}") }
                        SummaryPill("${totalDays - done} pelajaran tersisa")
                    }
                }
            }
        }
        item {
            SectionTitle("Mulai belajar")
        }
        items(weeks) { week ->
            val daysDone = week.days.count { completedDayIds.contains(it.dayId) }
            ElevatedCard(
                onClick = { onOpenWeek(week.weekNum) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("W${week.weekNum}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            StrongJapaneseTitleText(
                                text = week.title,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(week.titleId, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.NavigateNext,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    LinearProgressIndicator(
                        progress = { daysDone.toFloat() / week.days.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp)),
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SummaryPill("$daysDone/${week.days.size} selesai")
                        SummaryPill("${week.days.size - daysDone} tersisa")
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekScreen(
    week: CatalogWeek,
    completedDayIds: Set<Int>,
    quizProgressByDay: Map<Int, DayQuizProgress>,
    onOpenLesson: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryPill("Minggu ${week.weekNum}")
                    Text("第${week.weekNum}週", color = MaterialTheme.colorScheme.primary)
                    StrongJapaneseTitleText(
                        text = week.title,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(week.titleId, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    SummaryPill("${completedDayIds.count { id -> week.days.any { it.dayId == id } }}/${week.days.size} selesai")
                }
            }
        }
        item {
            SectionTitle("Daftar pelajaran")
        }
        items(week.days) { day ->
            val completed = completedDayIds.contains(day.dayId)
            val quizProgress = quizProgressByDay[day.dayId]
            ElevatedCard(
                onClick = { onOpenLesson(day.dayId) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (completed) completionContainerColor(strong = true) else MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            if (completed) "✓" else day.dayNum.toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (completed) completionContentColor() else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text("${day.dayNum}日目", color = MaterialTheme.colorScheme.primary)
                        Text(
                            day.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Text(
                            day.grammarPoints.joinToString("・") { it.pattern },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            lessonStateText(completed = completed, quizProgress = quizProgress),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (completed) completionContentColor() else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun LessonScreen(
    day: CatalogDay,
    completed: Boolean,
    quizProgress: DayQuizProgress?,
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
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        BlendedHeaderCard {
            Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        LessonStatusBadge(completed = completed, quizProgress = quizProgress)
                        Text("第${day.weekNum}週 › ${day.dayNum}日目", color = MaterialTheme.colorScheme.primary)
                        Text(day.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(day.titleId, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    LessonQuickActionDock(
                        completed = completed,
                        onToggleComplete = onToggleComplete,
                        onStartQuiz = onStartQuiz,
                    )
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SummaryPill("${day.grammarPoints.size} pola grammar")
                }
            }
        }
        SectionTitle("Materi pelajaran")
        day.grammarPoints.forEach { grammar ->
            GrammarCard(grammar, furiganaOn, romajiOn, readings, romajiMap)
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        if (hasPrev && hasNext) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    LessonPagerButton(text = "← ${day.dayNum - 1}日目", emphasized = false, onClick = onPrev)
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    LessonPagerButton(text = "${day.dayNum + 1}日目 →", emphasized = true, onClick = onNext)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    hasPrev -> LessonPagerButton(text = "← ${day.dayNum - 1}日目", emphasized = false, onClick = onPrev)
                    hasNext -> LessonPagerButton(text = "${day.dayNum + 1}日目 →", emphasized = true, onClick = onNext)
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
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(grammar.pattern, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                AssistChip(onClick = {}, label = { Text(grammar.badge) })
            }
            Text(grammar.meaning, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(22.dp),
            ) {
                Text(
                    grammar.meaningId,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            grammar.verbForm?.let { verbForm ->
                FilledTonalButton(onClick = { showVerbForm = !showVerbForm }) {
                    Text(if (showVerbForm) "Tutup cara pembentukan" else "Lihat cara pembentukan")
                }
                AnimatedVisibility(
                    visible = showVerbForm,
                    enter = expandVertically(animationSpec = tween(240)) + fadeIn(animationSpec = tween(240)),
                    exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(180)),
                ) {
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
            SectionTitle("Contoh kalimat / 例文")
            grammar.examples.forEach { example ->
                ExampleCard(example, furiganaOn, romajiOn, readings, romajiMap, false)
            }
            if (grammar.extraExamples.isNotEmpty()) {
                OutlinedButton(onClick = { showExtras = !showExtras }) {
                    Text(if (showExtras) "Sembunyikan contoh tambahan" else "Lihat ${grammar.extraExamples.size} contoh tambahan")
                }
                AnimatedVisibility(
                    visible = showExtras,
                    enter = expandVertically(animationSpec = tween(240)) + fadeIn(animationSpec = tween(240)),
                    exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(180)),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        grammar.extraExamples.forEach { example ->
                            ExampleCard(example, furiganaOn, romajiOn, readings, romajiMap, true)
                        }
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isExtra) {
                lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.tertiaryContainer, 0.5f)
            } else {
                lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.58f)
            },
        ),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            RubyText(example.japaneseHtml, furiganaOn, readings)
            if (romajiOn) {
                Text(
                    toRomaji(example.japaneseHtml, readings, romajiMap),
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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
                .padding(20.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(result.third, fontSize = 56.sp)
                    Text("${quizState.score}/${quizState.questions.size}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text(result.first, textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium)
                    Text(result.second, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (quizState.dayId != null) {
                        Text(
                            if (percentage >= 80) {
                                "Lulus mode latihan. Pelajaran otomatis ditandai selesai."
                            } else {
                                "Belum lulus mode latihan. Progress latihan tetap tersimpan offline."
                            },
                            textAlign = TextAlign.Center,
                            color = if (percentage >= 80) completionContentColor() else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    LinearProgressIndicator(
                        progress = { percentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp)),
                    )
                    Button(onClick = onReplay, modifier = Modifier.fillMaxWidth()) { Text("もう一度 / Ulangi") }
                    OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) { Text("ホームへ / Beranda") }
                }
            }
        }
        return
    }

    val question = quizState.currentQuestion ?: return
    val answered = quizState.selectedAnswer != null
    val isCorrect = quizState.selectedAnswer == question.correctPattern
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryPill("Mode latihan")
                    Text(quizState.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SummaryPill("Soal ${quizState.index + 1}/${quizState.questions.size}")
                        SummaryPill("Skor ${quizState.score}")
                    }
                    LinearProgressIndicator(
                        progress = { quizState.index.toFloat() / quizState.questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp)),
                    )
                }
            }
        }
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Pilih pola grammar yang paling tepat", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    RubyText(question.promptHtml, furiganaOn, readings)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.secondaryContainer, 0.5f),
                        ),
                        shape = RoundedCornerShape(22.dp),
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(question.promptId, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            Text(
                                toRomaji(question.promptHtml, readings, romajiMap),
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
        items(question.options.withIndex().toList()) { indexed ->
            val option = indexed.value
            val selected = quizState.selectedAnswer == option
            val correct = option == question.correctPattern
            val colors = when {
                !answered -> CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.24f),
                )
                correct -> CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.tertiaryContainer, 0.72f),
                )
                selected -> CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.errorContainer, 0.68f),
                )
                else -> CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.16f),
                )
            }
            ElevatedCard(
                onClick = { onAnswer(option) },
                enabled = !answered,
                modifier = Modifier.fillMaxWidth(),
                colors = colors,
                shape = RoundedCornerShape(22.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.72f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(('A' + indexed.index).toString(), fontWeight = FontWeight.Bold)
                    }
                    Text(option, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                }
            }
        }
        if (answered) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) {
                            lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.tertiaryContainer, 0.65f)
                        } else {
                            lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.errorContainer, 0.62f)
                        },
                    ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(if (isCorrect) "正解！ Benar! ✓" else "不正解 Salah. ✗", fontWeight = FontWeight.Bold)
                        if (!isCorrect) Text("正解: ${question.correctPattern}", fontWeight = FontWeight.SemiBold)
                        Text(question.meaningId)
                    }
                }
            }
            item {
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                ) {
                    Text(if (quizState.index + 1 < quizState.questions.size) "次の問題 →" else "結果を見る 🎉")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun SummaryPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.background, 0.12f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StrongJapaneseTitleText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        modifier = modifier.drawWithContent {
            drawContent()
            translate(left = 0.65f) { this@drawWithContent.drawContent() }
        },
        style = style.copy(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Black,
        ),
        color = color,
        maxLines = maxLines,
        overflow = overflow,
    )
}

private enum class AppTab {
    Home,
    Learn,
    Quiz,
}

@Composable
private fun AppBackdrop() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 72.dp)
                .size(180.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.045f)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 144.dp)
                .size(128.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 84.dp)
                .size(220.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.03f)),
        )
    }
}

@Composable
private fun AppBottomBar(
    state: UiState,
    onHome: () -> Unit,
    onOpenLearn: (Int) -> Unit,
    onQuiz: () -> Unit,
) {
    val activeTab = state.activeTab()
    val learnWeek = state.recommendedWeekNum()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 10.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FloatingBottomTab(
                    selected = activeTab == AppTab.Home,
                    onClick = {
                        if (activeTab != AppTab.Home) onHome()
                    },
                    icon = Icons.Default.Home,
                    text = "Beranda",
                )
                FloatingBottomTab(
                    selected = activeTab == AppTab.Learn,
                    onClick = {
                        if (activeTab != AppTab.Learn) {
                            learnWeek?.let(onOpenLearn)
                        }
                    },
                    enabled = learnWeek != null,
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    text = "Belajar",
                )
                FloatingBottomTab(
                    selected = activeTab == AppTab.Quiz,
                    onClick = {
                        if (activeTab != AppTab.Quiz) onQuiz()
                    },
                    icon = Icons.Default.Quiz,
                    text = "Quiz",
                )
            }
        }
    }
}

private fun UiState.activeTab(): AppTab =
    when (screen) {
        Screen.Home -> AppTab.Home
        is Screen.Week, is Screen.Lesson -> AppTab.Learn
        Screen.Quiz -> AppTab.Quiz
        Screen.Settings -> when (settingsReturnScreen) {
            Screen.Home -> AppTab.Home
            is Screen.Week, is Screen.Lesson -> AppTab.Learn
            Screen.Quiz -> AppTab.Quiz
            Screen.Settings -> AppTab.Home
        }
    }

private fun UiState.recommendedWeekNum(): Int? {
    val weeks = catalog?.weeks ?: return null
    return when (val current = screen) {
        is Screen.Week -> current.weekNum
        is Screen.Lesson -> weeks.firstOrNull { week -> week.days.any { it.dayId == current.dayId } }?.weekNum
        else -> weeks.firstOrNull { week -> week.days.any { it.dayId !in completedDayIds } }?.weekNum
            ?: weeks.firstOrNull()?.weekNum
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
    val tokens = remember(segments, sorted) { tokenizeRubySegments(segments, sorted) }

    RubyWrapLayout(modifier = Modifier.fillMaxWidth()) {
        tokens.forEach { token ->
            RubyTokenView(
                token = token,
                showFurigana = showFurigana,
            )
        }
    }
}

private data class StrongSegment(val text: String, val bold: Boolean)
private data class RubyToken(val surface: String, val reading: String?, val bold: Boolean)
private data class RubyLine(val items: List<Pair<Placeable, Int>>, val width: Int, val height: Int)

private enum class ScriptCategory {
    Space,
    Punctuation,
    Kanji,
    Kana,
    Latin,
    Digit,
    Other,
}

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

@Composable
private fun RubyWrapLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val verticalGap = with(LocalDensity.current) { 4.dp.roundToPx() }

    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val relaxedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(relaxedConstraints) }
        val maxWidth = if (constraints.maxWidth == Constraints.Infinity) Int.MAX_VALUE else constraints.maxWidth
        val rows = mutableListOf<RubyLine>()
        var rowItems = mutableListOf<Pair<Placeable, Int>>()
        var rowWidth = 0
        var rowHeight = 0

        fun commitRow() {
            if (rowItems.isNotEmpty()) {
                rows += RubyLine(rowItems.toList(), rowWidth, rowHeight)
                rowItems = mutableListOf()
                rowWidth = 0
                rowHeight = 0
            }
        }

        placeables.forEach { placeable ->
            val projectedWidth = if (rowItems.isEmpty()) placeable.width else rowWidth + placeable.width
            if (rowItems.isNotEmpty() && projectedWidth > maxWidth) {
                commitRow()
            }
            rowItems += placeable to rowWidth
            rowWidth += placeable.width
            rowHeight = max(rowHeight, placeable.height)
        }
        commitRow()

        val width = when {
            constraints.maxWidth != Constraints.Infinity -> constraints.maxWidth
            rows.isEmpty() -> 0
            else -> rows.maxOf { it.width }
        }.coerceAtLeast(constraints.minWidth)

        val contentHeight = rows.sumOf { it.height } + (rows.size - 1).coerceAtLeast(0) * verticalGap
        val height = if (constraints.maxHeight == Constraints.Infinity) {
            contentHeight.coerceAtLeast(constraints.minHeight)
        } else {
            contentHeight.coerceIn(constraints.minHeight, constraints.maxHeight)
        }

        layout(width, height) {
            var y = 0
            rows.forEach { row ->
                row.items.forEach { (placeable, x) ->
                    placeable.placeRelative(x, y + row.height - placeable.height)
                }
                y += row.height + verticalGap
            }
        }
    }
}

@Composable
private fun RubyTokenView(
    token: RubyToken,
    showFurigana: Boolean,
) {
    val weight = if (token.bold) FontWeight.Bold else FontWeight.Normal
    if (!showFurigana || token.reading == null || !token.surface.any(::isKanji)) {
        Text(
            token.surface,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = weight,
        )
        return
    }

    val furiganaGap = with(LocalDensity.current) { 1.dp.roundToPx() }
    val sidePadding = with(LocalDensity.current) { 2.dp.roundToPx() }

    Layout(
        content = {
            Text(
                token.reading,
                fontSize = 9.sp,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                token.surface,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = weight,
            )
        },
    ) { measurables, constraints ->
        val relaxed = constraints.copy(minWidth = 0, minHeight = 0)
        val reading = measurables[0].measure(relaxed)
        val surface = measurables[1].measure(relaxed)

        val compactReadingWidth = (reading.width * 0.88f).toInt()
        val width = max(
            surface.width + sidePadding * 2,
            compactReadingWidth,
        ).coerceAtMost(max(reading.width, surface.width + sidePadding * 3))
        val height = reading.height + furiganaGap + surface.height

        layout(width, height) {
            reading.placeRelative((width - reading.width) / 2, 0)
            surface.placeRelative((width - surface.width) / 2, reading.height + furiganaGap)
        }
    }
}

private fun tokenizeRubySegments(
    segments: List<StrongSegment>,
    sortedReadings: List<Map.Entry<String, String>>,
): List<RubyToken> = buildList {
    segments.forEach { segment ->
        addAll(tokenizeSegment(segment.text, segment.bold, sortedReadings))
    }
}

private fun tokenizeSegment(
    text: String,
    bold: Boolean,
    sortedReadings: List<Map.Entry<String, String>>,
): List<RubyToken> {
    val tokens = mutableListOf<RubyToken>()
    var index = 0
    while (index < text.length) {
        val matched = sortedReadings.firstOrNull { (word, _) -> text.startsWith(word, index) }
        if (matched != null) {
            tokens += RubyToken(matched.key, matched.value, bold)
            index += matched.key.length
            continue
        }

        val start = index
        val group = scriptCategory(text[index])
        if (group == ScriptCategory.Space || group == ScriptCategory.Punctuation) {
            tokens += RubyToken(text[index].toString(), null, bold)
            index++
            continue
        }

        index++
        while (index < text.length) {
            if (sortedReadings.any { (word, _) -> text.startsWith(word, index) }) break
            val nextGroup = scriptCategory(text[index])
            if (nextGroup != group || nextGroup == ScriptCategory.Space || nextGroup == ScriptCategory.Punctuation) break
            index++
        }
        tokens += RubyToken(text.substring(start, index), null, bold)
    }
    return tokens
}

private fun scriptCategory(ch: Char): ScriptCategory =
    when {
        ch.isWhitespace() -> ScriptCategory.Space
        isJapanesePunctuation(ch) -> ScriptCategory.Punctuation
        isKanji(ch) -> ScriptCategory.Kanji
        isKana(ch) -> ScriptCategory.Kana
        ch.isDigit() -> ScriptCategory.Digit
        ch.isLetter() -> ScriptCategory.Latin
        else -> ScriptCategory.Other
    }

private fun isKana(ch: Char): Boolean =
    ch.code in 0x3040..0x309F || ch.code in 0x30A0..0x30FF || ch == 'ー'

private fun isJapanesePunctuation(ch: Char): Boolean =
    ch in "。、・！？「」『』（）()[]【】〈〉《》.,!?;:~〜-"

private fun isKanji(ch: Char): Boolean = ch.code in 0x4E00..0x9FFF

private fun toRomaji(
    html: String,
    readings: Map<String, String>,
    romajiMap: Map<String, String>,
): String {
    val plain = stripTags(html)
    val sorted = readings.entries.sortedByDescending { it.key.length }
    val output = StringBuilder()

    tokenizeSegment(plain, bold = false, sortedReadings = sorted).forEach { token ->
        val source = (token.reading ?: token.surface).toHiragana()
        when {
            source.isBlank() -> {
                if (output.isNotEmpty() && output.last() != ' ') output.append(' ')
            }
            source.length == 1 && isJapanesePunctuation(source.first()) -> {
                appendRomajiPunctuation(output, source.first())
            }
            else -> {
                val romaji = kanaToRomaji(source, romajiMap)
                if (romaji.isNotBlank()) {
                    if (output.isNotEmpty() && output.last() !in " \"'([{/~") output.append(' ')
                    output.append(romaji)
                }
            }
        }
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
        .replace(Regex("\\s+([,.!?])"), "$1")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun String.toHiragana(): String =
    map { ch -> if (ch.code in 0x30A1..0x30F6) (ch.code - 0x60).toChar() else ch }.joinToString("")

private fun kanaToRomaji(
    kana: String,
    romajiMap: Map<String, String>,
): String {
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
            val nextRomaji = when {
                nextTwo != null && romajiMap.containsKey(nextTwo) -> romajiMap.getValue(nextTwo)
                romajiMap.containsKey(nextOne) -> romajiMap.getValue(nextOne)
                else -> ""
            }
            if (nextRomaji.isNotEmpty()) output.append(nextRomaji.first())
            i++
            continue
        }
        when {
            romajiMap.containsKey(ch.toString()) -> output.append(romajiMap.getValue(ch.toString()))
            ch == 'ー' && output.isNotEmpty() -> output.append(output.last())
            else -> output.append(ch)
        }
        i++
    }
    return output.toString()
}

private fun appendRomajiPunctuation(
    output: StringBuilder,
    punctuation: Char,
) {
    when (punctuation) {
        '。' -> {
            output.trimEndSpaces()
            output.append(". ")
        }
        '、', '・' -> {
            output.trimEndSpaces()
            output.append(", ")
        }
        '！' -> {
            output.trimEndSpaces()
            output.append("! ")
        }
        '？' -> {
            output.trimEndSpaces()
            output.append("? ")
        }
        '「', '『', '（' -> {
            if (output.isNotEmpty() && output.last() != ' ') output.append(' ')
            output.append('"')
        }
        '」', '』', '）' -> {
            output.trimEndSpaces()
            output.append("\" ")
        }
        '〜', '～', '~' -> output.append("~")
        '-', '−' -> output.append("-")
        else -> {
            if (output.isNotEmpty() && output.last() != ' ') output.append(' ')
        }
    }
}

private fun StringBuilder.trimEndSpaces() {
    while (isNotEmpty() && last() == ' ') {
        deleteCharAt(length - 1)
    }
}
