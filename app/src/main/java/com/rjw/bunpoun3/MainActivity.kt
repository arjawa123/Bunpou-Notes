package com.rjw.bunpoun3

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import androidx.credentials.CustomCredential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.rjw.bunpoun3.data.AppRepository
import com.rjw.bunpoun3.data.AuthSession
import com.rjw.bunpoun3.data.Catalog
import com.rjw.bunpoun3.data.CatalogDay
import com.rjw.bunpoun3.data.CatalogExample
import com.rjw.bunpoun3.data.CatalogGrammar
import com.rjw.bunpoun3.data.CatalogVerbForm
import com.rjw.bunpoun3.data.CatalogVerbFormRow
import com.rjw.bunpoun3.data.CatalogWeek
import com.rjw.bunpoun3.data.DayQuizProgress
import com.rjw.bunpoun3.data.SupabaseAuthRepository
import com.rjw.bunpoun3.ui.theme.BunpouTheme
import com.rjw.bunpoun3.ui.theme.ThemeMode
import com.rjw.bunpoun3.ui.theme.ThemePreset
import com.rjw.bunpoun3.ui.theme.appColorSchemeFor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.math.max
import kotlin.random.Random

private val JapaneseTitleFontFamily = FontFamily(
    Font(R.font.noto_sans_cjk_black_subset, weight = FontWeight.Black),
)

