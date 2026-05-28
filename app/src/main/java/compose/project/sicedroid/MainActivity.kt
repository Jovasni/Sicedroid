package compose.project.sicedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.project.sicedroid.data.model.*
import compose.project.sicedroid.ui.*
import compose.project.sicedroid.ui.theme.SicedroidTheme
import compose.project.sicedroid.util.SicenetParser
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SicedroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: SicenetViewModel = viewModel()
                    SicenetAppNavigation(viewModel)
                }
            }
        }
    }
}

@Composable
fun SicenetAppNavigation(viewModel: SicenetViewModel) {
    Crossfade(targetState = viewModel.loginUiState, label = "LoginTransition") { state ->
        when (state) {
            is LoginState.Success -> {
                MainContent(viewModel)
            }
            else -> {
                LoginScreen(viewModel = viewModel, currentState = state)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(viewModel: SicenetViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = {
                        navController.navigate("profile")
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Carga Académica") },
                    selected = false,
                    onClick = {
                        navController.navigate("carga")
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Historial (Kardex)") },
                    selected = false,
                    onClick = {
                        navController.navigate("kardex")
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Calificaciones Unidad") },
                    selected = false,
                    onClick = {
                        navController.navigate("calif_unidad")
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Calificaciones Finales") },
                    selected = false,
                    onClick = {
                        navController.navigate("calif_final")
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SICENET") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "profile",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("profile") { ProfileScreen(viewModel) }
                composable("carga") { CargaScreen(viewModel) }
                composable("kardex") { KardexScreen(viewModel) }
                composable("calif_unidad") { CalifUnidadScreen(viewModel) }
                composable("calif_final") { CalifFinalScreen(viewModel) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: SicenetViewModel, currentState: LoginState) {
    var matricula by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    val opcionesUsuario = listOf("ALUMNO", "1")
    var expanded by remember { mutableStateOf(false) }
    var tipoUsuarioSeleccionado by remember { mutableStateOf(opcionesUsuario[0]) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SICENET", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = matricula, onValueChange = { matricula = it }, label = { Text("Matrícula") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = contrasenia, onValueChange = { contrasenia = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(value = tipoUsuarioSeleccionado, onValueChange = {}, readOnly = true, label = { Text("Tipo") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                opcionesUsuario.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { tipoUsuarioSeleccionado = it; expanded = false }) }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (currentState is LoginState.Loading) CircularProgressIndicator()
        else Button(onClick = { if (matricula.isNotBlank() && contrasenia.isNotBlank()) viewModel.login(matricula, contrasenia, tipoUsuarioSeleccionado) }, modifier = Modifier.fillMaxWidth()) { Text("Iniciar Sesión") }
        if (currentState is LoginState.Error) Text(currentState.message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun ProfileScreen(viewModel: SicenetViewModel) {
    LaunchedEffect(Unit) { viewModel.fetchProfile() }
    val state = viewModel.profileUiState
    FeatureBaseScreen(title = "Mi Perfil Académico", state = state) { data ->
        val perfil = SicenetParser.parsePerfil(data)
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = perfil.nombre.take(1),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(perfil.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(perfil.matricula, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoStatCard(Modifier.weight(1f), "Semestre", perfil.semActual.toString(), Icons.Default.School)
                InfoStatCard(Modifier.weight(1f), "Estatus", perfil.estatus, Icons.Default.Verified)
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow(Icons.Default.Book, "Carrera", perfil.carrera)
                    DetailRow(Icons.Default.Star, "Especialidad", perfil.especialidad)
                    DetailRow(Icons.Default.Numbers, "Créditos", "${perfil.cdtosAcumulados} acumulados")
                    DetailRow(Icons.Default.CalendarToday, "Reinscripción", perfil.fechaReins)
                }
            }
        }
    }
}

@Composable
fun InfoStatCard(modifier: Modifier, label: String, value: String, icon: ImageVector) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.secondary)
            Text(label, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CargaScreen(viewModel: SicenetViewModel) {
    LaunchedEffect(Unit) { viewModel.fetchCarga() }
    val state = viewModel.cargaUiState
    FeatureBaseScreen(title = "Carga Académica", state = state) { data ->
        val carga = SicenetParser.parseCarga(data)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(carga) { materia ->
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(materia.materia, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(materia.grupo, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        Text(materia.maestro, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                        Text("ID: ${materia.clvMat}", style = MaterialTheme.typography.labelSmall)
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        
                        val days = listOf(
                            "Lun" to materia.lunes,
                            "Mar" to materia.martes,
                            "Mie" to materia.miercoles,
                            "Jue" to materia.jueves,
                            "Vie" to materia.viernes,
                            "Sab" to materia.sabado
                        ).filter { it.second.isNotBlank() }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            days.forEach { (day, time) ->
                                Column(
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = day,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    val timeParts = time.split("-")
                                    Text(
                                        text = timeParts.firstOrNull() ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                    if (timeParts.size > 1) {
                                        Text(
                                            text = "a",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontSize = 9.sp
                                        )
                                        Text(
                                            text = timeParts.last(),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KardexScreen(viewModel: SicenetViewModel) {
    LaunchedEffect(Unit) { viewModel.fetchKardex() }
    val state = viewModel.kardexUiState
    FeatureBaseScreen(title = "Historial Académico", state = state) { data ->
        val kardex = SicenetParser.parseKardex(data)
        val grouped = kardex.groupBy { it.semestre }.toSortedMap()
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            grouped.forEach { (semestre, materias) ->
                item {
                    Text("Semestre $semestre", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                items(materias) { materia ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(materia.materia, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text(materia.periodo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                            }
                            GradeBadge(materia.calif)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradeBadge(grade: Int) {
    val containerColor = when {
        grade >= 90 -> MaterialTheme.colorScheme.primary
        grade >= 70 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Text(
            text = grade.toString(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CalifUnidadScreen(viewModel: SicenetViewModel) {
    LaunchedEffect(Unit) { viewModel.fetchCalifUnidad() }
    val state = viewModel.califUnidadUiState
    FeatureBaseScreen(title = "Seguimiento de Unidades", state = state) { data ->
        val califs = SicenetParser.parseCalifUnidad(data)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(califs) { item ->
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.materia, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                            item.unidades.forEachIndexed { index, score ->
                                UnitScoreBox(index + 1, score)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text("Promedio actual:", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(item.promedio, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnitScoreBox(unit: Int, score: String) {
    Column(
        modifier = Modifier
            .width(45.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("U$unit", style = MaterialTheme.typography.labelSmall)
        Text(score, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CalifFinalScreen(viewModel: SicenetViewModel) {
    LaunchedEffect(Unit) { viewModel.fetchCalifFinal() }
    val state = viewModel.califFinalUiState
    FeatureBaseScreen(title = "Calificaciones Finales", state = state) { data ->
        val finalGrades = SicenetParser.parseCalifFinal(data)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(finalGrades) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.materia, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text(item.evaluacion, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                        GradeBadge(item.calif)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureBaseScreen(title: String, state: FeatureState, content: @Composable (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(16.dp))
        when (state) {
            is FeatureState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(strokeWidth = 4.dp) }
            is FeatureState.Success -> {
                if (state.lastUpdated != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        shape = CircleShape
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            val dateStr = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(state.lastUpdated))
                            Text(text = "Sincronizado: $dateStr", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (state.data.isBlank()) {
                    EmptyStateView()
                } else {
                    content(state.data)
                }
            }
            is FeatureState.Error -> ErrorView(state.message)
            else -> {}
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
        Text("No hay datos disponibles", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ErrorView(message: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun ScheduleRow(day: String, time: String) {
    if (time.isNotBlank()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "$day:", style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(70.dp))
            Text(text = time, style = MaterialTheme.typography.bodySmall)
        }
    }
}
