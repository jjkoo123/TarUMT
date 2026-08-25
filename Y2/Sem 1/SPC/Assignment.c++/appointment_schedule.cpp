#include <cstdlib>
#include <cctype>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <limits>
#include <sstream>
#include <vector>
#include "appointment_schedule.h"

using namespace std;

// Automatically chooses cls on Windows and clear on macOS/Linux.
void clearScreen() {
#ifdef _WIN32
    system("cls");
#else
    system("clear");
#endif
}

// Removes invalid keyboard input after a failed cin operation.
void clearInputBuffer() {
    cin.clear();
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
}

// Validates integer menu choices, dates, time slots and mechanic choices.
int readNumber(const string& prompt, int minimum, int maximum) {
    int value;
    while (true) {
        cout << prompt;
        cin >> value;
        if (cin.fail() || value < minimum || value > maximum) {
            cout << "Invalid input. Enter a number from " << minimum << " to " << maximum << ".\n";
            clearInputBuffer();
        } else {
            clearInputBuffer();
            return value;
        }
    }
}

// Returns a non-empty booking ID or appointment ID entered by the user.
string readNonEmptyText(const string& prompt) {
    string text;
    do {
        cout << prompt;
        getline(cin, text);
        if (text.empty()) cout << "Input cannot be empty.\n";
    } while (text.empty());
    return text;
}

void printLine(char character, int length) {
    for (int i = 0; i < length; i++) cout << character;
    cout << '\n';
}

// Converts an ID to uppercase so ID comparisons accept either lower- or uppercase input.
// Example: a1 becomes A1 and b001 becomes B001.
string convertToUppercase(string text) {
    for (int i = 0; i < static_cast<int>(text.length()); i++) {
        text[i] = static_cast<char>(toupper(static_cast<unsigned char>(text[i])));
    }
    return text;
}

// Initialises the required 2D array. 0 means no workshop bays are reserved.
void initialiseSchedule(int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT]) {
    for (int day = 0; day < WORKING_DAYS; day++)
        for (int slot = 0; slot < TIME_SLOT_COUNT; slot++)
            slotBookingCount[day][slot] = 0;
}

// Searches the booking vector. Returns -1 when the ID is absent.
int findBookingIndex(const vector<Booking>& bookings, const string& bookingId) {
    string formattedBookingId = convertToUppercase(bookingId);
    for (int i = 0; i < static_cast<int>(bookings.size()); i++)
        if (convertToUppercase(bookings[i].bookingId) == formattedBookingId) return i;
    return -1;
}

// Searches the appointment vector. Returns -1 when the ID is absent.
int findAppointmentIndex(const vector<Appointment>& appointments, const string& appointmentId) {
    string formattedAppointmentId = convertToUppercase(appointmentId);
    for (int i = 0; i < static_cast<int>(appointments.size()); i++)
        if (convertToUppercase(appointments[i].appointmentId) == formattedAppointmentId) return i;
    return -1;
}

// Stores appointment records using | as a separator, for example A1|B001|...
void saveAppointmentsToFile(const vector<Appointment>& appointments) {
    ofstream outputFile(APPOINTMENT_FILE.c_str());
    if (!outputFile) {
        cout << "Warning: Unable to save " << APPOINTMENT_FILE << ".\n";
        return;
    }
    for (int i = 0; i < static_cast<int>(appointments.size()); i++) {
        const Appointment& a = appointments[i];
        outputFile << a.appointmentId << '|' << a.bookingId << '|' << a.customerName << '|'
                   << a.plateNumber << '|' << a.serviceName << '|' << a.dateIndex << '|'
                   << a.timeSlotIndex << '|' << a.mechanicName << '|'
                   << a.appointmentStatus << '\n';
    }
}