private val ExampleItalicFontFamily = FontFamily(
    Font(R.font.open_sans_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
)

private val BottomNavContentPadding = 124.dp

class MainActivity : ComponentActivity() {
    private var authCallbackUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authCallbackUri = intent?.data
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
                LaunchedEffect(authCallbackUri) {
                    authCallbackUri?.let { uri ->
                        vm.handleAuthCallback(uri)
                        authCallbackUri = null
                    }
                }
                AppRoot(state = state, vm = vm)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        authCallbackUri = intent.data
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
    private val authRepository = SupabaseAuthRepository(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        anonKey = BuildConfig.SUPABASE_ANON_KEY,
    )
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
                val authSession = settings.toAuthSession()
                if (authSession != null && authSession.email.isBlank()) {
                    viewModelScope.launch {
                        runCatching { authRepository.enrichSession(authSession) }
                            .onSuccess { session -> saveAuthSession(session) }
                    }
                }
                _uiState.update { state ->
                    state.copy(
                        accessGranted = authSession != null,
                        authSession = authSession,
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

    fun openProfile() {
        _uiState.update { it.copy(profileReturnScreen = it.screen, screen = Screen.Profile) }
    }

    fun showLogin() {
        _uiState.update {
            it.copy(
                authMode = AuthMode.Login,
                authError = null,
                authNotice = null,
            )
        }
    }

    fun showRegister() {
        _uiState.update {
            it.copy(
                authMode = AuthMode.Register,
                authError = null,
                authNotice = null,
            )
        }
    }

    fun signIn(email: String, password: String) {
        runAuthAction {
            val session = authRepository.signIn(email, password)
            saveAuthSession(session)
            _uiState.update {
                it.copy(
                    screen = Screen.Home,
                    authMode = AuthMode.Login,
                    authError = null,
                    authNotice = "Login berhasil. Selamat belajar lagi.",
                )
            }
        }
    }

    fun register(email: String, password: String) {
        runAuthAction {
            val session = authRepository.signUp(email, password)
            if (session != null) {
                saveAuthSession(session)
                _uiState.update {
                    it.copy(
                        screen = Screen.Home,
                        authMode = AuthMode.Login,
                        authError = null,
                        authNotice = "Akun dibuat dan sudah masuk.",
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        authMode = AuthMode.Login,
                        authError = null,
                        authNotice = "Registrasi berhasil. Cek email kamu untuk konfirmasi, lalu login.",
                    )
                }
            }
        }
    }

    fun requestPasswordReset(email: String) {
        runAuthAction {
            authRepository.sendPasswordReset(email)
            _uiState.update {
                it.copy(
                    authMode = AuthMode.Login,
                    authError = null,
                    authNotice = "Link reset password sudah dikirim. Buka dari email agar kembali ke app.",
                )
            }
        }
    }

    fun googleWebClientId(): String =
        BuildConfig.GOOGLE_WEB_CLIENT_ID

    fun signInWithGoogleIdToken(
        idToken: String,
        nonce: String,
    ) {
        runAuthAction {
            val session = authRepository.signInWithGoogleIdToken(idToken, nonce)
            val profileSession = runCatching { authRepository.enrichSession(session) }.getOrElse { session }
            saveAuthSession(profileSession)
            _uiState.update {
                it.copy(
                    screen = Screen.Home,
                    authMode = AuthMode.Login,
                    authError = null,
                    authNotice = "Login Google berhasil. Selamat belajar lagi.",
                )
            }
        }
    }

    fun showAuthError(message: String) {
        _uiState.update { it.copy(authError = message, authNotice = null) }
    }

    fun updatePassword(password: String) {
        val session = uiState.value.authSession
        if (session == null) {
            _uiState.update { it.copy(authError = "Session reset tidak ditemukan. Buka ulang link dari email.") }
            return
        }
        runAuthAction {
            authRepository.updatePassword(session.accessToken, password)
            _uiState.update {
                it.copy(
                    screen = Screen.Profile,
                    authError = null,
                    authNotice = "Password berhasil diperbarui.",
                )
            }
        }
    }

    fun handleAuthCallback(uri: Uri) {
        val params = uri.authCallbackParams()
        val error = params["error_description"] ?: params["error"] ?: params["message"]
        if (!error.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    authMode = AuthMode.Login,
                    authError = error,
                    authNotice = null,
                )
            }
            return
        }

        val session = params.toAuthSessionFromCallback()
        if (session == null) {
            _uiState.update {
                it.copy(
                    authMode = AuthMode.Login,
                    authNotice = "Email berhasil dikonfirmasi. Silakan login untuk melanjutkan.",
                )
            }
            return
        }

        viewModelScope.launch {
            val profileSession = runCatching { authRepository.enrichSession(session) }.getOrElse { session }
            saveAuthSession(profileSession)
            _uiState.update {
                it.copy(
                    screen = if (params["type"] == "recovery") Screen.ResetPassword else Screen.Home,
                    authMode = AuthMode.Login,
                    authError = null,
                    authNotice = if (params["type"] == "recovery") {
                        "Masukkan password baru untuk menyelesaikan reset."
                    } else {
                        "Email berhasil dikonfirmasi. Kamu sudah masuk."
                    },
                )
            }
        }
    }

    fun signOut() {
        _uiState.update { it.copy(authLoading = true, authError = null, authNotice = null) }
        viewModelScope.launch {
            uiState.value.authSession?.accessToken?.takeIf { it.isNotBlank() }?.let { token ->
                if (authRepository.isConfigured) {
                    runCatching { authRepository.signOut(token) }
                }
            }
            clearAuthSession()
            _uiState.update {
                it.copy(
                    screen = Screen.Home,
                    authMode = AuthMode.Login,
                    authError = null,
                    authNotice = "Kamu sudah logout.",
                )
            }
            _uiState.update { it.copy(authLoading = false) }
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
                    Screen.Settings, Screen.Quiz, Screen.Profile, Screen.ResetPassword -> Screen.Home
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
            Screen.Profile -> closeProfile()
            Screen.ResetPassword -> goHome()
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

    private fun closeProfile() {
        _uiState.update {
            it.copy(
                screen = it.profileReturnScreen,
            )
        }
    }

    private fun runAuthAction(action: suspend () -> Unit) {
        if (!authRepository.isConfigured) {
            _uiState.update {
                it.copy(
                    authLoading = false,
                    authError = "SUPABASE_URL dan SUPABASE_ANON_KEY belum terbaca dari local.properties.",
                )
            }
            return
        }
        _uiState.update { it.copy(authLoading = true, authError = null, authNotice = null) }
        viewModelScope.launch {
            runCatching { action() }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            authError = throwable.message ?: "Autentikasi gagal. Coba lagi.",
                        )
                    }
                }
            _uiState.update { it.copy(authLoading = false) }
        }
    }

    private suspend fun saveAuthSession(session: AuthSession) {
        repository.setSetting(SETTING_AUTH_ACCESS_TOKEN, session.accessToken)
        repository.setSetting(SETTING_AUTH_REFRESH_TOKEN, session.refreshToken)
        repository.setSetting(SETTING_AUTH_EXPIRES_AT, session.expiresAtMillis.toString())
        repository.setSetting(SETTING_AUTH_USER_ID, session.userId)
        repository.setSetting(SETTING_AUTH_EMAIL, session.email)
    }

    private suspend fun clearAuthSession() {
        repository.setSetting(SETTING_AUTH_ACCESS_TOKEN, "")
        repository.setSetting(SETTING_AUTH_REFRESH_TOKEN, "")
        repository.setSetting(SETTING_AUTH_EXPIRES_AT, "")
        repository.setSetting(SETTING_AUTH_USER_ID, "")
        repository.setSetting(SETTING_AUTH_EMAIL, "")
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
        private const val SETTING_FURIGANA = "furigana_on"
        private const val SETTING_ROMAJI = "romaji_on"
        private const val SETTING_THEME_MODE = "theme_mode"
        private const val SETTING_THEME_PRESET = "theme_preset"
        private const val SETTING_AUTH_ACCESS_TOKEN = "auth_access_token"
        private const val SETTING_AUTH_REFRESH_TOKEN = "auth_refresh_token"
        private const val SETTING_AUTH_EXPIRES_AT = "auth_expires_at"
        private const val SETTING_AUTH_USER_ID = "auth_user_id"
        private const val SETTING_AUTH_EMAIL = "auth_email"
        private const val DAILY_QUIZ_PASS_PERCENTAGE = 80
    }
}

private fun Map<String, String>.toAuthSession(): AuthSession? {
    val token = this["auth_access_token"]?.takeIf { it.isNotBlank() } ?: return null
    return AuthSession(
        accessToken = token,
        refreshToken = this["auth_refresh_token"].orEmpty(),
        expiresAtMillis = this["auth_expires_at"]?.toLongOrNull() ?: 0L,
        userId = this["auth_user_id"].orEmpty(),
        email = this["auth_email"].orEmpty(),
    )
}

private fun Map<String, String>.toAuthSessionFromCallback(): AuthSession? {
    val token = this["access_token"]?.takeIf { it.isNotBlank() } ?: return null
    return AuthSession(
        accessToken = token,
        refreshToken = this["refresh_token"].orEmpty(),
        expiresAtMillis = System.currentTimeMillis() + ((this["expires_in"]?.toLongOrNull() ?: 3_600L) * 1_000L),
        userId = this["user_id"].orEmpty(),
        email = this["email"].orEmpty(),
    )
}

