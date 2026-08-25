import tkinter as tk
from tkinter import ttk, messagebox
import datetime

# --- Mock Database ---
# We use a simple list to store incidents in memory for this demo.
# Each incident is a dictionary.
incidents_db = [
    {"id": "INC-054", "type": "Plumbing", "location": "Block B Lvl 2", "desc": "Leaky pipe near lift", "status": "In Progress", "vendor": "City Plumbers"},
    {"id": "INC-055", "type": "Amenity", "location": "Gym Block A", "desc": "Treadmill #2 power failure", "status": "Pending", "vendor": "None"},
]

# Global counter for daily updates to simulate the automation trigger
daily_update_count = 10  # Start at 10 to demonstrate the trigger easily

class CondoApp(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("Condominium A Management System")
        self.geometry("900x600")
        self.configure(bg="#f0f0f0")

        # Container for all frames
        self.container = tk.Frame(self)
        self.container.pack(side="top", fill="both", expand=True)
        self.container.grid_rowconfigure(0, weight=1)
        self.container.grid_columnconfigure(0, weight=1)

        # Dictionary to store frames
        self.frames = {}

        # Initialize all pages
        for F in (MainMenu, AddIncidentPage, ViewManagePage):
            page_name = F.__name__
            frame = F(parent=self.container, controller=self)
            self.frames[page_name] = frame
            frame.grid(row=0, column=0, sticky="nsew")

        self.show_frame("MainMenu")

    def show_frame(self, page_name):
        '''Show a frame for the given page name'''
        frame = self.frames[page_name]
        frame.tkraise()
        # If showing the view page, refresh the data
        if page_name == "ViewManagePage":
            frame.refresh_data()


class MainMenu(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent, bg="#f4f6f9")
        self.controller = controller

        header = tk.Label(self, text="Main Dashboard", font=("Helvetica", 28, "bold"), bg="#f4f6f9", fg="#2c3e50")
        header.pack(pady=50)

        btn_frame = tk.Frame(self, bg="#f4f6f9")
        btn_frame.pack(expand=True)

        # Buttons
        btn_style = {"font": ("Helvetica", 14), "width": 25, "height": 3, "bd": 0, "cursor": "hand2"}

        btn_add = tk.Button(btn_frame, text="+ Report New Incident", bg="#28a745", fg="white", **btn_style,
                            command=lambda: controller.show_frame("AddIncidentPage"))
        btn_add.grid(row=0, column=0, padx=20, pady=20)

        btn_view = tk.Button(btn_frame, text="🔍 Search & Manage Incidents", bg="#17a2b8", fg="white", **btn_style,
                             command=lambda: controller.show_frame("ViewManagePage"))
        btn_view.grid(row=0, column=1, padx=20, pady=20)



class AddIncidentPage(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent, bg="#ffffff")
        self.controller = controller

        # Header
        header_frame = tk.Frame(self, bg="#28a745", height=80)
        header_frame.pack(fill="x")
        tk.Label(header_frame, text="Report New Incident", font=("Helvetica", 20, "bold"), bg="#28a745", fg="white").pack(pady=20)

        # Form
        form_frame = tk.Frame(self, bg="#ffffff")
        form_frame.pack(pady=30)

        # Type
        tk.Label(form_frame, text="Incident Type:", font=("Helvetica", 12, "bold"), bg="#ffffff").grid(row=0, column=0, sticky="w", pady=10)
        self.type_var = tk.StringVar()
        self.type_combo = ttk.Combobox(form_frame, textvariable=self.type_var, values=["Plumbing", "Electrical", "Amenity/Gym", "Structural", "Other"], width=40)
        self.type_combo.grid(row=0, column=1, padx=20)
        self.type_combo.current(0)

        # Location
        tk.Label(form_frame, text="Location:", font=("Helvetica", 12, "bold"), bg="#ffffff").grid(row=1, column=0, sticky="w", pady=10)
        self.entry_loc = ttk.Entry(form_frame, width=42)
        self.entry_loc.grid(row=1, column=1, padx=20)

        # Description
        tk.Label(form_frame, text="Description:", font=("Helvetica", 12, "bold"), bg="#ffffff").grid(row=2, column=0, sticky="nw", pady=10)
        self.text_desc = tk.Text(form_frame, height=5, width=42)
        self.text_desc.grid(row=2, column=1, padx=20)

        # Buttons
        btn_frame = tk.Frame(self, bg="#ffffff")
        btn_frame.pack(pady=20)

        tk.Button(btn_frame, text="Submit Report", bg="#28a745", fg="white", font=("Helvetica", 12, "bold"), width=15,
                  command=self.submit_incident).pack(side="left", padx=10)
        
        tk.Button(btn_frame, text="Cancel", bg="#6c757d", fg="white", font=("Helvetica", 12), width=15,
                  command=lambda: controller.show_frame("MainMenu")).pack(side="left", padx=10)

    def submit_incident(self):
        new_id = f"INC-{len(incidents_db) + 55}" # Generate fake ID
        new_incident = {
            "id": new_id,
            "type": self.type_var.get(),
            "location": self.entry_loc.get(),
            "desc": self.text_desc.get("1.0", "end-1c"),
            "status": "Pending",
            "vendor": "None"
        }
        incidents_db.append(new_incident)
        messagebox.showinfo("Success", f"Incident {new_id} created successfully!")
        self.entry_loc.delete(0, 'end')
        self.text_desc.delete('1.0', 'end')
        self.controller.show_frame("MainMenu")

class ViewManagePage(tk.Frame):
    def __init__(self, parent, controller):
        super().__init__(parent, bg="#ffffff")
        self.controller = controller

        # Header
        header_frame = tk.Frame(self, bg="#17a2b8", height=80)
        header_frame.pack(fill="x")
        tk.Label(header_frame, text="Manage Incidents", font=("Helvetica", 20, "bold"), bg="#17a2b8", fg="white").pack(pady=20)

        # Treeview (Table)
        table_frame = tk.Frame(self)
        table_frame.pack(pady=20, padx=20, fill="both", expand=True)

        cols = ("ID", "Type", "Location", "Status", "Vendor", "Description")
        self.tree = ttk.Treeview(table_frame, columns=cols, show="headings", height=8)
        
        for col in cols:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=120)
        self.tree.column("Description", width=250)
        
        self.tree.pack(side="left", fill="both", expand=True)
        
        # Scrollbar
        scrollbar = ttk.Scrollbar(table_frame, orient="vertical", command=self.tree.yview)
        scrollbar.pack(side="right", fill="y")
        self.tree.configure(yscrollcommand=scrollbar.set)

        # Management Controls
        control_frame = tk.Frame(self, bg="#f8f9fa", bd=1, relief="solid", padx=10, pady=10)
        control_frame.pack(pady=20, fill="x", padx=20)

        tk.Label(control_frame, text="Management Actions", font=("Helvetica", 14, "bold"), bg="#f8f9fa").grid(row=0, column=0, sticky="w", columnspan=4, pady=(0, 10))

        # Assign Vendor
        tk.Label(control_frame, text="Assign Vendor:", bg="#f8f9fa").grid(row=1, column=0, padx=5)
        self.entry_vendor = ttk.Entry(control_frame, width=20)
        self.entry_vendor.grid(row=1, column=1, padx=5)
        tk.Button(control_frame, text="Update Vendor", command=self.update_vendor, bg="#ffc107").grid(row=1, column=2, padx=5)

        # Update Status
        tk.Label(control_frame, text="Update Status:", bg="#f8f9fa").grid(row=1, column=3, padx=(30, 5))
        self.status_var = tk.StringVar()
        self.status_combo = ttk.Combobox(control_frame, textvariable=self.status_var, values=["Pending", "Awaiting Vendor Quote", "In Progress", "Resolved"], width=20)
        self.status_combo.grid(row=1, column=4, padx=5)
        tk.Button(control_frame, text="Update Status", command=self.update_status, bg="#28a745", fg="white").grid(row=1, column=5, padx=5)

        # Back Button
        tk.Button(self, text="<< Back to Menu", command=lambda: controller.show_frame("MainMenu"), width=20).pack(pady=10)

    def refresh_data(self):
        # Clear existing
        for i in self.tree.get_children():
            self.tree.delete(i)
        # Add from db
        for inc in incidents_db:
            self.tree.insert("", "end", values=(inc["id"], inc["type"], inc["location"], inc["status"], inc["vendor"], inc["desc"]))

    def get_selected_id(self):
        selected = self.tree.selection()
        if not selected:
            messagebox.showwarning("Selection Error", "Please select an incident from the list first.")
            return None
        return self.tree.item(selected[0])['values'][0]

    def update_vendor(self):
        inc_id = self.get_selected_id()
        vendor = self.entry_vendor.get()
        if inc_id and vendor:
            for inc in incidents_db:
                if inc["id"] == inc_id:
                    inc["vendor"] = vendor
                    if inc["status"] == "Pending":
                        inc["status"] = "Awaiting Vendor Quote" # Auto update status
                    break
            self.refresh_data()
            self.entry_vendor.delete(0, 'end')
            messagebox.showinfo("Updated", f"Vendor assigned to {inc_id}")

    def update_status(self):
        global daily_update_count
        inc_id = self.get_selected_id()
        new_status = self.status_var.get()
        
        if inc_id and new_status:
            # 1. Update DB
            for inc in incidents_db:
                if inc["id"] == inc_id:
                    inc["status"] = new_status
                    break
            
            # 2. Increment Daily Counter
            daily_update_count += 1
            self.refresh_data()
            
            # 3. Check Logic for Automation (The requirement from the case study)
            if daily_update_count > 10:
                self.trigger_automation_report(daily_update_count)
            else:
                messagebox.showinfo("Updated", f"Status updated to {new_status}.\n(Daily Updates: {daily_update_count})")

    def trigger_automation_report(self, count):
        # This simulates the backend logic generating a PDF
        msg = (f"AUTOMATION TRIGGERED!\n\n"
               f"Daily Update Count: {count}\n"
               f"Threshold (>10) Exceeded.\n\n"
               f"System is generating 'Daily Summary Report'...\n"
               f"Emailing to Management Committee...")
        messagebox.showinfo("System Alert", msg)

if __name__ == "__main__":
    app = CondoApp()
    app.mainloop()