// Loads saved appointments and rebuilds the 2D schedule array at program startup.
void loadAppointmentsFromFile(vector<Appointment>& appointments, vector<Booking>& bookings,
                              int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT],
                              int& nextAppointmentNumber) {
    ifstream inputFile(APPOINTMENT_FILE.c_str());
    if (!inputFile) return; // First run: text file does not exist yet.

    string line;
    while (getline(inputFile, line)) {
        string fields[9];
        stringstream record(line);
        bool valid = true;
        for (int i = 0; i < 9; i++) {
            if (!getline(record, fields[i], '|')) { valid = false; break; }
        }
        if (!valid) continue;

        Appointment a;
        try {
            a.appointmentId = fields[0]; a.bookingId = fields[1];
            a.customerName = fields[2]; a.plateNumber = fields[3];
            a.serviceName = fields[4]; a.dateIndex = stoi(fields[5]);
            a.timeSlotIndex = stoi(fields[6]); a.mechanicName = fields[7];
            a.appointmentStatus = fields[8];
        } catch (...) { continue; }

        if (a.dateIndex < 0 || a.dateIndex >= WORKING_DAYS ||
            a.timeSlotIndex < 0 || a.timeSlotIndex >= TIME_SLOT_COUNT) continue;

        appointments.push_back(a);
        if (a.appointmentStatus != "Cancelled")
            slotBookingCount[a.dateIndex][a.timeSlotIndex]++;

        int bookingIndex = findBookingIndex(bookings, a.bookingId);
        if (bookingIndex != -1) bookings[bookingIndex].bookingStatus = a.appointmentStatus;

        try {
            int savedNumber = stoi(a.appointmentId.substr(1));
            if (savedNumber >= nextAppointmentNumber) nextAppointmentNumber = savedNumber + 1;
        } catch (...) { }
    }
}

// Stops a booking from being scheduled twice unless the old appointment was cancelled.
bool bookingHasActiveAppointment(const vector<Appointment>& appointments, const string& bookingId) {
    for (int i = 0; i < static_cast<int>(appointments.size()); i++)
        if (appointments[i].bookingId == bookingId && appointments[i].appointmentStatus != "Cancelled")
            return true;
    return false;
}

// Verifies the mechanic is not assigned to another active appointment at that time.
bool mechanicIsAvailable(const vector<Appointment>& appointments, int day, int slot,
                         const string& mechanic, int ignoredIndex) {
    for (int i = 0; i < static_cast<int>(appointments.size()); i++)
        if (i != ignoredIndex && appointments[i].dateIndex == day &&
            appointments[i].timeSlotIndex == slot && appointments[i].mechanicName == mechanic &&
            appointments[i].appointmentStatus != "Cancelled") return false;
    return true;
}

void displayBookings(const vector<Booking>& bookings) {
    cout << "\nPENDING SERVICE BOOKINGS\n";
    printLine('-', 72);
    cout << left << setw(5) << "No." << setw(12) << "Booking ID" << setw(22) << "Customer"
         << setw(16) << "Plate" << "Service\n";
    printLine('-', 72);
    bool found = false;
    int number = 1;
    for (int i = 0; i < static_cast<int>(bookings.size()); i++)
        if (bookings[i].bookingStatus == "Pending") {
            cout << left << setw(5) << number++ << setw(12) << bookings[i].bookingId
                 << setw(22) << bookings[i].customerName
                 << setw(16) << bookings[i].plateNumber << bookings[i].serviceName << '\n';
            found = true;
        }
    if (!found) cout << "No pending bookings.\n";
    cout << "0. Back to Appointment Menu\n";
    printLine('-', 72);
}

// Lets users select a pending booking by number, avoiding errors such as b001/B001.
// 0 returns -1 so the caller can return to the previous menu.
int chooseBookingFromList(const vector<Booking>& bookings) {
    vector<int> pendingIndexes;
    for (int i = 0; i < static_cast<int>(bookings.size()); i++)
        if (bookings[i].bookingStatus == "Pending") pendingIndexes.push_back(i);

    if (pendingIndexes.empty()) {
        cout << "No pending booking is available.\n";
        return -1;
    }

    displayBookings(bookings);
    int choice = readNumber("Choose booking number: ", 0,
                            static_cast<int>(pendingIndexes.size()));
    if (choice == 0) return -1;
    return pendingIndexes[choice - 1];
}

