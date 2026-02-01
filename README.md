# 🍽️ Restaurant Management System

**Course:** COMP2000HK - Software Engineering 2  
**Student:** Yang Chun Kit
**Status:** Complete (v1.0)

A comprehensive native Android application designed to streamline restaurant operations. This project implements a **Role-Based Access Control (RBAC)** system, allowing distinct functionalities for **Staff** (Administrative) and **Guests** (Customer Service).

---

## 📱 Project Overview

The application is built using the **MVC (Model-View-Controller)** architecture to ensure separation of concerns. It features a persistent local database (**SQLite**) and a high-fidelity UI designed with HCI principles (Fitts' Law, Visibility of System Status).

### 🎯 Key Roles & Features

#### 👨‍🍳 Staff Portal (Admin)
*   **Menu Management (CRUD):** Full capability to Add, Edit, and Delete menu items.
*   **Image Handling:** Custom logic to upload images from the device gallery and persist them to internal storage.
*   **Reservation Control:** View and manage guest bookings with status updates.
*   **Safety Mechanisms:** Custom XML Dialogs (`dialog_delete_confirm.xml`) prevent accidental data loss for destructive actions.

#### 🎨 Guest Dashboard (User)
*   **High-Visibility Actions:** 
    *   Primary "Book Table" action utilizes a **160dp gradient button** for immediate recognition.
    *   Secondary "Browse Menu" uses an outline style to establish visual hierarchy.
*   **Notification System:** Granular preference controls (Promotions vs. Updates) stored in the database.
*   **Real-time Feedback:** Visual cues like badges and toast messages confirm user actions.

---

## 🛠️ Technical Implementation

### Architecture
*   **Pattern:** MVC (Model-View-Controller)
*   **Database:** SQLite (Relational Data Model)
*   **Language:** Java
*   **Minimum SDK:** API 24 (Android 7.0)

### Design System
*   **Layouts:** `ConstraintLayout` for responsive positioning; `CardView` for content grouping.
*   **Styling:** Custom drawables for gradients and rounded corners (`pill-shaped` buttons).
*   **Assets:** Material Design Icons & Vector Assets.

---

## 📂 Project Structure

app/src/main/
├── java/com/example/restaurantapp/
│   ├── adapter/
│   │   ├── MenuAdapter.java          # Handles both Staff (Edit) and Guest (View) layouts
│   │   └── ReservationAdapter.java
│   ├── database/
│   │   └── DatabaseHelper.java       # Centralized SQLite Logic (Facade Pattern)
│   ├── model/
│   │   ├── MenuItem.java
│   │   └── Reservation.java
│   ├── utils/
│   │   └── NotificationHelper.java   # Notification logic with Preference checks
│   ├── GuestDashboardActivity.java
│   └── StaffDashboardActivity.java
│
└── res/layout/
    ├── activity_guest_dashboard.xml  # Primary Guest Interface
    ├── activity_staff_menu_editor.xml # Staff Management Interface
    ├── item_menu_card_staff.xml      # CardView with Edit/Delete controls
    ├── item_menu_card_guest.xml      # CardView optimized for viewing
    ├── dialog_delete_confirm.xml     # Custom Warning Dialog
    └── ... (other layout files)
