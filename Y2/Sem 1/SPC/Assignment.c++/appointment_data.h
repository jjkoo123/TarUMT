#ifndef APPOINTMENT_DATA_H
#define APPOINTMENT_DATA_H

#include <string>
using std::string;

const int WORKING_DAYS = 5;
const int TIME_SLOT_COUNT = 4;
const int MECHANIC_COUNT = 3;
const int MAX_APPOINTMENTS_PER_SLOT = 3;
const string APPOINTMENT_FILE = "appointments.txt";

const string WORKSHOP_DATES[WORKING_DAYS] = {
    "18/08/2026 (Monday)", "19/08/2026 (Tuesday)",
    "20/08/2026 (Wednesday)", "21/08/2026 (Thursday)",
    "22/08/2026 (Friday)"
};

const string TIME_SLOTS[TIME_SLOT_COUNT] = {
    "09:00 AM - 10:30 AM", "11:00 AM - 12:30 PM",
    "02:00 PM - 03:30 PM", "04:00 PM - 05:30 PM"
};

const string MECHANICS[MECHANIC_COUNT] = {
    "Ali Rahman", "Brandon Lee", "Chen Wei Ming"
};

// Received from Member 2: Service Booking Management.
struct Booking {
    string bookingId, customerName, plateNumber, serviceName, bookingStatus;
};

// Managed by Member 3: Appointment / Schedule Management.
struct Appointment {
    string appointmentId, bookingId, customerName, plateNumber, serviceName;
    int dateIndex, timeSlotIndex;
    string mechanicName, appointmentStatus;
};

#endif