// Shows appointment records in a table. It is used before the user chooses
// an appointment to reschedule, cancel, or update its status.
void displayAppointmentsTable(const vector<Appointment>& appointments, bool activeOnly) {
    cout << "\nAPPOINTMENT LIST\n";
    printLine('-', 128);
    cout << left << setw(5) << "No."
         << setw(12) << "Appt ID"
         << setw(12) << "Booking ID"
         << setw(20) << "Customer"
         << setw(15) << "Plate No."
         << setw(25) << "Date"
         << setw(21) << "Time Slot"
         << setw(18) << "Mechanic"
         << "Status\n";
    printLine('-', 128);

    int number = 1;
    bool found = false;
    for (int i = 0; i < static_cast<int>(appointments.size()); i++) {
        if (!activeOnly || appointments[i].appointmentStatus != "Cancelled") {
            cout << left << setw(5) << number++
                 << setw(12) << appointments[i].appointmentId
                 << setw(12) << appointments[i].bookingId
                 << setw(20) << appointments[i].customerName
                 << setw(15) << appointments[i].plateNumber
                 << setw(25) << WORKSHOP_DATES[appointments[i].dateIndex]
                 << setw(21) << TIME_SLOTS[appointments[i].timeSlotIndex]
                 << setw(18) << appointments[i].mechanicName
                 << appointments[i].appointmentStatus << '\n';
            found = true;
        }
    }
    if (!found) cout << "No appointment record is available.\n";
    printLine('-', 128);
}

// Lets the user select an active appointment by table number instead of typing an ID.
// Returns the real vector index, or -1 when there is no active appointment.
int chooseAppointmentFromList(const vector<Appointment>& appointments) {
    vector<int> activeIndexes;
    for (int i = 0; i < static_cast<int>(appointments.size()); i++)
        if (appointments[i].appointmentStatus != "Cancelled") activeIndexes.push_back(i);

    if (activeIndexes.empty()) {
        cout << "No active appointment is available.\n";
        return -1;
    }

    displayAppointmentsTable(appointments, true);
    cout << "0. Back to Appointment Menu\n";
    int choice = readNumber("Choose appointment number: ", 0,
                            static_cast<int>(activeIndexes.size()));
    if (choice == 0) return -1;
    return activeIndexes[choice - 1];
}

// Uses pass-by-reference to return a validated date, slot and mechanic selection.
bool chooseScheduleDetails(const vector<Appointment>& appointments,
                           const int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT],
                           int ignoredIndex, int& selectedDay, int& selectedSlot,
                           string& selectedMechanic) {
    cout << "\nChoose appointment date\n";
    cout << "0. Back to Appointment Menu\n";
    for (int i = 0; i < WORKING_DAYS; i++) cout << i + 1 << ". " << WORKSHOP_DATES[i] << '\n';
    int dateChoice = readNumber("Date choice: ", 0, WORKING_DAYS);
    if (dateChoice == 0) return false;
    selectedDay = dateChoice - 1;

    cout << "\nChoose time slot\n";
    cout << "0. Back to Appointment Menu\n";
    for (int i = 0; i < TIME_SLOT_COUNT; i++)
        cout << i + 1 << ". " << TIME_SLOTS[i] << " (" << slotBookingCount[selectedDay][i]
             << "/" << MAX_APPOINTMENTS_PER_SLOT << " bays reserved)\n";
    int slotChoice = readNumber("Time-slot choice: ", 0, TIME_SLOT_COUNT);
    if (slotChoice == 0) return false;
    selectedSlot = slotChoice - 1;
    if (slotBookingCount[selectedDay][selectedSlot] >= MAX_APPOINTMENTS_PER_SLOT) {
        cout << "All workshop bays are occupied.\n";
        return false;
    }

    cout << "\nChoose mechanic\n";
    cout << "0. Back to Appointment Menu\n";
    for (int i = 0; i < MECHANIC_COUNT; i++)
        cout << i + 1 << ". " << MECHANICS[i] << " - "
             << (mechanicIsAvailable(appointments, selectedDay, selectedSlot, MECHANICS[i], ignoredIndex)
                 ? "Available" : "Not available") << '\n';
    int mechanicChoice = readNumber("Mechanic choice: ", 0, MECHANIC_COUNT);
    if (mechanicChoice == 0) return false;
    selectedMechanic = MECHANICS[mechanicChoice - 1];
    if (!mechanicIsAvailable(appointments, selectedDay, selectedSlot, selectedMechanic, ignoredIndex)) {
        cout << "Selected mechanic is not available.\n";
        return false;
    }
    return true;
}

