package com.example.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Building
import com.example.data.model.CampusLocation
import com.example.data.model.Classroom
import com.example.ui.components.CampusMapCanvas
import com.example.ui.components.RouteSheet
import com.example.ui.theme.*
import com.example.ui.viewmodel.ZantaUiState

@Composable
fun SmartCampusMapScreen(
    uiState: ZantaUiState,
    onSelectBuilding: (Building?) -> Unit,
    onSelectClassroom: (Classroom?) -> Unit,
    onGenerateRoute: (String, String) -> Unit,
    onClearRoute: () -> Unit
) {
    val buildings = uiState.buildings
    val locations = uiState.campusLocations
    val classrooms = uiState.classrooms
    val selectedBuilding = uiState.selectedBuilding
    val activeRoute = uiState.activeRoute

    var categoryFilter by remember { mutableStateOf("ALL") }

    val filteredBuildings = remember(categoryFilter, buildings) {
        if (categoryFilter == "ALL") buildings else {
            when (categoryFilter) {
                "Science" -> buildings.filter { it.buildingType == "Academic & Laboratories" }
                "Lecture" -> buildings.filter { it.buildingType == "Lecture Halls" }
                "ICT" -> buildings.filter { it.buildingType == "Technology & Computing" }
                "Library" -> buildings.filter { it.buildingType == "Library & Research" }
                else -> buildings
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Map Header & Search/Filter Strip
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Intelligent Campus Navigation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Interactive outdoor-to-indoor wayfinding & room finder",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("ALL", "Science", "Lecture", "ICT", "Library")) { cat ->
                        FilterChip(
                            selected = categoryFilter == cat,
                            onClick = { categoryFilter = cat },
                            label = {
                                Text(
                                    text = if (cat == "ALL") "All Facilities" else cat,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }
            }

            // Vector Campus Map Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                CampusMapCanvas(
                    buildings = filteredBuildings,
                    campusLocations = locations,
                    selectedBuilding = selectedBuilding,
                    activeRoute = activeRoute,
                    onSelectBuilding = { bld ->
                        onSelectBuilding(bld)
                    },
                    onSelectLocation = { loc ->
                        onGenerateRoute("Main Gate", loc.name)
                    },
                    modifier = Modifier.fillMaxSize().testTag("campus_map_canvas")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Building Details Bottom Sheet (when building selected and no route active)
        if (selectedBuilding != null && activeRoute == null) {
            val buildingRooms = classrooms.filter { it.buildingId == selectedBuilding.id }

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .testTag("building_detail_sheet"),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedBuilding.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "${selectedBuilding.buildingType} • ${selectedBuilding.totalFloors} Floors",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(onClick = { onSelectBuilding(null) }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Classrooms & Laboratories (${buildingRooms.size}):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    buildingRooms.forEach { room ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${room.roomNumber}: ${room.name}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Floor ${room.floorLevel} • Capacity ${room.capacity} • ${room.roomType}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = {
                                    onGenerateRoute("Main Gate", room.name)
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ZantaPrimary)
                            ) {
                                Icon(Icons.Default.DirectionsWalk, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Navigate", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onGenerateRoute("Main Gate", selectedBuilding.name) },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text("Navigate to Building Entrance", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active Route Overlay
        if (activeRoute != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                RouteSheet(
                    route = activeRoute,
                    onClose = onClearRoute
                )
            }
        }
    }
}
