package com.azeemi.adminannouncement.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.azeemi.adminannouncement.presentation.viewmodel.AnnouncementViewModel
import kotlinx.coroutines.delay
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


@Composable
fun AdminScreen(viewModel: AnnouncementViewModel = hiltViewModel()) {

    val announcements by viewModel.announcements.collectAsState()
    val initialLoading by viewModel.initialLoading.collectAsState()
    var message by remember { mutableStateOf("") }
    var expiresAt by remember { mutableStateOf<LocalDateTime?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.fetchAnnouncements(smooth = true)
            delay(10000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WarningBanner()
        Spacer(modifier = Modifier.height(20.dp))

        // Message Input Field
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Enter Message", color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Gray
            )
        )

        // Expiry Date & Time Picker
        expiresAt?.let { ExpiresAtPicker(it) { expiresAt = it } }

        Spacer(modifier = Modifier.height(12.dp))

        // Create Announcement Button
        Button(
            onClick = {
                if (message.isNotEmpty() && expiresAt != null) {
                    val formattedDateTime = expiresAt!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    viewModel.createAnnouncement(
                        message,
                        formattedDateTime,
                        onSuccess = {
                            // Send the notification after announcement creation
                            viewModel.sendNotification(
                                title = "New Announcement",
                                body = message
                            )

                            Toast.makeText(context, "Created!", Toast.LENGTH_SHORT).show()
                            message = ""
                            expiresAt = null
                        },
                        onError = { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    )
                } else {
                    Toast.makeText(context, "Please enter message and expiry time.", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Announcement", color = Color.White)
        }


        Spacer(modifier = Modifier.height(20.dp))

        // Loading Indicator (only initially)
        if (initialLoading) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Active Announcements
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(announcements) { announcement ->
                AnnouncementItem(
                    announcement = announcement,
                    onDelete = { id ->
                        val longId = id.toLongOrNull() ?: return@AnnouncementItem
                        viewModel.deleteAnnouncement(
                            longId.toString(),
                            onSuccess = {
                                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                                viewModel.fetchAnnouncements(smooth = true)
                            },
                            onError = {
                                Toast.makeText(context, "Failed to delete: $it", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                )

            }
        }
    }
}