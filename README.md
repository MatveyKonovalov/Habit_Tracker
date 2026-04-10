<div align="center">
  
  # 🎯 HabitTracker
  
  **Приложение для отслеживания привычек с системой ежедневных задач, статистикой и напоминаниями**
  
  [![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
  [![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09.00-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
  [![Material3](https://img.shields.io/badge/Material%20Design-3-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)
  [![Room](https://img.shields.io/badge/Room-Database-FFB300?style=for-the-badge&logo=sqlite&logoColor=white)](https://developer.android.com/jetpack/androidx/releases/room)
  [![Hilt](https://img.shields.io/badge/Hilt-DI-2C3E50?style=for-the-badge&logo=dagger&logoColor=white)](https://dagger.dev/hilt/)
  [![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=26)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
  
</div>

---

## 📱 О проекте

**HabitTracker** — это современное Android-приложение для формирования полезных привычек. Оно помогает пользователям отслеживать ежедневные задачи, анализировать прогресс и поддерживать мотивацию через систему стриков (непрерывных серий выполнений). Приложение поддерживает напоминания через уведомления и сохраняет данные локально.

### ✨ Основные возможности

| Возможность | Описание |
|-------------|----------|
| ✅ **Управление задачами** | Создание, редактирование и удаление задач с приоритетами (Высокий/Средний/Низкий) |
| 📊 **Статистика** | Визуализация прогресса по дням с процентами выполнения и цветовой индикацией |
| 🔥 **Система стриков** | Отслеживание текущей и максимальной серии выполнений задач |
| ⏰ **Напоминания** | Настройка ежедневных уведомлений о невыполненных задачах |
| 🎨 **Material Design 3** | Современный интерфейс с динамическими цветами и плавными анимациями |
| 📈 **Детальная аналитика** | Просмотр статистики по каждому дню со списком невыполненных задач |
| 🎬 **Анимированная навигация** | Плавные переходы между экранами с визуальной обратной связью |
| 🔄 **Фоновая синхронизация** | Автоматическое обновление данных через WorkManager |

---

## 🏗️ Архитектура

Проект построен на **Clean Architecture** с разделением на слои:

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  • Jetpack Compose UI (Screens, Components)                 │
│  • ViewModels (StateFlow + Compose)                         │
│  • Navigation (Compose Navigation + BottomBar)              │
├─────────────────────────────────────────────────────────────┤
│                       Domain Layer                           │
│  • Domain Models (Task, DayStatistics, TaskPriority)        │
│  • Repository Interfaces (TaskRepository)                   │
│  • Business Logic (WorkWithTimeHelper)                      │
├─────────────────────────────────────────────────────────────┤
│                        Data Layer                            │
│  • Repository Implementations                               │
│  • Room Database (DAOs, Entities, Mappers)                  │
│  • DataStore (User preferences)                             │
│  • WorkManager (Background tasks)                           │
│  • BroadcastReceiver (Boot completed)                       │
└─────────────────────────────────────────────────────────────┘
```

### 🛠️ Технологический стек

| Компонент | Технология |
|-----------|------------|
| **Язык** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **DI** | Dagger Hilt |
| **База данных** | Room (SQLite) |
| **Preferences** | DataStore |
| **Асинхронность** | Kotlin Coroutines + Flow |
| **Навигация** | Compose Navigation |
| **Фоновые задачи** | WorkManager |
| **Уведомления** | AlarmManager + BroadcastReceiver |
| **Архитектура** | MVVM + Clean Architecture |
| **Минимальная версия** | Android 8.0 (API 26) |
| **Target SDK** | Android 14 (API 34) |

---

## 📁 Структура проекта

```
com.example.habittracker/
│
├── data/
│   ├── local/
│   │   ├── daos/
│   │   │   ├── DayDao.kt              # DAO для статистики дней
│   │   │   ├── MaxStreakDao.kt        # DAO для максимальной серии
│   │   │   └── TaskDao.kt             # DAO для задач
│   │   ├── entities/
│   │   │   ├── DayEntity.kt           # Сущность дня (Room)
│   │   │   ├── MaxStreekEntity.kt     # Сущность макс. серии
│   │   │   └── TaskEntity.kt          # Сущность задачи
│   │   ├── helpers/
│   │   │   └── WorkWithTimeHelper.kt  # Хелпер для работы с датами
│   │   ├── mappers/
│   │   │   ├── DayEntityMapper.kt     # Маппер дня
│   │   │   ├── DayOfWeekMapper.kt     # Маппер дня недели
│   │   │   ├── IncompleteTasksMapper.kt # Маппер невыполненных задач
│   │   │   ├── PriorityMapper.kt      # Маппер приоритетов
│   │   │   └── TaskEntityMapper.kt    # Маппер задачи
│   │   └── AppDataBase.kt             # База данных Room
│   ├── datastore/
│   │   └── SettingsDataStore.kt       # DataStore для настроек
│   ├── worker/
│   │   └── NotificationWorker.kt      # Worker для уведомлений
│   └── TaskRepository.kt              # Реализация репозитория
│
├── domain/
│   ├── models/
│   │   ├── DayStatistics.kt           # Модель статистики дня
│   │   ├── IncompleteTasks.kt         # Модель невыполненных задач
│   │   ├── Task.kt                    # Модель задачи
│   │   └── TaskPriority.kt            # Enum приоритетов
│   └── TaskRepository.kt              # Интерфейс репозитория
│
├── presentation/
│   ├── navigation/
│   │   ├── BottomNavigationBar.kt     # Нижняя навигация с анимацией
│   │   └── Screen.kt                  # Экранные маршруты
│   ├── view/
│   │   ├── components/
│   │   │   ├── DayDetails.kt          # Компонент деталей дня
│   │   │   ├── TaskDialog.kt          # Диалог добавления/редактирования
│   │   │   ├── TimePickerDialog.kt    # Диалог выбора времени
│   │   │   └── UserInfoDialog.kt      # Диалог информации о пользователе
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt          # Главный экран
│   │   │   ├── SettingsScreen.kt      # Экран настроек
│   │   │   ├── StaticScreen.kt        # Экран статистики
│   │   │   ├── TaskScreen.kt          # Экран задач
│   │   │   └── MainScreen.kt          # Основной экран с BottomBar
│   │   ├── StruckStatistics.kt        # Компонент отображения стриков
│   │   └── TasksList.kt               # Список задач
│   └── viewmodels/
│       ├── NavigationViewModel.kt     # VM для навигации
│       ├── SettingsViewModel.kt       # VM для настроек
│       ├── StatisticsViewModel.kt     # VM для статистики
│       └── TaskManagerViewModel.kt    # VM для управления задачами
│
├── receiver/
│   ├── BootReceiver.kt                # Приёмник загрузки устройства
│   └── receiver.kt                    # Дополнительные ресиверы
│
├── ui/
│   └── theme/
│       ├── Color.kt                   # Цветовая схема
│       ├── Theme.kt                   # Тема приложения
│       └── Type.kt                    # Типографика
│
├── utils/
│   ├── AlarmHelper.kt                 # Хелпер для будильников
│   └── App.kt                         # Application класс
│
├── di/
│   ├── AppModule.kt                   # Основной DI модуль
│   ├── LocalDataModule.kt             # DI модуль для локальных данных
│   ├── RepositoryModule.kt            # DI модуль для репозиториев
│   └── WorkManagerModule.kt           # DI модуль для WorkManager
│
└── MainActivity.kt                    # Главная активность
```

---

## 🗄️ База данных

### Схема базы данных

```sql
-- Таблица задач
CREATE TABLE tasks (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    isCompleted INTEGER DEFAULT 0,
    priority TEXT
)

-- Таблица статистики дней
CREATE TABLE days (
    idDay TEXT PRIMARY KEY,
    dayOfWeek TEXT NOT NULL,
    completedTasks INTEGER DEFAULT 0,
    totalTasks INTEGER DEFAULT 0,
    incompleteTasks TEXT  -- JSON строка с ID задач
)

-- Таблица максимальной серии
CREATE TABLE max_streak (
    maxStreak INTEGER DEFAULT 0
)
```

### DAO интерфейсы

```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>
    
    @Insert
    suspend fun insertTask(task: TaskEntity)
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: String)
}

@Dao
interface DayDao {
    @Query("SELECT * FROM days")
    suspend fun getAllDay(): List<DayEntity>
    
    @Query("SELECT * FROM days WHERE idDay = :date")
    suspend fun getDayByData(date: String): List<DayEntity>
    
    @Insert
    suspend fun insert(day: DayEntity)
    
    @Update
    suspend fun updateDay(day: DayEntity)
}

@Dao
interface MaxStreakDao {
    @Query("SELECT * FROM max_streak")
    suspend fun getMaxStreak(): List<MaxStreakEntity>
    
    @Insert
    suspend fun insertMaxStreak(maxStreak: MaxStreakEntity)
    
    @Query("DELETE FROM max_streak")
    suspend fun deleteStreak()
}
```

---

## 🎯 Основные функции в коде

### Управление задачами (TaskManagerViewModel)

```kotlin
@HiltViewModel
class TaskManagerViewModel @Inject constructor(
    private val appRepository: TaskRepository
) : ViewModel() {
    
    // Состояния UI
    private val _tasksTodayComplete = MutableStateFlow(emptyList<Task>())
    val tasksTodayComplete: StateFlow<List<Task>> = _tasksTodayComplete.asStateFlow()
    
    private val _currentStrick = MutableStateFlow(0)
    val currentStrick: StateFlow<Int> = _currentStrick.asStateFlow()
    
    private val _maxStrick = MutableStateFlow(0)
    val maxStrick: StateFlow<Int> = _maxStrick.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Добавление задачи
    fun addTask(title: String, description: String, priority: TaskPriority) {
        viewModelScope.launch {
            _isLoading.value = true
            val task = Task(
                id = random().toString(),
                title = title,
                description = description,
                isCompleted = false,
                priority = priority
            )
            _tasksAll.value += task
            appRepository.addTask(task)
            _isLoading.value = false
            makeChange()
        }
    }
    
    // Выполнение задачи
    fun completeTask(id: String) {
        viewModelScope.launch {
            _tasksTodayComplete.value = _tasksTodayComplete.value.map { task ->
                if (task.id == id) task.copy(isCompleted = !task.isCompleted)
                else task
            }
            withContext(Dispatchers.IO) {
                appRepository.completeTask(id)
            }
            _currentStrick.value = appRepository.getCurrentStreak()
            _maxStrick.value = appRepository.getMaxStreak()
            makeChange()
        }
    }
}
```

### Работа с датами (WorkWithTimeHelper)

```kotlin
class WorkWithTimeHelper {
    fun toDateTime(date: String): LocalDateTime? {
        return runCatching {
            LocalDate.parse(date).atStartOfDay()
        }.getOrNull()
    }
    
    fun isNext(dtPrev: LocalDateTime, dtNext: LocalDateTime): Boolean {
        return dtNext.toLocalDate() == dtPrev.toLocalDate().plusDays(1)
    }
}
```

### Настройка уведомлений (AlarmHelper)

```kotlin
class AlarmHelper(private val context: Context) {
    fun scheduleDailyReminder(hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BootReceiver::class.java).apply {
            action = "NOTIFICATION_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
        }
        
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
```

---

## 🎨 UI Компоненты


### Индикатор загрузки

```kotlin
@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(56.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Загрузка задач...", fontSize = 16.sp)
            Text("Пожалуйста, подождите", fontSize = 13.sp)
        }
    }
}
```

---

## 🔄 Навигация

### Экранные маршруты

```kotlin
sealed class Screen(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object Home : Screen("home", "Главная", Icons.Outlined.Home, Icons.Filled.Home)
    object Tasks : Screen("tasks", "Задачи", Icons.Outlined.List, Icons.Filled.List)
    object Statistics : Screen("statistics", "Статистика", Icons.Outlined.BarChart, Icons.Filled.BarChart)
    object Settings : Screen("settings", "Настройки", Icons.Outlined.Settings, Icons.Filled.Settings)
    
    companion object {
        val screens = listOf(Home, Tasks, Statistics, Settings)
    }
}
```

### NavGraph

```kotlin
@Composable
fun NavGraph(
    startDestination: String = Screen.Home.route,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideIntoContainer(Left, tween(300)) },
        exitTransition = { slideOutOfContainer(Right, tween(300)) }
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Tasks.route) { TaskScreen() }
        composable(Screen.Statistics.route) { StaticScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
```

---

## 📦 Dagger Hilt DI модули

### AppModule.kt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context
}
```

### LocalDataModule.kt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "habit_tracker.db"
        ).build()
    }
    
    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
    
    @Provides
    fun provideDayDao(database: AppDatabase): DayDao = database.dayDao()
    
    @Provides
    fun provideMaxStreakDao(database: AppDatabase): MaxStreakDao = database.maxStreakDao()
}
```

---

## 🚦 Статус проекта

| Функция | Статус |
|---------|--------|
| ✅ Управление задачами (CRUD) | Завершено |
| ✅ Система стриков | Завершено |
| ✅ Статистика по дням | Завершено |
| ✅ Анимированная навигация | Завершено |
| ✅ Индикаторы загрузки | Завершено |
| ✅ Напоминания и уведомления | Завершено |
| ✅ Material Design 3 | Завершено |
| ✅ DI с Hilt | Завершено |
| ✅ Фоновая синхронизация | Завершено |
| 🟡 Экспорт/импорт данных | В планах |
| 🟡 Unit тесты | В планах |

---

## 🚀 Установка и запуск

### Требования
- Android Studio Hedgehog | 2023.1.1 или новее
- JDK 17
- Android SDK API 34+
- Устройство или эмулятор с Android 8.0+

### Шаги для установки

1. **Клонируйте репозиторий**
   ```bash
   git clone https://github.com/yourusername/HabitTracker.git
   cd HabitTracker
   ```

2. **Откройте проект в Android Studio**
   - File → Open → выберите папку проекта
   - Дождитесь завершения синхронизации Gradle

3. **Соберите проект**
   ```bash
   ./gradlew build
   ```

4. **Запустите приложение**
   - Подключите устройство или запустите эмулятор
   - Нажмите `Run` (зелёная стрелка)

---

## 🤝 Вклад в проект

1. Форкните репозиторий
2. Создайте ветку для фичи (`git checkout -b feature/AmazingFeature`)
3. Зафиксируйте изменения (`git commit -m 'Add some AmazingFeature'`)
4. Запушьте ветку (`git push origin feature/AmazingFeature`)
5. Откройте Pull Request

---

## 📄 Лицензия

Распространяется под лицензией MIT. Смотрите файл `LICENSE` для подробностей.

```text
MIT License

Copyright (c) 2024 HabitTracker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
```

---

<div align="center">
  
  ***
  
  **⭐ Не забудьте поставить звезду, если проект оказался полезным! ⭐**
  
  **Made with ❤️ and Kotlin**
  
  ***
  
</div>
```
