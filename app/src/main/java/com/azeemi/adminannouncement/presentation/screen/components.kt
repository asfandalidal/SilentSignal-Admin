package com.azeemi.adminannouncement.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azeemi.adminannouncement.domain.model.Announcement
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun WarningBanner() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 20.dp), // Moves it slightly down
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚠️ Admin Access Only",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
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
    val createdAtFormatted = LocalDateTime.parse(announcement.createdAt).format(formatter)
    val expiresAtFormatted = LocalDateTime.parse(announcement.expiresAt).format(formatter)
    var showDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF222222)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp), // Compact padding
        elevation = CardDefaults.elevatedCardElevation(4.dp)
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
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Announcement?") },
            text = { Text("Are you sure you want to delete this announcement?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(announcement.id.toString())
                    showDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}



// Expiry Date and Time Picker
@Composable
fun ExpiresAtPicker(expiresAt: LocalDateTime, onDateSelected: (LocalDateTime) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Convert LocalDateTime to Calendar if available
    expiresAt.let {
        calendar.set(Calendar.YEAR, it.year)
        calendar.set(Calendar.MONTH, it.monthValue - 1)
        calendar.set(Calendar.DAY_OF_MONTH, it.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, it.hour)
        calendar.set(Calendar.MINUTE, it.minute)
    }

    var selectedDate by remember { mutableStateOf(expiresAt?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "Select Expiry Date") }

    Button(
        onClick = {
            val datePicker = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(context, { _, hour, minute ->
                    val selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute)
                    onDateSelected(selectedDateTime)
                    selectedDate = selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)

                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            datePicker.show()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)), // Orange Button
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(selectedDate, color = Color.White)
    }
}

fun showDateTimePicker(context: Context, onDateTimeSelected: (LocalDateTime) -> Unit) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            TimePickerDialog(context, { _, hour, minute ->
                val selectedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hour, minute)
                onDateTimeSelected(selectedDateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

