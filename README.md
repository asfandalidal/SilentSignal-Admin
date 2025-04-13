# 🛡️ AdminAnnouncementApp

**AdminAnnouncementApp** is the official Android admin panel for managing secure announcements in the **SilentSignalApp** ecosystem. It enables authorized users to create one-time announcements, define expiry times, manually delete messages, and monitor active announcements. It communicates with a **Spring Boot backend**, which handles announcement processing and delivery via Firebase.

---

## 🎯 Purpose

This app is designed for **trusted administrators** to securely push and manage sensitive, time-limited messages for users of **SilentSignalApp**. Useful for real-time alerts, internal announcements, or confidential notifications.

---

## 🚀 Features

- 📝 **Create Announcements**
  - Set message title, content, and expiry time with precision.

- ⏳ **Expiry Time Control**
  - Messages are automatically considered invalid after their expiry timestamp.

- 🗑️ **Manual Message Deletion**
  - Instantly revoke any active announcement.

- 🔒 **Backend-Secured Communication**
  - Talks to a **Spring Boot** backend that validates, stores, and routes messages.

- 📡 **Integrated with Firebase Cloud Messaging (FCM)**
  - The backend handles secure FCM delivery to **SilentSignalApp** clients.

- 📊 **Monitor Active Announcements**
  - View currently valid and active messages sent from the backend.

---

## 🛠️ Tech Stack

### 📱 Android (Frontend)
- **Kotlin**
- **Jetpack Compose**
- **MVVM Architecture**
- **Retrofit (HTTP client)**
- **Material Design 3**
- **Coroutines & StateFlow**

### 🖥️ Backend (Spring Boot)
- **Kotlin with Spring Boot**
- **RESTful APIs**
- **Firebase Admin SDK**
- **PostgreSQL**
- **Spring Security**

---

## 📱 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/41f91deb-6c5d-420c-a922-ad294d8f3d22" width="300" alt="Admin Screenshot 1"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/cb2eba6a-fc6e-429c-a616-705603775883" width="300" alt="Admin Screenshot 2"/>
  &nbsp;
  <img src="https://github.com/user-attachments/assets/7ba449f0-3b19-46f3-8743-0f038a84e9db" width="300" alt="Admin Screenshot 3"/>
</p>

---

## 🔧 Setup Instructions

### 🔹 Android App

1. **Clone the repository**
   ```bash
   git clone https://github.com/asfandalidal/AdminAnnouncementApp.git
   cd AdminAnnouncementApp