private fun Uri.authCallbackParams(): Map<String, String> =
    buildMap {
        queryParameterNames.forEach { key ->
            getQueryParameter(key)?.let { value -> put(key, value) }
        }
        encodedFragment
            ?.split("&")
            .orEmpty()
            .mapNotNull { pair ->
                val parts = pair.split("=", limit = 2)
                if (parts.size == 2) Uri.decode(parts[0]) to Uri.decode(parts[1]) else null
            }
            .forEach { (key, value) -> put(key, value) }
    }

data class UiState(
    val isLoading: Boolean = true,
    val catalog: Catalog? = null,
    val completedDayIds: Set<Int> = emptySet(),
    val quizProgressByDay: Map<Int, DayQuizProgress> = emptyMap(),
    val accessGranted: Boolean = false,
    val authSession: AuthSession? = null,
    val authMode: AuthMode = AuthMode.Login,
    val authLoading: Boolean = false,
    val authError: String? = null,
    val authNotice: String? = null,
    val furiganaOn: Boolean = true,
    val romajiOn: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.Light,
    val themePreset: ThemePreset = ThemePreset.Classic,
    val screen: Screen = Screen.Home,
    val quizReturnScreen: Screen = Screen.Home,
    val settingsReturnScreen: Screen = Screen.Home,
    val profileReturnScreen: Screen = Screen.Settings,
    val quizState: QuizState? = null,
)

enum class AuthMode {
    Login,
    Register,
}

sealed interface Screen {
    data object Home : Screen
    data class Week(val weekNum: Int) : Screen
    data class Lesson(val dayId: Int) : Screen
    data object Quiz : Screen
    data object Settings : Screen
    data object Profile : Screen
    data object ResetPassword : Screen
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
    BackHandler(
        enabled = !state.accessGranted && state.authMode == AuthMode.Register,
        onBack = vm::showLogin,
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
                !state.accessGranted -> AuthGatewayScreen(
                    mode = state.authMode,
                    loading = state.authLoading,
                    error = state.authError,
                    notice = state.authNotice,
                    onLogin = vm::signIn,
                    onRegister = vm::register,
                    onRequestPasswordReset = vm::requestPasswordReset,
                    googleWebClientId = vm.googleWebClientId(),
                    onGoogleSignIn = vm::signInWithGoogleIdToken,
                    onGoogleSignInError = vm::showAuthError,
                    onShowLogin = vm::showLogin,
                    onShowRegister = vm::showRegister,
                )
                else -> {
                    val title = when (val screen = state.screen) {
                        Screen.Home -> "日本語N3 文法"
                        is Screen.Week -> "第${screen.weekNum}週"
                        is Screen.Lesson -> {
                            catalog.weeks.flatMap { it.days }.firstOrNull { it.dayId == screen.dayId }?.title ?: "Lesson"
                        }
                        Screen.Quiz -> state.quizState?.title ?: "Quiz"
                        Screen.Settings -> "Pengaturan"
                        Screen.Profile -> "Profil"
                        Screen.ResetPassword -> "Reset Password"
                    }
                    val showBottomBar = state.screen != Screen.Settings &&
                        state.screen != Screen.Profile &&
                        state.screen != Screen.ResetPassword
                    val density = LocalDensity.current
                    val statusBarInset = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
                    val navigationBarInset = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }
                    val contentTopPadding = statusBarInset + 66.dp
                    val contentBottomPadding = navigationBarInset

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
                                    onOpenLesson = vm::openLesson,
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
                                    romajiOn = state.romajiOn,
                                    readings = catalog.readings,
                                    romajiMap = catalog.romajiMap,
                                    onAnswer = vm::answerQuiz,
                                    onNext = vm::nextQuizQuestion,
                                    onReplay = vm::replayQuiz,
                                    onHome = vm::goHome,
                                )
                                Screen.Settings -> ThemeSettingsScreen(
                                    authSession = state.authSession,
                                    furiganaOn = state.furiganaOn,
                                    romajiOn = state.romajiOn,
                                    themeMode = state.themeMode,
                                    themePreset = state.themePreset,
                                    onOpenProfile = vm::openProfile,
                                    onToggleFurigana = vm::toggleFurigana,
                                    onToggleRomaji = vm::toggleRomaji,
                                    onSelectMode = vm::updateThemeMode,
                                    onSelectPreset = vm::updateThemePreset,
                                )
                                Screen.Profile -> ProfileScreen(
                                    session = state.authSession,
                                    loading = state.authLoading,
                                    error = state.authError,
                                    onLogout = vm::signOut,
                                )
                                Screen.ResetPassword -> ResetPasswordScreen(
                                    loading = state.authLoading,
                                    error = state.authError,
                                    notice = state.authNotice,
                                    onUpdatePassword = vm::updatePassword,
                                )
                            }
                        }

                        AppTopBar(
                            title = title,
                            canGoBack = state.screen != Screen.Home,
                            onBack = vm::handleBack,
                            onOpenThemeSettings = vm::openThemeSettings,
                            showThemeAction = state.screen != Screen.Settings &&
                                state.screen != Screen.Profile &&
                                state.screen != Screen.ResetPassword,
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
private fun AuthGatewayScreen(
    mode: AuthMode,
    loading: Boolean,
    error: String?,
    notice: String?,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String) -> Unit,
    onRequestPasswordReset: (String) -> Unit,
    googleWebClientId: String,
    onGoogleSignIn: (String, String) -> Unit,
    onGoogleSignInError: (String) -> Unit,
    onShowLogin: () -> Unit,
    onShowRegister: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        when (mode) {
            AuthMode.Login -> LoginScreen(
                loading = loading,
                error = error,
                notice = notice,
                onLogin = onLogin,
                onRequestPasswordReset = onRequestPasswordReset,
                googleWebClientId = googleWebClientId,
                onGoogleSignIn = onGoogleSignIn,
                onGoogleSignInError = onGoogleSignInError,
                onShowRegister = onShowRegister,
            )
            AuthMode.Register -> RegisterScreen(
                loading = loading,
                error = error,
                notice = notice,
                onRegister = onRegister,
                googleWebClientId = googleWebClientId,
                onGoogleSignIn = onGoogleSignIn,
                onGoogleSignInError = onGoogleSignInError,
                onShowLogin = onShowLogin,
            )
        }
    }
}

