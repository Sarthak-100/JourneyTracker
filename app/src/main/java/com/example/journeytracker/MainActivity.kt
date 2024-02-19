package com.example.journeytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journeytracker.ui.theme.JourneyTrackerTheme

data class Stop(val name: String, val distance: Double)

//val stops = List(10) { index ->
//    Stop("Stop ${index + 1}", (index + 1) * 10.0)
//}

val stops = List(20) { index ->
    Stop("Stop ${index + 1}", (index + 1) * 10.0)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JourneyTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var unit by remember { mutableStateOf("km") }
    var currentStopIndex by remember { mutableStateOf(0) }

    val isLargeList = stops.size > 10

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = "Journey Tracker",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    unit = if (unit == "km") "miles" else "km"
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Switch to ${if (unit == "km") "miles" else "km"}")
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Current Stop: ${stops[currentStopIndex].name}",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    currentStopIndex++
                    if (currentStopIndex >= stops.size) {
                        currentStopIndex = 0
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Next Stop Reached")
            }

            Spacer(Modifier.height(16.dp))

            val totalDistanceCovered = stops.filterIndexed { index, _ -> index < currentStopIndex }
                .sumOf { it.distance }
            val totalDistanceLeft = stops.filterIndexed { index, _ -> index >= currentStopIndex }
                .sumOf { it.distance }

            val totalDistanceCoveredText = "%.2f".format(if (unit == "km") totalDistanceCovered else totalDistanceCovered * 0.621371)
            val totalDistanceLeftText = "%.2f".format(if (unit == "km") totalDistanceLeft else totalDistanceLeft * 0.621371)

            Text(
                text = "Total Distance Covered: $totalDistanceCoveredText $unit",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Total Distance Left: $totalDistanceLeftText $unit",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            val progressPercentage = if (totalDistanceCovered + totalDistanceLeft > 0) {
                (totalDistanceCovered / (totalDistanceCovered + totalDistanceLeft)) * 100
            } else {
                0.0
            }

            Text(
                text = "Progress Percentage: ${"%.2f".format(progressPercentage)}%",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = (progressPercentage / 100).toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (isLargeList) {
                LazyColumn {
                    items(stops.size) { index ->
                        val stop = stops[index]

                        val distance = if (unit == "km") stop.distance else stop.distance * 0.621371
                        val progress = when {
                            index < currentStopIndex -> "Completed"
                            index == currentStopIndex -> "Reached"
                            else -> "Pending"
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${stop.name}: ${"%.2f".format(distance)} $unit - $progress",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            } else {
                Column {
                    stops.forEachIndexed { index, stop ->
                        val distance = if (unit == "km") stop.distance else stop.distance * 0.621371
                        val progress = when {
                            index < currentStopIndex -> "Completed"
                            index == currentStopIndex -> "Reached"
                            else -> "Pending"
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${stop.name}: ${"%.2f".format(distance)} $unit - $progress",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    JourneyTrackerTheme {
        App()
    }
}