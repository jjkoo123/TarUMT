import tkinter as tk
from tkinter import messagebox, ttk
from datetime import datetime
import json
import os
import uuid  # For unique IDs to ensure safe deletion

# ============================================================================
# BACKEND LOGIC (MODEL)
# ============================================================================

class ScheduleManager:
    """
    Handles data storage, loading, saving, and logic validation.
    Separating logic from GUI makes the code cleaner and reusable.
    """
    DATA_FILE = "grooming_data.json"
    DATE_FMT = "%d%m%Y"
    TIME_FMT = "%H%M"
    ISO_FMT = "%Y-%m-%d %H:%M:%S" # Format for JSON storage
    
    def __init__(self):
        self.tasks = []
        self.load_data()

    def load_data(self):
        """Loads data from JSON file if it exists."""
        if not os.path.exists(self.DATA_FILE):
            return

        try:
            with open(self.DATA_FILE, 'r') as f:
                raw_data = json.load(f)
                
            self.tasks = []
            for item in raw_data:
                # Convert string back to datetime objects
                self.tasks.append({
                    'id': item['id'],
                    'start': datetime.strptime(item['start'], self.ISO_FMT),
                    'end': datetime.strptime(item['end'], self.ISO_FMT),
                    'task': item['task']
                })
            self.sort_tasks()
        except (json.JSONDecodeError, ValueError):
            print("Error loading data file. Starting fresh.")

    def save_data(self):
        """Saves current tasks to JSON file."""
        data_to_save = []
        for item in self.tasks:
            data_to_save.append({
                'id': item['id'],
                'start': item['start'].strftime(self.ISO_FMT),
                'end': item['end'].strftime(self.ISO_FMT),
                'task': item['task']
            })
        
        try:
            with open(self.DATA_FILE, 'w') as f:
                json.dump(data_to_save, f, indent=4)
        except IOError as e:
            print(f"Error saving data: {e}")

    def add_task(self, start_dt, end_dt, task_desc):
        """Adds a task after checking for conflicts."""
        # Conflict Detection
        for item in self.tasks:
            # Logic: (StartA < EndB) and (EndA > StartB) indicates overlap
            if start_dt < item['end'] and end_dt > item['start']:
                return False, f"Conflict with: {item['task']}"

        new_task = {
            'id': str(uuid.uuid4()), # Unique ID
            'start': start_dt,
            'end': end_dt,
            'task': task_desc
        }
        self.tasks.append(new_task)
        self.sort_tasks()
        self.save_data()
        return True, "Task added successfully."

    def delete_task(self, task_id):
        """Deletes a task by its Unique ID."""
        original_count = len(self.tasks)
        self.tasks = [t for t in self.tasks if t['id'] != task_id]
        
        if len(self.tasks) < original_count:
            self.save_data()
            return True
        return False

    def clear_all(self):
        self.tasks = []
        self.save_data()

    def sort_tasks(self):
        self.tasks.sort(key=lambda x: x['start'])

    @staticmethod
    def parse_datetime(date_str, time_str):
        """Helper to parse inputs into datetime objects."""
        try:
            return datetime.strptime(f"{date_str}{time_str}", "%d%m%Y%H%M")
        except ValueError:
            return None

# ============================================================================
# FRONTEND GUI (VIEW)
# ============================================================================