@Composable
private fun LoginScreen(
    loading: Boolean,
    error: String?,
    notice: String?,
    onLogin: (String, String) -> Unit,
    onRequestPasswordReset: (String) -> Unit,
    googleWebClientId: String,
    onGoogleSignIn: (String, String) -> Unit,
    onGoogleSignInError: (String) -> Unit,
    onShowRegister: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    AuthScaffoldCard(
        eyebrow = "Supabase Auth",
        title = "Masuk ke Bunpou Notes",
        description = "Login untuk membuka materi, menyimpan session offline, dan nanti siap disinkronkan ke Supabase.",
        error = error,
        notice = notice,
    ) {
        AuthFields(
            email = email,
            password = password,
            enabled = !loading,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
        )
        Button(
            onClick = { onLogin(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading && email.isNotBlank() && password.isNotBlank(),
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text("Login")
            }
        }
        GoogleSignInButton(
            enabled = !loading,
            onClick = {
                scope.launch {
                    runCatching {
                        requestNativeGoogleCredential(context, googleWebClientId)
                    }.onSuccess { credential ->
                        onGoogleSignIn(credential.idToken, credential.nonce)
                    }.onFailure { throwable ->
                        onGoogleSignInError(throwable.toGoogleSignInMessage())
                    }
                }
            },
        )
        OutlinedButton(
            onClick = { onRequestPasswordReset(email) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading && email.isNotBlank(),
        ) {
            Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
            Text("Reset password via email", modifier = Modifier.padding(start = 8.dp))
        }
        OutlinedButton(
            onClick = onShowRegister,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
            Text("Buat akun baru", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun RegisterScreen(
    loading: Boolean,
    error: String?,
    notice: String?,
    onRegister: (String, String) -> Unit,
    googleWebClientId: String,
    onGoogleSignIn: (String, String) -> Unit,
    onGoogleSignInError: (String) -> Unit,
    onShowLogin: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val localError = when {
        password.isNotBlank() && password.length < 6 -> "Password minimal 6 karakter."
        confirmPassword.isNotBlank() && password != confirmPassword -> "Konfirmasi password belum sama."
        else -> null
    }
    AuthScaffoldCard(
        eyebrow = "Daftar akun",
        title = "Buat akun belajar",
        description = "Gunakan email dan password Supabase. Kalau email confirmation aktif, kamu perlu konfirmasi email sebelum login.",
        error = localError ?: error,
        notice = notice,
    ) {
        AuthFields(
            email = email,
            password = password,
            enabled = !loading,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
        )
        PasswordTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            label = "Confirm password",
        )
        Button(
            onClick = { onRegister(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading &&
                localError == null &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank(),
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text("Register")
            }
        }
        GoogleSignInButton(
            enabled = !loading,
            onClick = {
                scope.launch {
                    runCatching {
                        requestNativeGoogleCredential(context, googleWebClientId)
                    }.onSuccess { credential ->
                        onGoogleSignIn(credential.idToken, credential.nonce)
                    }.onFailure { throwable ->
                        onGoogleSignInError(throwable.toGoogleSignInMessage())
                    }
                }
            },
        )
        OutlinedButton(
            onClick = onShowLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
        ) {
            Text("Sudah punya akun? Login")
        }
    }
}

@Composable
private fun AuthScaffoldCard(
    eyebrow: String,
    title: String,
    description: String,
    error: String?,
    notice: String?,
    content: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = RoundedCornerShape(34.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            lerp(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.surface, 0.18f),
                            MaterialTheme.colorScheme.surface,
                        ),
                    ),
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SummaryPill(eyebrow)
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            if (!notice.isNullOrBlank()) {
                AuthMessageCard(text = notice, error = false)
            }
            if (!error.isNullOrBlank()) {
                AuthMessageCard(text = error, error = true)
            }
            content()
        }
    }
}

@Composable
private fun AuthFields(
    email: String,
    password: String,
    enabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = { Text("Email") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        singleLine = true,
    )
    PasswordTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = "Password",
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = { Text(label) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }, enabled = enabled) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password",
                )
            }
        },
        singleLine = true,
    )
}

@Composable
private fun GoogleSignInButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
    ) {
        GoogleIcon()
        Text("Sign in with Google", modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
private fun GoogleIcon() {
    Canvas(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E3EB), CircleShape),
    ) {
        val strokeWidth = 2.6.dp.toPx()
        val inset = strokeWidth + 1.dp.toPx()
        val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -28f,
            sweepAngle = 88f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 54f,
            sweepAngle = 82f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 138f,
            sweepAngle = 70f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 206f,
            sweepAngle = 126f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(size.width * 0.52f, size.height * 0.5f),
            end = Offset(size.width * 0.82f, size.height * 0.5f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square,
        )
    }
}

private data class NativeGoogleCredential(
    val idToken: String,
    val nonce: String,
)

