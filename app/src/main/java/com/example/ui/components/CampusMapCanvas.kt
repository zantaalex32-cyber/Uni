package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Building
import com.example.data.model.CampusLocation
import com.example.ui.theme.*
import com.example.ui.viewmodel.CampusRoute

@OptIn(ExperimentalTextApi::class)
@Composable
fun CampusMapCanvas(
    buildings: List<Building>,
    campusLocations: List<CampusLocation>,
    selectedBuilding: Building?,
    activeRoute: CampusRoute?,
    onSelectBuilding: (Building) -> Unit,
    onSelectLocation: (CampusLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1.0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val textMeasurer = rememberTextMeasurer()

    // Pulse animation for active route destination & user location
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE2E8F0))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.7f, 3.5f)
                    offset = Offset(
                        x = (offset.x + pan.x).coerceIn(-600f, 600f),
                        y = (offset.y + pan.y).coerceIn(-600f, 600f)
                    )
                }
            }
            .pointerInput(buildings, campusLocations) {
                detectTapGestures { tapOffset ->
                    // Transform tap coordinates to normalized map space
                    val mapWidth = size.width * scale
                    val mapHeight = size.height * scale
                    val originX = (size.width - mapWidth) / 2 + offset.x
                    val originY = (size.height - mapHeight) / 2 + offset.y

                    val normX = (tapOffset.x - originX) / mapWidth
                    val normY = (tapOffset.y - originY) / mapHeight

                    // Check if a building was tapped
                    val tappedBuilding = buildings.firstOrNull { b ->
                        val bx = b.mapXRatio
                        val by = b.mapYRatio
                        kotlin.math.abs(bx - normX) < 0.08f && kotlin.math.abs(by - normY) < 0.08f
                    }

                    if (tappedBuilding != null) {
                        onSelectBuilding(tappedBuilding)
                    } else {
                        val tappedLoc = campusLocations.firstOrNull { loc ->
                            kotlin.math.abs(loc.mapXRatio - normX) < 0.06f && kotlin.math.abs(loc.mapYRatio - normY) < 0.06f
                        }
                        if (tappedLoc != null) {
                            onSelectLocation(tappedLoc)
                        }
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            val mapW = canvasW * scale
            val mapH = canvasH * scale
            val originX = (canvasW - mapW) / 2 + offset.x
            val originY = (canvasH - mapH) / 2 + offset.y

            // 1. Draw Campus Grass Background
            drawRect(
                color = Color(0xFFE8F5E9),
                topLeft = Offset(originX, originY),
                size = Size(mapW, mapH)
            )

            // 2. Draw Campus Roads & Pathways
            val roadColor = Color(0xFFCBD5E1)
            val pathColor = Color(0xFFE2E8F0)
            val roadWidth = 24f * scale
            val pathWidth = 10f * scale

            // Main Perimeter Ring Road
            drawRoundRect(
                color = roadColor,
                topLeft = Offset(originX + mapW * 0.08f, originY + mapH * 0.12f),
                size = Size(mapW * 0.84f, mapH * 0.76f),
                cornerRadius = CornerRadius(40f * scale, 40f * scale),
                style = Stroke(width = roadWidth)
            )

            // Central Avenue & Crosswalks
            drawLine(
                color = roadColor,
                start = Offset(originX + mapW * 0.12f, originY + mapH * 0.20f),
                end = Offset(originX + mapW * 0.88f, originY + mapH * 0.80f),
                strokeWidth = pathWidth
            )
            drawLine(
                color = roadColor,
                start = Offset(originX + mapW * 0.50f, originY + mapH * 0.12f),
                end = Offset(originX + mapW * 0.50f, originY + mapH * 0.88f),
                strokeWidth = pathWidth
            )
            drawLine(
                color = roadColor,
                start = Offset(originX + mapW * 0.10f, originY + mapH * 0.50f),
                end = Offset(originX + mapW * 0.90f, originY + mapH * 0.50f),
                strokeWidth = pathWidth
            )

            // Central University Fountain / Quadrangle
            drawCircle(
                color = Color(0xFF93C5FD),
                center = Offset(originX + mapW * 0.50f, originY + mapH * 0.50f),
                radius = 28f * scale
            )
            drawCircle(
                color = Color(0xFF2563EB),
                center = Offset(originX + mapW * 0.50f, originY + mapH * 0.50f),
                radius = 12f * scale
            )

            // 3. Draw Campus Locations & Gates
            campusLocations.forEach { loc ->
                val lx = originX + mapW * loc.mapXRatio
                val ly = originY + mapH * loc.mapYRatio

                val pinColor = when (loc.category) {
                    "Gate" -> Color(0xFF0F172A)
                    "Health" -> Color(0xFFDC2626)
                    "Sports" -> Color(0xFF16A34A)
                    "Hostel" -> Color(0xFF9333EA)
                    else -> Color(0xFF475569)
                }

                drawCircle(
                    color = pinColor,
                    center = Offset(lx, ly),
                    radius = 12f * scale
                )
                drawCircle(
                    color = Color.White,
                    center = Offset(lx, ly),
                    radius = 5f * scale
                )

                // Label
                val textLayout = textMeasurer.measure(
                    text = AnnotatedString(loc.name.take(16)),
                    style = TextStyle(
                        fontSize = (9 * scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                )
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(lx - textLayout.size.width / 2, ly + 14f * scale)
                )
            }

            // 4. Draw Campus Buildings
            buildings.forEach { bld ->
                val bx = originX + mapW * bld.mapXRatio
                val by = originY + mapH * bld.mapYRatio
                val bWidth = 64f * scale
                val bHeight = 48f * scale

                val isSelected = selectedBuilding?.id == bld.id
                val buildingColor = when (bld.id) {
                    "bld_science" -> Color(0xFF2563EB)
                    "bld_lecture_theatre" -> Color(0xFF7C3AED)
                    "bld_ict" -> Color(0xFF0D9488)
                    "bld_library" -> Color(0xFFD97706)
                    "bld_admin" -> Color(0xFF475569)
                    else -> Color(0xFFEA580C)
                }

                // Building Drop Shadow
                drawRoundRect(
                    color = Color(0x33000000),
                    topLeft = Offset(bx - bWidth / 2 + 4f, by - bHeight / 2 + 6f),
                    size = Size(bWidth, bHeight),
                    cornerRadius = CornerRadius(8f * scale, 8f * scale)
                )

                // Building Block
                drawRoundRect(
                    color = if (isSelected) Color(0xFFFBBF24) else buildingColor,
                    topLeft = Offset(bx - bWidth / 2, by - bHeight / 2),
                    size = Size(bWidth, bHeight),
                    cornerRadius = CornerRadius(8f * scale, 8f * scale),
                    style = if (isSelected) Stroke(width = 4f * scale) else androidx.compose.ui.graphics.drawscope.Fill
                )

                if (isSelected) {
                    drawRoundRect(
                        color = buildingColor.copy(alpha = 0.9f),
                        topLeft = Offset(bx - bWidth / 2, by - bHeight / 2),
                        size = Size(bWidth, bHeight),
                        cornerRadius = CornerRadius(8f * scale, 8f * scale)
                    )
                }

                // Building Code
                val codeLayout = textMeasurer.measure(
                    text = AnnotatedString(bld.code),
                    style = TextStyle(
                        fontSize = (11 * scale).sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
                drawText(
                    textLayoutResult = codeLayout,
                    topLeft = Offset(bx - codeLayout.size.width / 2, by - codeLayout.size.height / 2)
                )

                // Building Full Name under
                val nameLayout = textMeasurer.measure(
                    text = AnnotatedString(bld.name),
                    style = TextStyle(
                        fontSize = (9 * scale).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                )
                drawText(
                    textLayoutResult = nameLayout,
                    topLeft = Offset(bx - nameLayout.size.width / 2, by + bHeight / 2 + 4f)
                )
            }

            // 5. Draw Active Route if Present
            activeRoute?.let { route ->
                val points = route.pathPoints.map { (px, py) ->
                    Offset(originX + mapW * px, originY + mapH * py)
                }

                if (points.size >= 2) {
                    val path = Path()
                    path.moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        path.lineTo(points[i].x, points[i].y)
                    }

                    // Route casing glow
                    drawPath(
                        path = path,
                        color = Color(0x663B82F6),
                        style = Stroke(
                            width = 12f * scale,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    // Route dashed blue line
                    drawPath(
                        path = path,
                        color = Color(0xFF1D4ED8),
                        style = Stroke(
                            width = 6f * scale,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f * scale, 12f * scale), 0f),
                            cap = StrokeCap.Round
                        )
                    )

                    // Start Waypoint (User Current Location at Main Gate)
                    val startPt = points.first()
                    drawCircle(
                        color = Color(0xFF3B82F6).copy(alpha = pulseAlpha),
                        center = startPt,
                        radius = pulseRadius * scale
                    )
                    drawCircle(
                        color = Color(0xFF1D4ED8),
                        center = startPt,
                        radius = 8f * scale
                    )
                    drawCircle(
                        color = Color.White,
                        center = startPt,
                        radius = 4f * scale
                    )

                    // Destination Waypoint (Target Classroom/Building)
                    val endPt = points.last()
                    drawCircle(
                        color = Color(0xFFEF4444).copy(alpha = pulseAlpha),
                        center = endPt,
                        radius = pulseRadius * scale * 1.2f
                    )
                    drawCircle(
                        color = Color(0xFFDC2626),
                        center = endPt,
                        radius = 9f * scale
                    )
                    drawCircle(
                        color = Color.White,
                        center = endPt,
                        radius = 4f * scale
                    )
                }
            }
        }

        // Floating Map Controls (Zoom In, Zoom Out, Reset, Compass)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallFloatingActionButton(
                onClick = { scale = (scale * 1.3f).coerceAtMost(3.5f) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("zoom_in_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }

            SmallFloatingActionButton(
                onClick = { scale = (scale / 1.3f).coerceAtLeast(0.7f) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("zoom_out_button")
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }

            SmallFloatingActionButton(
                onClick = {
                    scale = 1.0f
                    offset = Offset.Zero
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("reset_map_button")
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Center Map")
            }
        }

        // Map Legend / Scale Indicator
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ZantaPrimary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Kisii Main Campus", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("• 1:5000", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