class GroomingApp(tk.Tk):
    DISPLAY_FMT = "%d/%m/%Y %H:%M"

    def __init__(self):
        super().__init__()
        self.manager = ScheduleManager() # Connect to Backend
        
        self.title("📝 Grooming Schedule Manager (v2.0)")
        self.geometry("800x600")
        
        # Apply a theme
        style = ttk.Style()
        style.theme_use('clam') # 'clam', 'alt', 'default', 'classic'
        
        self._setup_ui()
        self.refresh_ui()

    def _setup_ui(self):
        # 1. Main Container
        main_frame = ttk.Frame(self, padding=10)
        main_frame.pack(fill=tk.BOTH, expand=True)

        # 2. Treeview (Table)
        tree_frame = ttk.LabelFrame(main_frame, text="Scheduled Appointments", padding=5)
        tree_frame.pack(side=tk.TOP, fill=tk.BOTH, expand=True)

        columns = ("start", "end", "task", "id") # ID is hidden
        self.tree = ttk.Treeview(tree_frame, columns=columns, show="headings", selectmode="browse")
        
        self.tree.heading("start", text="Start Time")
        self.tree.heading("end", text="End Time")
        self.tree.heading("task", text="Task Description")
        
        self.tree.column("start", width=140, anchor="center")
        self.tree.column("end", width=140, anchor="center")
        self.tree.column("task", width=350, anchor="w")
        self.tree.column("id", width=0, stretch=tk.NO) # Hidden column
        
        scrollbar = ttk.Scrollbar(tree_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscroll=scrollbar.set)
        
        self.tree.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

        # 3. Input Area
        input_frame = ttk.LabelFrame(main_frame, text="Add / Manage Tasks", padding=10)
        input_frame.pack(side=tk.BOTTOM, fill=tk.X, pady=(10, 0))

        # Grid Layout for inputs
        # Row 0: Headers
        ttk.Label(input_frame, text="Date (DDMMYYYY)").grid(row=0, column=0, padx=5, sticky="w")
        ttk.Label(input_frame, text="Start (HHMM)").grid(row=0, column=1, padx=5, sticky="w")
        ttk.Label(input_frame, text="End Date (Opt)").grid(row=0, column=2, padx=5, sticky="w")
        ttk.Label(input_frame, text="End (HHMM)").grid(row=0, column=3, padx=5, sticky="w")

        # Row 1: Entries
        self.ent_date = ttk.Entry(input_frame, width=15)
        self.ent_date.grid(row=1, column=0, padx=5, pady=5)
        
        self.ent_start = ttk.Entry(input_frame, width=10)
        self.ent_start.grid(row=1, column=1, padx=5, pady=5)
        
        self.ent_end_date = ttk.Entry(input_frame, width=15)
        self.ent_end_date.grid(row=1, column=2, padx=5, pady=5)
        
        self.ent_end_time = ttk.Entry(input_frame, width=10)
        self.ent_end_time.grid(row=1, column=3, padx=5, pady=5)

        # Row 2: Task Description
        ttk.Label(input_frame, text="Description:").grid(row=2, column=0, sticky="w", padx=5)
        self.ent_task = ttk.Entry(input_frame)
        self.ent_task.grid(row=2, column=1, columnspan=3, sticky="ew", padx=5, pady=5)

        # Row 3: Buttons (Action Bar)
        btn_frame = ttk.Frame(input_frame)
        btn_frame.grid(row=3, column=0, columnspan=4, pady=10)
        
        ttk.Button(btn_frame, text="➕ Add Task", command=self.action_add).pack(side=tk.LEFT, padx=5)
        ttk.Button(btn_frame, text="❌ Delete Selected", command=self.action_delete).pack(side=tk.LEFT, padx=5)
        ttk.Button(btn_frame, text="🗑️ Clear All", command=self.action_clear).pack(side=tk.LEFT, padx=5)

        # Set default date to today for convenience
        today_str = datetime.now().strftime("%d%m%Y")
        self.ent_date.insert(0, today_str)

    def refresh_ui(self):
        """Clears and repopulates the treeview."""
        for item in self.tree.get_children():
            self.tree.delete(item)
            
        for task in self.manager.tasks:
            self.tree.insert("", "end", values=(
                task['start'].strftime(self.DISPLAY_FMT),
                task['end'].strftime(self.DISPLAY_FMT),
                task['task'],
                task['id']
            ))

    def action_add(self):
        s_date = self.ent_date.get().strip()
        s_time = self.ent_start.get().strip()
        e_date = self.ent_end_date.get().strip()
        e_time = self.ent_end_time.get().strip()
        desc = self.ent_task.get().strip()

        # Input Validation
        if not desc:
            return messagebox.showerror("Input Error", "Please enter a task description.")

        # Logic: Default end date to start date if empty
        if not e_date: e_date = s_date

        start_dt = self.manager.parse_datetime(s_date, s_time)
        end_dt = self.manager.parse_datetime(e_date, e_time)

        if not start_dt or not end_dt:
            return messagebox.showerror("Format Error", "Invalid Date/Time format.\nUse DDMMYYYY for dates and HHMM for times.")

        if start_dt >= end_dt:
            return messagebox.showerror("Logic Error", "End time must be after Start time.")

        # Pass to manager
        success, msg = self.manager.add_task(start_dt, end_dt, desc)
        
        if success:
            self.refresh_ui()
            # Clear inputs except dates (user might add multiple tasks for same day)
            self.ent_task.delete(0, tk.END)
            self.ent_start.delete(0, tk.END)
            self.ent_end_time.delete(0, tk.END)
            messagebox.showinfo("Success", msg)
        else:
            messagebox.showerror("Conflict Error", msg)

    def action_delete(self):
        selected = self.tree.selection()
        if not selected:
            return messagebox.showwarning("Select Task", "Please click on a task to delete it.")
        
        # Retrieve the hidden UUID from the selected row
        # values = (start, end, task, ID) -> ID is at index 3
        task_id = self.tree.item(selected[0])['values'][3]
        
        if messagebox.askyesno("Confirm", "Delete this task permanently?"):
            if self.manager.delete_task(task_id):
                self.refresh_ui()
                messagebox.showinfo("Deleted", "Task removed.")
            else:
                messagebox.showerror("Error", "Could not delete task.")

    def action_clear(self):
        if messagebox.askyesno("Warning", "Delete ALL tasks? This cannot be undone."):
            self.manager.clear_all()
            self.refresh_ui()

if __name__ == "__main__":
    app = GroomingApp()
    app.mainloop()