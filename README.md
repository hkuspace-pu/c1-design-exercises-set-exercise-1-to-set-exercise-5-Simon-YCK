# c1-design-exercises-set-exercise-1-to-set-exercise-5-Simon-YCK
c1-design-exercises-set-exercise-1-to-set-exercise-5-Simon-YCK created by GitHub Classroom

# 🍽️ Restaurant Management App

This project is developed for the COMP2003HK assessment — a mobile application that helps a restaurant manage both **staff** and **guest** operations efficiently.

---
## 📱 Project Overview
The **Restaurant Management App** is a native Android application featuring a **modern, high-fidelity UI** designed for efficiency and visual appeal. This implementation goes beyond standard wireframes, incorporating trendy design elements like gradients, card views, and "pill-shaped" buttons to enhance the user experience for both Guests and Staff.

---

## ✨ "New UI" Features & Redesign (Exercise 5)

### 🎨 Modern Guest Dashboard (`activity_guest_dashboard.xml`)
*   **Visual Hierarchy:** Implemented a **Huge (160dp) "Book Table" Button** with a vibrant blue/purple gradient background to make the primary action unmistakable (solving Visibility issues).
*   **Secondary Actions:** "Browse Menu" is styled as a clean **Outline Button** (70dp) to differentiate it from the primary call-to-action.
*   **Notifications:** Added a **Notification Bell with Red Badge** icon in the top-right corner to simulate real-time alerts.
*   **Aesthetics:** Used a clean off-white background (`#F7FAFC`) with rounded bottom corners for the top header area.

### 👨‍🍳 Professional Staff Portal (`activity_staff_menu.xml`)
*   **Card-Based Layout:** Menu items are displayed in elevated white cards with rounded corners (`24dp`).
*   **Standardized Controls:** 
    *   **Edit Button:** Styled in **Blue (#667EEA)**.
    *   **Delete Button:** Styled in **Red (#E53E3E)**.
    *   *Improvement:* Both buttons are enforced to be **exact same size (48dp)** for visual consistency and Fitts' Law compliance.
*   **Image Handling:** Added a dedicated "Upload Image" placeholder area with a camera icon 📷.

### 🛡️ Safety & Error Prevention
*   **Custom Dialogs:** Replaced standard system alerts with **Custom XML Dialogs** (`dialog_delete_confirm.xml`).
    *   Features a scary **Red "Delete" button** and clear warning text: *"This action cannot be undone."*
*   **Reservation Management:** 
    *   **Guest:** Added a "Cancel Booking" button in the edit view.
    *   **Staff:** Replaced the "Edit" pencil with a **"Delete" Trash Can** for reservations, streamlining the cancellation workflow.

---

## 🛠️ Technical Stack
*   **Language:** Java
*   **Design System:** 
    *   **XML Layouts:** `ConstraintLayout` for complex positioning, `LinearLayout` for stacking.
    *   **Components:** `CardView`, `ScrollView`, Custom Drawables (Gradients, Rounded Shapes).
*   **Assets:** Vector Assets (Material Icons) + Emoji Text integration.

---

## 📂 Project Structure
app/src/main/
├── java/com/example/restaurantapp/
│ ├── LoginActivity.java # Modern login with gradient buttons
│ ├── GuestDashboardActivity.java # The "Big Button" UI logic
│ ├── StaffMenuActivity.java # Menu CRUD with custom dialogs
│ └── adapters/ # Custom adapters for Lists
│
└── res/layout/
├── activity_guest_dashboard.xml # 160dp Gradient Button Layout
├── item_menu_card_staff.xml # Staff card with Blue/Red buttons
├── dialog_delete_confirm.xml # Custom "Are you sure?" popup
└── item_reservation_ticket.xml # Reservation card layout

text

---

## 🚀 How to Run
1.  Clone this repository.
2.  Open in **Android Studio**.
3.  Sync Gradle.
4.  Run on an emulator (Pixel 6 recommended for best UI rendering).

---
**Note:** This project demonstrates a significant UI iteration from the initial low-fidelity sketches, moving towards a polished, market-ready visual style.