void printAppointmentSlip(const Appointment& a) {
    printLine('=', 55);
    cout << "             APPOINTMENT CONFIRMATION SLIP\n";
    printLine('=', 55);
    cout << left << setw(22) << "Appointment ID:" << a.appointmentId << '\n'
         << setw(22) << "Booking ID:" << a.bookingId << '\n'
         << setw(22) << "Customer:" << a.customerName << '\n'
         << setw(22) << "Vehicle Plate:" << a.plateNumber << '\n'
         << setw(22) << "Service:" << a.serviceName << '\n'
         << setw(22) << "Date:" << WORKSHOP_DATES[a.dateIndex] << '\n'
         << setw(22) << "Time:" << TIME_SLOTS[a.timeSlotIndex] << '\n'
         << setw(22) << "Mechanic:" << a.mechanicName << '\n'
         << setw(22) << "Status:" << a.appointmentStatus << '\n';
    printLine('=', 55);
}

// Creates an appointment and updates the shared booking data and the 2D array.
void createAppointment(vector<Appointment>& appointments, vector<Booking>& bookings,
                       int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT], int& nextId) {
    int bookingIndex = chooseBookingFromList(bookings);
    if (bookingIndex == -1) return;
    if (bookingHasActiveAppointment(appointments, bookings[bookingIndex].bookingId)) {
        cout << "This booking already has an active appointment.\n";
        return;
    }
    int day, slot; string mechanic;
    if (!chooseScheduleDetails(appointments, slotBookingCount, -1, day, slot, mechanic)) return;

    Appointment a = {"A" + to_string(nextId++), bookings[bookingIndex].bookingId,
        bookings[bookingIndex].customerName, bookings[bookingIndex].plateNumber,
        bookings[bookingIndex].serviceName, day, slot, mechanic, "Scheduled"};
    appointments.push_back(a);
    slotBookingCount[day][slot]++;
    bookings[bookingIndex].bookingStatus = "Scheduled";
    saveAppointmentsToFile(appointments);
    cout << "Appointment created successfully.\n";
    printAppointmentSlip(a);
}

// Displays the active schedule for one selected day.
void viewDailySchedule(const vector<Appointment>& appointments,
                       const int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT]) {
    cout << "\nChoose date to view\n";
    cout << "0. Back to Appointment Menu\n";
    for (int i = 0; i < WORKING_DAYS; i++) cout << i + 1 << ". " << WORKSHOP_DATES[i] << '\n';
    int dateChoice = readNumber("Date choice: ", 0, WORKING_DAYS);
    if (dateChoice == 0) return;
    int day = dateChoice - 1;
    cout << "\nDAILY SCHEDULE - " << WORKSHOP_DATES[day] << '\n';
    printLine('-', 115);
    cout << left << setw(22) << "Time Slot"
         << setw(12) << "Appt ID"
         << setw(12) << "Booking ID"
         << setw(20) << "Customer"
         << setw(15) << "Plate No."
         << setw(18) << "Mechanic"
         << "Status\n";
    printLine('-', 115);
    for (int slot = 0; slot < TIME_SLOT_COUNT; slot++) {
        bool found = false;
        for (int i = 0; i < static_cast<int>(appointments.size()); i++)
            if (appointments[i].dateIndex == day && appointments[i].timeSlotIndex == slot &&
                appointments[i].appointmentStatus != "Cancelled") {
                cout << left << setw(22) << (found ? "" : TIME_SLOTS[slot])
                     << setw(12) << appointments[i].appointmentId
                     << setw(12) << appointments[i].bookingId
                     << setw(20) << appointments[i].customerName
                     << setw(15) << appointments[i].plateNumber
                     << setw(18) << appointments[i].mechanicName
                     << appointments[i].appointmentStatus << '\n';
                found = true;
            }
        if (!found)
            cout << left << setw(22) << TIME_SLOTS[slot]
                 << "Available\n";
    }
    int total = 0;
    for (int slot = 0; slot < TIME_SLOT_COUNT; slot++) total += slotBookingCount[day][slot];
    printLine('-', 115);
    cout << "Total active appointments: " << total << '\n';
}

