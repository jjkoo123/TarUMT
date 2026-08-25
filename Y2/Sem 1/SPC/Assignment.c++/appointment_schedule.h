#ifndef APPOINTMENT_SCHEDULE_H
#define APPOINTMENT_SCHEDULE_H

#include <vector>
#include "appointment_data.h"

// The 2D array stores the number of reserved workshop bays for each date/slot.
void initialiseSchedule(int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT]);

// Saves and restores appointment data in appointments.txt.
void saveAppointmentsToFile(const std::vector<Appointment>& appointments);
void loadAppointmentsFromFile(std::vector<Appointment>& appointments,
                              std::vector<Booking>& bookings,
                              int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT],
                              int& nextAppointmentNumber);

// The public function called from the team's shared main menu.
void appointmentScheduleMenu(std::vector<Appointment>& appointments,
                             std::vector<Booking>& bookings,
                             int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT],
                             int& nextAppointmentNumber);

#endif
