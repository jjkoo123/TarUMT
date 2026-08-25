#include <iostream>
#include <vector>
#include "appointment_schedule.h"

using namespace std;

// Standalone testing main. In the complete team project, use the shared main.cpp
// and call appointmentScheduleMenu from its Appointment/Schedule menu option.
int main() {
    vector<Booking> bookings = {
        {"B001", "Aisha Rahman", "VAA 1234", "Oil Change", "Pending"},
        {"B002", "Lim Wei Jie", "WXY 5678", "Brake Inspection", "Pending"},
        {"B003", "Siti Nur", "BKM 8888", "Battery Replacement", "Pending"}
    };
    vector<Appointment> appointments;
    int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT];
    int nextAppointmentNumber = 1;

    initialiseSchedule(slotBookingCount);
    loadAppointmentsFromFile(appointments, bookings, slotBookingCount, nextAppointmentNumber);
    appointmentScheduleMenu(appointments, bookings, slotBookingCount, nextAppointmentNumber);
    cout << "Thank you for using the Car Workshop Management System.\n";
    return 0;
}