private suspend fun requestNativeGoogleCredential(
    context: Context,
    webClientId: String,
): NativeGoogleCredential {
    if (webClientId.isBlank()) {
        error("GOOGLE_WEB_CLIENT_ID belum terbaca dari local.properties.")
    }

    val nonce = generateRawNonce()
    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetSignInWithGoogleOption.Builder(webClientId)
        .setNonce(nonce.sha256())
        .build()
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
    val result = credentialManager.getCredential(
        context = context.findActivity(),
        request = request,
    )
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        return NativeGoogleCredential(
            idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken,
            nonce = nonce,
        )
    }

    error("Credential Google tidak valid.")
}

private fun generateRawNonce(): String {
    val bytes = ByteArray(32)
    SecureRandom().nextBytes(bytes)
    return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
}

private fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256").digest(toByteArray())
    return digest.joinToString("") { byte -> "%02x".format(byte) }
}

private fun Throwable.toGoogleSignInMessage(): String {
    val rawMessage = message.orEmpty()
    return when {
        rawMessage.contains("Account reauth failed", ignoreCase = true) ->
            "Google sign in gagal karena Android OAuth client belum cocok. Cek Google Cloud: package com.rjw.bunpoun3 dan SHA-1 harus terdaftar di OAuth Client type Android."
        rawMessage.contains("No credentials available", ignoreCase = true) ->
            "Tidak ada credential Google yang tersedia di perangkat ini. Coba tambah akun Google di perangkat, atau cek konfigurasi Google OAuth."
        rawMessage.isNotBlank() -> rawMessage
        else -> "Google sign in dibatalkan."
    }
}

@Composable
private fun AuthMessageCard(text: String, error: Boolean) {
    val container = if (error) {
        lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.errorContainer, 0.76f)
    } else {
        doneGreenContainerColor()
    }
    val content = if (error) MaterialTheme.colorScheme.error else doneGreenContentColor()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = container,
        contentColor = content,
        shape = RoundedCornerShape(18.dp),
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
        )
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

@Composable
private fun doneGreenContainerColor(): Color =
    lerp(MaterialTheme.colorScheme.surface, Color(0xFFCFEFDB), 0.82f)

@Composable
private fun doneGreenContentColor(): Color =
    Color(0xFF2F7D4F)

@Composable
private fun grammarAccentContainerColor(): Color =
    lerp(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.secondaryContainer, 0.68f)

@Composable
private fun grammarAccentContentColor(): Color =
    completionContentColor()

@Composable
private fun sentenceHighlightColor(): Color =
    MaterialTheme.colorScheme.tertiary

private fun Modifier.dashedRoundBorder(color: Color): Modifier = drawBehind {
    val strokeWidth = 1.2.dp.toPx()
    val radius = 22.dp.toPx()
    val borderSize = Size(size.width - strokeWidth, size.height - strokeWidth)
    drawRoundRect(
        color = color,
        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
        size = borderSize,
        cornerRadius = CornerRadius(radius, radius),
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10.dp.toPx(), 7.dp.toPx())),
        ),
    )
}

private fun lessonStateText(
    completed: Boolean,
    quizProgress: DayQuizProgress?,
): String = when {
    completed && quizProgress?.passed == true -> "Lulus latihan ${quizProgress.bestPercentage}%"
    quizProgress != null -> "Latihan ${quizProgress.attempts}x • terbaik ${quizProgress.bestPercentage}%"
    completed -> "Pelajaran selesai"
    else -> ""
}

@Composable
private fun LessonCompleteButton(
    completed: Boolean,
    onToggleComplete: () -> Unit,
) {
    FilledTonalIconButton(
        onClick = onToggleComplete,
        colors = androidx.compose.material3.IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = if (completed) {
                doneGreenContainerColor()
            } else {
                lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.68f)
            },
            contentColor = if (completed) {
                doneGreenContentColor()
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f)
            },
        ),
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = if (completed) "Batalkan selesai" else "Tandai selesai",
        )
    }
}

