package com.azeemi.adminannouncement.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.azeemi.adminannouncement.presentation.viewmodel.AnnouncementViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            WarningBanner()
            Spacer(modifier = Modifier.height(12.dp))

            // Announcement Card Wrapper
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFFC107),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(
                            containerColor = Color.Black
                            )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnnouncementTextField(
                            value = message,
                            onMessageChange = { message = it }
                        )

                        ExpiresAtPicker(expiresAt) { expiresAt = it }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = {
                                    if (message.isNotEmpty() && expiresAt != null) {
                                        val formattedDateTime = expiresAt!!.format(
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                                        )
                                        viewModel.createAnnouncement(
                                            message,
                                            formattedDateTime,
                                            onSuccess = {
                                                viewModel.sendNotification(
                                                    title = "New Announcement",
                                                    body = message,
                                                    formattedDateTime
                                                )
                                                Toast.makeText(
                                                    context,
                                                    "Created!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                message = ""
                                                expiresAt = null
                                            },
                                            onError = {
                                                Toast.makeText(context, it, Toast.LENGTH_LONG)
                                                    .show()
                                            }
                                        )
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please enter message and expiry time.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD32F2F)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Text("Create Announcement", color = Color.White, fontSize = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (initialLoading) {
                            CircularProgressIndicator(color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
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
                            Toast.makeText(context, "Failed to delete: $it", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                }
            )
        }
    }
}
