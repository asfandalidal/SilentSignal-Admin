package com.azeemi.adminannouncement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azeemi.adminannouncement.domain.model.Announcement
import com.azeemi.adminannouncement.domain.model.AnnouncementRequest
import com.azeemi.adminannouncement.domain.usecase.CreateAnnouncementUseCase
import com.azeemi.adminannouncement.domain.usecase.DeleteAnnouncementUseCase
import com.azeemi.adminannouncement.domain.usecase.GetAnnouncementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val getAnnouncementsUseCase: GetAnnouncementsUseCase,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase
) : ViewModel() {

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _initialLoading = MutableStateFlow(true)
    val initialLoading: StateFlow<Boolean> = _initialLoading.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        fetchAnnouncements()
    }

    fun fetchAnnouncements(smooth: Boolean = false) {
        viewModelScope.launch {
            if (!smooth && _initialLoading.value) {
                _initialLoading.value = true // Only set once
            }

            val newAnnouncements = getAnnouncementsUseCase()
                .firstOrNull() ?: emptyList()

            if (newAnnouncements != _announcements.value) {
                _announcements.value = newAnnouncements
            }

            _initialLoading.value = false
        }
    }

    fun createAnnouncement(message: String, expiresAt: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                createAnnouncementUseCase(AnnouncementRequest(message, expiresAt, userId = 1))
                fetchAnnouncements(smooth = true) // Refresh list without UI lag
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Unknown error")
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteAnnouncement(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                deleteAnnouncementUseCase(id)
                fetchAnnouncements(smooth = true)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Failed to delete announcement")
            } finally {
                _loading.value = false
            }
        }
    }
}