@Composable
private fun StartQuizCta(
    onStartQuiz: () -> Unit,
) {
    Button(
        onClick = onStartQuiz,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
        Text("Start Quiz", modifier = Modifier.padding(start = 8.dp))
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
    val contentTint = when {
        !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
        selected -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.88f)
    }
    val tabContainer = if (selected) {
        lerp(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, 0.12f)
    } else {
        Color.Transparent
    }
    val tabShape = RoundedCornerShape(22.dp)

    Row(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clip(tabShape)
            .background(tabContainer)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = contentTint,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text,
            modifier = Modifier.padding(start = 7.dp),
            color = contentTint,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ThemeSettingsScreen(
    authSession: AuthSession?,
    furiganaOn: Boolean,
    romajiOn: Boolean,
    themeMode: ThemeMode,
    themePreset: ThemePreset,
    onOpenProfile: () -> Unit,
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
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = BottomNavContentPadding),
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
            SectionTitle("Akun")
        }
        item {
            AccountSettingsCard(
                session = authSession,
                onOpenProfile = onOpenProfile,
            )
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
private fun AccountSettingsCard(
    session: AuthSession?,
    onOpenProfile: () -> Unit,
) {
    ElevatedCard(
        onClick = onOpenProfile,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
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
                    .clip(RoundedCornerShape(17.dp))
                    .background(doneGreenContainerColor()),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = doneGreenContentColor())
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text("Profile Supabase", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    session?.email?.takeIf { it.isNotBlank() } ?: "Session tersimpan di perangkat ini",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ProfileScreen(
    session: AuthSession?,
    loading: Boolean,
    error: String?,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    SummaryPill("Supabase profile")
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        session?.email?.takeIf { it.isNotBlank() } ?: "Akun aktif",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        "Session disimpan lokal agar akses materi tetap tersedia saat offline. Sync progress bisa dilanjutkan setelah endpoint tabel progress siap.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
                    )
                }
            }
        }
        item {
            SettingsSectionCard {
                ProfileInfoRow(label = "Email", value = session?.email.orEmpty().ifBlank { "Tidak tersedia" })
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                ProfileInfoRow(label = "User ID", value = session?.userId.orEmpty().ifBlank { "Tidak tersedia" })
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                ProfileInfoRow(
                    label = "Session",
                    value = if ((session?.accessToken).isNullOrBlank()) "Tidak aktif" else "Aktif di perangkat ini",
                )
            }
        }
        if (!error.isNullOrBlank()) {
            item {
                AuthMessageCard(text = error, error = true)
            }
        }
        item {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(if (loading) "Logout..." else "Logout", modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
private fun ResetPasswordScreen(
    loading: Boolean,
    error: String?,
    notice: String?,
    onUpdatePassword: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val localError = when {
        password.isNotBlank() && password.length < 6 -> "Password minimal 6 karakter."
        confirmPassword.isNotBlank() && password != confirmPassword -> "Konfirmasi password belum sama."
        else -> null
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AuthScaffoldCard(
            eyebrow = "Recovery",
            title = "Buat password baru",
            description = "Link reset dari email sudah membuka app. Masukkan password baru untuk menyelesaikan proses recovery.",
            error = localError ?: error,
            notice = notice,
        ) {
            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                label = "Password baru",
            )
            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
                label = "Ulangi password",
            )
            Button(
                onClick = { onUpdatePassword(password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && localError == null && password.isNotBlank() && confirmPassword.isNotBlank(),
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Simpan password baru")
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
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

private fun nextStudyDay(
    weeks: List<CatalogWeek>,
    completedDayIds: Set<Int>,
): CatalogDay? {
    val days = weeks.flatMap { it.days }.sortedWith(compareBy<CatalogDay> { it.weekNum }.thenBy { it.dayNum })
    if (days.isEmpty()) return null
    val lastCompletedIndex = days.indexOfLast { it.dayId in completedDayIds }
    return when {
        lastCompletedIndex < 0 -> days.first()
        lastCompletedIndex + 1 < days.size -> days[lastCompletedIndex + 1]
        else -> days.firstOrNull { it.dayId !in completedDayIds }
    }
}

@Composable
private fun HomeScreen(
    weeks: List<CatalogWeek>,
    completedDayIds: Set<Int>,
    onOpenLesson: (Int) -> Unit,
    onOpenWeek: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalDays = weeks.sumOf { it.days.size }
    val done = completedDayIds.size
    val continueDay = remember(weeks, completedDayIds) {
        nextStudyDay(weeks = weeks, completedDayIds = completedDayIds)
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = BottomNavContentPadding),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Bunpou Notes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
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
                        SummaryPill("${totalDays - done} pelajaran tersisa")
                    }
                    continueDay?.let { day ->
                        Button(
                            onClick = { onOpenLesson(day.dayId) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = completionContentColor(),
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                        ) {
                            Text("Lanjutkan belajar: Week ${day.weekNum} Day ${day.dayNum}")
                        }
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
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = BottomNavContentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LessonBreadcrumb(weekNum = week.weekNum)
                    StrongJapaneseTitleText(
                        text = week.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(week.titleId, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
        item {
            SectionTitle("Daftar pelajaran")
        }
        items(week.days) { day ->
            val completed = completedDayIds.contains(day.dayId)
            val quizProgress = quizProgressByDay[day.dayId]
            val cardColor = if (completed) {
                lerp(MaterialTheme.colorScheme.surface, completionContainerColor(strong = true), 0.34f)
            } else {
                MaterialTheme.colorScheme.surface
            }
            ElevatedCard(
                onClick = { onOpenLesson(day.dayId) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = cardColor),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                if (completed) {
                                    completionContainerColor(strong = true)
                                } else {
                                    lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.58f)
                                },
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            if (completed) "✓" else day.dayNum.toString(),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (completed) completionContentColor() else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            "第${week.weekNum}週 › ${day.dayNum}日目",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        StrongJapaneseTitleText(
                            text = day.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Surface(
                            color = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.42f),
                            shape = RoundedCornerShape(14.dp),
                        ) {
                            Text(
                                day.grammarPoints.joinToString(" ・ ") { it.pattern },
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        if (quizProgress != null) {
                            Text(
                                lessonStateText(completed = completed, quizProgress = quizProgress),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (completed) completionContentColor() else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.NavigateNext,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonBreadcrumb(
    weekNum: Int,
    dayNum: Int? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "第${weekNum}週",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
        dayNum?.let {
            Text(
                "›",
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.56f),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "${it}日目",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
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
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = BottomNavContentPadding),
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
                        LessonBreadcrumb(weekNum = day.weekNum, dayNum = day.dayNum)
                        StrongJapaneseTitleText(
                            text = day.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(day.titleId, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    LessonCompleteButton(
                        completed = completed,
                        onToggleComplete = {
                            onToggleComplete()
                            Toast.makeText(
                                context,
                                if (completed) "Pelajaran ditandai belum selesai" else "Pelajaran ditandai selesai",
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                    )
                }
                StartQuizCta(onStartQuiz = onStartQuiz)
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        grammar.pattern,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = sentenceHighlightColor(),
                    )
                    Surface(
                        color = grammarAccentContainerColor(),
                        contentColor = grammarAccentContentColor(),
                        shape = RoundedCornerShape(999.dp),
                    ) {
                        Text(
                            grammar.badge,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                HorizontalDivider(color = grammarAccentContainerColor().copy(alpha = 0.82f))
            }
            Text(grammar.meaning, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyLarge)
            BunpouNoteCard(grammar.meaningId)
            grammar.verbForm?.let { verbForm ->
                AccordionActionButton(
                    expanded = showVerbForm,
                    collapsedText = "Lihat pembentukan bunpou",
                    expandedText = "Sembunyikan pembentukan bunpou",
                    onClick = { showVerbForm = !showVerbForm },
                )
                AnimatedVisibility(
                    visible = showVerbForm,
                    enter = expandVertically(animationSpec = tween(240)) + fadeIn(animationSpec = tween(240)),
                    exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(180)),
                ) {
                    VerbFormTable(verbForm)
                }
            }
            SectionTitle("Contoh kalimat / 例文")
            grammar.examples.forEach { example ->
                ExampleCard(example, furiganaOn, romajiOn, readings, romajiMap, false)
            }
            if (grammar.extraExamples.isNotEmpty()) {
                AccordionActionButton(
                    expanded = showExtras,
                    collapsedText = "Lihat ${grammar.extraExamples.size} contoh tambahan",
                    expandedText = "Sembunyikan contoh tambahan",
                    onClick = { showExtras = !showExtras },
                )
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
private fun BunpouNoteCard(text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = lerp(MaterialTheme.colorScheme.surface, grammarAccentContainerColor(), 0.34f),
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(end = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 54.dp)
                    .background(MaterialTheme.colorScheme.primary),
            )
            Text(
                text,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun AccordionActionButton(
    expanded: Boolean,
    collapsedText: String,
    expandedText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(22.dp)
    val contentColor = grammarAccentContentColor()
    val containerColor = grammarAccentContainerColor()
    val stateModifier = if (expanded) {
        Modifier.background(containerColor)
    } else {
        Modifier.dashedRoundBorder(contentColor.copy(alpha = 0.52f))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .then(stateModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            if (expanded) "▲" else "▼",
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            if (expanded) expandedText else collapsedText,
            modifier = Modifier.weight(1f),
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun VerbFormTable(verbForm: CatalogVerbForm) {
    val tableContainerColor = lerp(
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.secondaryContainer,
        0.36f,
    )
    OutlinedCard(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = tableContainerColor,
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                verbForm.title,
                color = grammarAccentContentColor(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            verbForm.rows.forEach { row ->
                VerbFormRowCard(row)
            }
        }
    }
}

@Composable
private fun VerbFormRowCard(row: CatalogVerbFormRow) {
    val rowShape = RoundedCornerShape(10.dp)
    val rowContainerColor = lerp(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.secondaryContainer,
        0.3f,
    )
    val badgeContainerColor = lerp(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        0.38f,
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f),
                shape = rowShape,
            ),
        colors = CardDefaults.cardColors(
            containerColor = rowContainerColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = rowShape,
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    color = badgeContainerColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(6.dp),
                ) {
                    Text(
                        row.label,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    row.body,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(
                row.bodyId,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (row.example.isNotBlank()) {
                Surface(
                    color = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.errorContainer, 0.28f),
                    contentColor = sentenceHighlightColor(),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(
                        "例: ${row.example}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                    )
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
    val containerColor = if (isExtra) {
        lerp(MaterialTheme.colorScheme.surface, grammarAccentContainerColor(), 0.46f)
    } else {
        lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.46f)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            RubyText(
                html = example.japaneseHtml,
                showFurigana = furiganaOn,
                readings = readings,
                surfaceFontSize = 17.sp,
                surfaceLineHeight = 23.sp,
                readingFontSize = 8.sp,
                readingLineHeight = 9.sp,
            )
            if (romajiOn) {
                Text(
                    toRomaji(example.japaneseHtml, readings, romajiMap),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.64f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = ExampleItalicFontFamily,
                        fontStyle = FontStyle.Italic,
                    ),
                )
            }
            Text(
                example.indonesian,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = ExampleItalicFontFamily,
                    fontStyle = FontStyle.Italic,
                ),
                lineHeight = 21.sp,
            )
        }
    }
}

@Composable
private fun QuizScreen(
    quizState: QuizState?,
    furiganaOn: Boolean,
    romajiOn: Boolean,
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
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = BottomNavContentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            BlendedHeaderCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Mode latihan", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Text(quizState.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        SummaryPill("Skor ${quizState.score}")
                    }
                    LinearProgressIndicator(
                        progress = { quizState.index.toFloat() / quizState.questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(999.dp)),
                    )
                    Text(
                        "Soal ${quizState.index + 1} dari ${quizState.questions.size}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.76f),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.24f),
                ),
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Pilih pola grammar yang paling tepat", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.42f),
                        ),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            RubyText(
                                html = question.promptHtml,
                                showFurigana = furiganaOn,
                                readings = readings,
                                surfaceFontSize = 17.sp,
                                surfaceLineHeight = 23.sp,
                                readingFontSize = 8.sp,
                                readingLineHeight = 9.sp,
                            )
                            if (romajiOn) {
                                Text(
                                    toRomaji(question.promptHtml, readings, romajiMap),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.64f),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = ExampleItalicFontFamily,
                                        fontStyle = FontStyle.Italic,
                                    ),
                                )
                            }
                            Text(
                                question.promptId,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = ExampleItalicFontFamily,
                                    fontStyle = FontStyle.Italic,
                                ),
                                lineHeight = 21.sp,
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
                    containerColor = MaterialTheme.colorScheme.surface,
                )
                correct -> CardDefaults.elevatedCardColors(
                    containerColor = doneGreenContainerColor(),
                )
                selected -> CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.errorContainer, 0.52f),
                )
                else -> CardDefaults.elevatedCardColors(
                    containerColor = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.18f),
                )
            }
            ElevatedCard(
                onClick = { onAnswer(option) },
                enabled = !answered,
                modifier = Modifier.fillMaxWidth(),
                colors = colors,
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (answered) 1.dp else 2.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val optionBadgeColor = when {
                        correct && answered -> doneGreenContentColor()
                        selected && answered -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(optionBadgeColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            ('A' + indexed.index).toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (answered && (correct || selected)) {
                                Color.White
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }
                    Text(option, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        if (answered) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) {
                            doneGreenContainerColor()
                        } else {
                            lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.errorContainer, 0.52f)
                        },
                    ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            if (isCorrect) "正解！ Benar! ✓" else "不正解 Salah. ✗",
                            fontWeight = FontWeight.Bold,
                            color = if (isCorrect) doneGreenContentColor() else MaterialTheme.colorScheme.error,
                        )
                        if (!isCorrect) Text("正解: ${question.correctPattern}", fontWeight = FontWeight.SemiBold)
                        Text(question.meaningId, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    val titleStyle = style.copy(
        fontFamily = JapaneseTitleFontFamily,
        fontWeight = FontWeight.Black,
    )
    Box(modifier = modifier) {
        Text(
            text = text,
            style = titleStyle.copy(
                drawStyle = Stroke(width = 0.28f),
            ),
            color = color,
            maxLines = maxLines,
            overflow = overflow,
        )
        Text(
            text = text,
            style = titleStyle.copy(
                drawStyle = Fill,
            ),
            color = color,
            maxLines = maxLines,
            overflow = overflow,
        )
    }
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp),
            color = lerp(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant, 0.28f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            tonalElevation = 6.dp,
            shadowElevation = 18.dp,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.46f),
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
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
            Screen.Profile -> AppTab.Home
            Screen.ResetPassword -> AppTab.Home
        }
        Screen.Profile -> when (profileReturnScreen) {
            Screen.Home -> AppTab.Home
            is Screen.Week, is Screen.Lesson -> AppTab.Learn
            Screen.Quiz -> AppTab.Quiz
            Screen.Settings, Screen.Profile, Screen.ResetPassword -> AppTab.Home
        }
        Screen.ResetPassword -> AppTab.Home
    }

private fun UiState.recommendedWeekNum(): Int? {
    val weeks = catalog?.weeks ?: return null
    return when (val current = screen) {
        is Screen.Week -> current.weekNum
        is Screen.Lesson -> weeks.firstOrNull { week -> week.days.any { it.dayId == current.dayId } }?.weekNum
        Screen.Profile -> weeks.firstOrNull { week -> week.days.any { it.dayId !in completedDayIds } }?.weekNum
            ?: weeks.firstOrNull()?.weekNum
        Screen.ResetPassword -> weeks.firstOrNull { week -> week.days.any { it.dayId !in completedDayIds } }?.weekNum
            ?: weeks.firstOrNull()?.weekNum
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
    surfaceFontSize: androidx.compose.ui.unit.TextUnit = 18.sp,
    surfaceLineHeight: androidx.compose.ui.unit.TextUnit = 24.sp,
    readingFontSize: androidx.compose.ui.unit.TextUnit = 9.sp,
    readingLineHeight: androidx.compose.ui.unit.TextUnit = 10.sp,
) {
    val segments = remember(html) { parseStrongSegments(stripTagsExceptStrong(html)) }
    val sorted = remember(readings) { readings.entries.sortedByDescending { it.key.length } }
    val tokens = remember(segments, sorted) { tokenizeRubySegments(segments, sorted) }

    RubyWrapLayout(modifier = Modifier.fillMaxWidth()) {
        tokens.forEach { token ->
            RubyTokenView(
                token = token,
                showFurigana = showFurigana,
                surfaceFontSize = surfaceFontSize,
                surfaceLineHeight = surfaceLineHeight,
                readingFontSize = readingFontSize,
                readingLineHeight = readingLineHeight,
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
    surfaceFontSize: androidx.compose.ui.unit.TextUnit,
    surfaceLineHeight: androidx.compose.ui.unit.TextUnit,
    readingFontSize: androidx.compose.ui.unit.TextUnit,
    readingLineHeight: androidx.compose.ui.unit.TextUnit,
) {
    val weight = if (token.bold) FontWeight.Bold else FontWeight.Normal
    val tokenColor = if (token.bold) sentenceHighlightColor() else MaterialTheme.colorScheme.onSurface
    if (!showFurigana || token.reading == null || !token.surface.any(::isKanji)) {
        Text(
            token.surface,
            fontSize = surfaceFontSize,
            lineHeight = surfaceLineHeight,
            fontWeight = weight,
            color = tokenColor,
        )
        return
    }

    val furiganaGap = with(LocalDensity.current) { 1.dp.roundToPx() }
    val sidePadding = with(LocalDensity.current) { 2.dp.roundToPx() }

    Layout(
        content = {
            Text(
                token.reading,
                fontSize = readingFontSize,
                lineHeight = readingLineHeight,
                fontWeight = FontWeight.Medium,
                color = if (token.bold) sentenceHighlightColor() else MaterialTheme.colorScheme.primary,
            )
            Text(
                token.surface,
                fontSize = surfaceFontSize,
                lineHeight = surfaceLineHeight,
                fontWeight = weight,
                color = tokenColor,
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