// Releases the previous slot and creates a new valid schedule selection.
void rescheduleAppointment(vector<Appointment>& appointments,
                           int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT]) {
    int index = chooseAppointmentFromList(appointments);
    if (index == -1 || appointments[index].appointmentStatus == "Cancelled" ||
        appointments[index].appointmentStatus == "Completed") {
        cout << "Only an active appointment can be rescheduled.\n";
        return;
    }
    int oldDay = appointments[index].dateIndex, oldSlot = appointments[index].timeSlotIndex;
    slotBookingCount[oldDay][oldSlot]--;
    int day, slot; string mechanic;
    if (!chooseScheduleDetails(appointments, slotBookingCount, index, day, slot, mechanic)) {
        slotBookingCount[oldDay][oldSlot]++;
        return;
    }
    appointments[index].dateIndex = day;
    appointments[index].timeSlotIndex = slot;
    appointments[index].mechanicName = mechanic;
    appointments[index].appointmentStatus = "Rescheduled";
    slotBookingCount[day][slot]++;
    saveAppointmentsToFile(appointments);
    cout << "Appointment rescheduled successfully.\n";
    printAppointmentSlip(appointments[index]);
}

// Cancels an appointment and makes its related booking available again.
void cancelAppointment(vector<Appointment>& appointments, vector<Booking>& bookings,
                       int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT]) {
    int index = chooseAppointmentFromList(appointments);
    if (index == -1 || appointments[index].appointmentStatus == "Cancelled" ||
        appointments[index].appointmentStatus == "Completed") {
        cout << "Only an active appointment can be cancelled.\n";
        return;
    }
    appointments[index].appointmentStatus = "Cancelled";
    slotBookingCount[appointments[index].dateIndex][appointments[index].timeSlotIndex]--;
    int bookingIndex = findBookingIndex(bookings, appointments[index].bookingId);
    if (bookingIndex != -1) bookings[bookingIndex].bookingStatus = "Pending";
    saveAppointmentsToFile(appointments);
    cout << "Appointment cancelled successfully.\n";
}

// Updates Appointment and Booking status together for module integration.
void updateAppointmentStatus(vector<Appointment>& appointments, vector<Booking>& bookings) {
    int index = chooseAppointmentFromList(appointments);
    if (index == -1 || appointments[index].appointmentStatus == "Cancelled") {
        cout << "Active appointment ID was not found.\n";
        return;
    }
    cout << "1. Scheduled\n2. In Service\n3. Completed\n";
    string statuses[] = {"Scheduled", "In Service", "Completed"};
    cout << "0. Back to Appointment Menu\n";
    int statusChoice = readNumber("New status: ", 0, 3);
    if (statusChoice == 0) return;
    string status = statuses[statusChoice - 1];
    appointments[index].appointmentStatus = status;
    int bookingIndex = findBookingIndex(bookings, appointments[index].bookingId);
    if (bookingIndex != -1) bookings[bookingIndex].bookingStatus = status;
    saveAppointmentsToFile(appointments);
    cout << "Appointment status updated successfully.\n";
}

// do-while repeats the submenu; switch controls which function is called.
void appointmentScheduleMenu(vector<Appointment>& appointments, vector<Booking>& bookings,
                             int slotBookingCount[WORKING_DAYS][TIME_SLOT_COUNT], int& nextId) {
    int choice;
    do {
        clearScreen();
        cout << "\n=== APPOINTMENT / SCHEDULE MANAGEMENT ===\n"
             << "1. Create appointment\n2. View daily schedule\n3. Reschedule appointment\n"
             << "4. Cancel appointment\n5. Update appointment status\n6. Return to main menu\n";
        choice = readNumber("Enter your choice: ", 1, 6);
        switch (choice) {
            case 1: createAppointment(appointments, bookings, slotBookingCount, nextId); break;
            case 2: viewDailySchedule(appointments, slotBookingCount); break;
            case 3: rescheduleAppointment(appointments, slotBookingCount); break;
            case 4: cancelAppointment(appointments, bookings, slotBookingCount); break;
            case 5: updateAppointmentStatus(appointments, bookings); break;
            case 6: cout << "Returning to main menu...\n"; break;
        }
    } while (choice != 6);
}
