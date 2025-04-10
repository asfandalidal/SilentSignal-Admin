package com.azeemi.adminannouncement.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.azeemi.adminannouncement.R
import com.azeemi.adminannouncement.domain.model.Announcement
import com.commandiron.wheel_picker_compose.WheelDateTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WarningBanner() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 12.dp, bottom = 30.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        border = BorderStroke(1.dp, color = Color.Black)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚠️ Admin Access Only",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onDelete: (String) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd-MMM-yy h:mm a")
    val createdAtFormatted = convertUtcToLocal(announcement.createdAt) // Convert UTC to local
    val expiresAtFormatted = convertUtcToLocal(announcement.expiresAt)
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        border = BorderStroke(width = 1.dp, color = Color(0xFFFFC107))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📢 ${announcement.message}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "🕒 Created: $createdAtFormatted",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Text(
                text = "⏳ Expires: $expiresAtFormatted",
                color = Color.Red,
                fontSize = 13.sp
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDialog) {
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E1E1E), // Dark background
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFFFFC107),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Delete Announcement?",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Are you sure you want to delete?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFFA726))
                            ) {
                                Text(
                                    text = "Cancel",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            TextButton(
                                onClick = {
                                    onDelete(announcement.id.toString())
                                    showDialog = false
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                            ) {
                                Text(
                                    text = "Delete",
                                    style = MaterialTheme.typography.labelLarge
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
fun ExpiresAtPicker(
    expiresAt: LocalDateTime?,
    onDateSelected: (LocalDateTime) -> Unit
) {
    var showPickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Step 1: Always use the same value for minAllowedTime
    val minAllowedTime = remember { LocalDateTime.now().plusMinutes(1) }

    // Step 2: Ensure initial value is at least minAllowedTime
    var selectedDateTime by remember {
        mutableStateOf(
            if (expiresAt != null && expiresAt.isAfter(minAllowedTime)) expiresAt else minAllowedTime
        )
    }

    // Update selectedDateTime if expiresAt changes
    LaunchedEffect(expiresAt) {
        expiresAt?.let {
            if (it.isAfter(minAllowedTime)) selectedDateTime = it
            else selectedDateTime = minAllowedTime
        }
    }

    Surface(
        modifier = Modifier
            .width(260.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFA726))
            .clickable { showPickerDialog = true },
        color = Color(0xFFFFA726),
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.calender),
                contentDescription = "Calendar Icon",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Select Expiry Time",
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showPickerDialog) {
        Dialog(onDismissRequest = { showPickerDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Select Expiry Date & Time", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    WheelDateTimePicker(
                        startDateTime = selectedDateTime,
                        minDateTime = minAllowedTime,
                        maxDateTime = LocalDateTime.of(2100, 12, 31, 23, 59),
                        timeFormat = TimeFormat.AM_PM,
                        size = DpSize(300.dp, 120.dp),
                        rowCount = 5,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        textColor = Color(0xFFffc300),
                        selectorProperties = WheelPickerDefaults.selectorProperties(
                            enabled = true,
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFf1faee).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFFf1faee))
                        )
                    ) { snappedDateTime ->
                        selectedDateTime = snappedDateTime
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onDateSelected(selectedDateTime)
                            showPickerDialog = false
                            Toast.makeText(context, "Date & Time selected!", Toast.LENGTH_LONG)
                                .show()
                        },
                        enabled = selectedDateTime.isAfter(minAllowedTime),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFffc300))
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

    @Composable
    fun AnnouncementTextField(
        value: String,
        onMessageChange: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onMessageChange,
            label = {
                Text(
                    text = "Enter Announcement",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = false,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD32F2F),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFFFA726),
                unfocusedLabelColor = Color.LightGray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = Color(0xFF1E1E1E),
                focusedContainerColor = Color(0xFF1E1E1E)
            )
        )
    }


    // Function to convert UTC to local time
    fun convertUtcToLocal(utcTime: String): String {
        val instant = Instant.parse(utcTime) // Parse the UTC time (from the backend)
        val localZone = ZoneId.systemDefault() // Get the device's local time zone
        val localTime = instant.atZone(localZone) // Convert to local time

        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yy h:mm a") // Format for display
        return localTime.format(formatter)
    }


