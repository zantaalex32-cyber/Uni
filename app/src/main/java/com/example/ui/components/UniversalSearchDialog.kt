package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.*
import com.example.ui.theme.*

data class SearchResultItem(
    val title: String,
    val subtitle: String,
    val category: String,
    val icon: ImageVector,
    val itemType: String,
    val rawId: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalSearchDialog(
    units: List<UnitEntity>,
    classrooms: List<Classroom>,
    buildings: List<Building>,
    campusLocations: List<CampusLocation>,
    users: List<UserEntity>,
    schools: List<School>,
    onDismiss: () -> Unit,
    onNavigateToResult: (SearchResultItem) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val allSearchable = remember(units, classrooms, buildings, campusLocations, users, schools) {
        val list = mutableListOf<SearchResultItem>()

        units.forEach { u ->
            list.add(
                SearchResultItem(
                    title = "${u.code} - ${u.title}",
                    subtitle = "${u.departmentName} • ${u.credits} Credits",
                    category = "Course Unit",
                    icon = Icons.Default.MenuBook,
                    itemType = "UNIT",
                    rawId = u.id
                )
            )
        }

        classrooms.forEach { c ->
            list.add(
                SearchResultItem(
                    title = "${c.roomNumber} (${c.name})",
                    subtitle = "${c.roomType} • Floor ${c.floorLevel} • Capacity ${c.capacity}",
                    category = "Classroom / Lab",
                    icon = Icons.Default.MeetingRoom,
                    itemType = "CLASSROOM",
                    rawId = c.id
                )
            )
        }

        buildings.forEach { b ->
            list.add(
                SearchResultItem(
                    title = b.name,
                    subtitle = "${b.buildingType} • ${b.totalFloors} Floors",
                    category = "Campus Building",
                    icon = Icons.Default.Apartment,
                    itemType = "BUILDING",
                    rawId = b.id
                )
            )
        }

        campusLocations.forEach { loc ->
            list.add(
                SearchResultItem(
                    title = loc.name,
                    subtitle = "${loc.description} • Open ${loc.openingHours}",
                    category = "Facility / Landmark",
                    icon = Icons.Default.Place,
                    itemType = "FACILITY",
                    rawId = loc.id
                )
            )
        }

        users.filter { it.role == UserRole.LECTURER }.forEach { lec ->
            list.add(
                SearchResultItem(
                    title = lec.fullName,
                    subtitle = "${lec.titleOrDesignation} • ${lec.email}",
                    category = "Faculty & Lecturer",
                    icon = Icons.Default.Person,
                    itemType = "LECTURER",
                    rawId = lec.id
                )
            )
        }

        schools.forEach { s ->
            list.add(
                SearchResultItem(
                    title = s.name,
                    subtitle = "Dean: ${s.deanName} • Code: ${s.code}",
                    category = "School / Faculty",
                    icon = Icons.Default.School,
                    itemType = "SCHOOL",
                    rawId = s.id
                )
            )
        }

        list
    }

    val filteredResults = remember(query, allSearchable) {
        if (query.isBlank()) {
            allSearchable.take(10)
        } else {
            allSearchable.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.subtitle.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Top Search Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search units, labs, rooms, lecturers...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("universal_search_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (query.isBlank()) "Quick Suggestions & Campus Facilities" else "Found ${filteredResults.size} Results",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredResults) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToResult(item)
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Surface(
                                            color = MaterialTheme.colorScheme.surface,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = item.category,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(
                                    imageVector = Icons.Default.DirectionsWalk,
                                    contentDescription = "Navigate",
                                    tint = ZantaPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
