# RemoteHealthMonitoringSystem

## Description

This project provides a solution for doctors and hospitals to remotely manage patients, handle appointments, monitor vitals, and deliver feedback and prescriptions in real time.

---

## User Roles

- Patient  
- Doctor  
- Admin  

---

## Key Features

### General Flow

- Users can enter the program as a Patient, Doctor, or Admin.
- Navigation is simple and guided via console-based integer options.

### Doctor Features

- View:
  - Appointment requests
  - Approved appointments
  - Today’s appointments
  - Associated patients and their vitals
- Approve or reject appointment requests
- Cancel booked appointments
- Provide feedback and prescriptions on current day's appointments only
- Receive emergency alerts from patients via Email and SMS
- Chat with patients
- Generate dummy Google Meet video call links

### Patient Features

- View:
  - Approved appointments
  - Assigned doctors
  - Feedback from doctors
- Request appointments from specific doctors
- Upload vitals during appointment requests:
  - Heart rate
  - Blood pressure
  - Oxygen level
  - Temperature
- Cancel booked appointments
- Trigger emergency alerts using the panic button
- Receive automated reminders for:
  - Prescription schedules (for medicines used under one month)
  - Appointments (a week before, three days before, and same day)
- Chat with doctors
- Access dummy video consultation links

---

## Emergency Alert System

Alerts are automatically sent to all doctors via Email and SMS when:

- The panic button is pressed by a patient
- Submitted vitals exceed critical thresholds

---

## Communication System

- One-to-one chat functionality between doctors and patients
- Dummy video consultation links (Google Meet format)

---

## Dummy Data Initialization

To simplify testing and meet assignment requirements:

- The system starts with predefined users:
  - 3 Admins
  - 3 Doctors
  - 3 Patients

Each user's ID, name, email, and phone number are displayed on console at program start. Users can log in using these credentials.

Note: The system is designed to be scalable to include:

- User registration and login
- Persistent storage (file or database)
- Graphical User Interface (JavaFX)

---

## User Experience

- Console-based interface
- No forced program termination without user consent
- All inputs are guided with clear prompts
- Input mismatches are handled gracefully with reusable utility methods
