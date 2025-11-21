package com.example.metermaster.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val vm: com.example.metermaster.ui.viewmodel.MeterViewModel = viewModel()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val current = navController.currentBackStackEntryAsState().value?.destination?.route ?: "consumption"
                BottomNavigationItem(icon = { Icon(Icons.Default.Home, null) }, label = { Text("Zużycie") }, selected = current=="consumption", onClick={ navController.navigate("consumption"){ launchSingleTop=true }})
                BottomNavigationItem(icon = { Icon(Icons.Default.BarChart, null) }, label = { Text("Statystyki") }, selected = current=="stats", onClick={ navController.navigate("stats"){ launchSingleTop=true }})
                BottomNavigationItem(icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Ustawienia") }, selected = current=="settings", onClick={ navController.navigate("settings"){ launchSingleTop=true }})
            }
        }
    ) { inner ->
        NavHost(navController, startDestination = "consumption", modifier = Modifier) {
            composable("consumption"){ ConsumptionScreen(vm, onOpen = { c -> navController.navigate("details/${c.id}") }) }
            composable("stats"){ StatsScreen() }
            composable("settings"){ SettingsScreen() }
            composable("details/{counterId}") { backEntry ->
                MeterDetailsScreen(com.example.metermaster.data.Counter(id = backEntry.arguments?.getString("counterId") ?: "", name = "Licznik", type = "woda"), vm)
            }
        }
    }
}
