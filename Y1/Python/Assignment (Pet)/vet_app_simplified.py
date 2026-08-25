import tkinter as tk
from tkinter import ttk, messagebox, scrolledtext
import json
import os
from datetime import datetime, timedelta
from typing import List, Dict, Any, Optional
from pathlib import Path

# ============================================================================
# 1. SHARED UTILITIES & CONFIGURATION
# ============================================================================

class Config:
    """Central configuration for styling and constants."""
    APP_TITLE = "🐾 Ultimate Pet Care Suite 2.0"
    GEOMETRY = "1200x800"
    
    # Color Palette
    PRIMARY = "#2E7D32"      # Forest Green
    SECONDARY = "#4CAF50"    # Light Green
    ACCENT = "#FF9800"       # Orange
    BG_COLOR = "#F5F5F5"     # Light Gray
    TEXT_COLOR = "#333333"
    
    # Fonts
    HEADER_FONT = ("Segoe UI", 12, "bold")
    BODY_FONT = ("Segoe UI", 10)

class DateHelper:
    """Standardized date handling across the entire app."""
    DISPLAY_FMT = "%Y-%m-%d %H:%M"
    STORAGE_FMT = "%Y-%m-%d %H:%M:%S"
    SHORT_DATE = "%d/%m/%Y"

    @staticmethod
    def now_str() -> str:
        return datetime.now().strftime(DateHelper.DISPLAY_FMT)

    @staticmethod
    def to_str(dt_obj: datetime) -> str:
        return dt_obj.strftime(DateHelper.DISPLAY_FMT)

    @staticmethod
    def from_str(dt_str: str) -> datetime:
        try:
            return datetime.strptime(dt_str, DateHelper.DISPLAY_FMT)
        except ValueError:
            # Fallback for old formats or partial dates
            try:
                return datetime.strptime(dt_str, "%Y-%m-%d")
            except ValueError:
                return datetime.now()

class JSONManager:
    """Generic Data Manager for handling JSON persistence."""
    def __init__(self, filename: str):
        self.filepath = Path(filename)
        self.data = []

    def load(self) -> List[Dict]:
        if not self.filepath.exists():
            return []
        try:
            with open(self.filepath, 'r') as f:
                self.data = json.load(f)
            return self.data
        except (json.JSONDecodeError, IOError):
            return []

    def save(self):
        try:
            with open(self.filepath, 'w') as f:
                json.dump(self.data, f, indent=2)
        except IOError as e:
            print(f"Error saving data: {e}")

# ============================================================================
# 2. DOMAIN LOGIC (BACKEND)
# ============================================================================

# --- MODULE A: FEEDING TRACKER ---
class FeedingManager(JSONManager):
    def __init__(self):
        super().__init__("data_feeding.json")
        self.load()
        # Initialize default structure if empty
        if not isinstance(self.data, dict):
            self.data = {'pets': [], 'records': []}
        
    def add_pet(self, name: str, type_: str):
        # Prevent duplicates
        if any(p['name'].lower() == name.lower() for p in self.data['pets']):
            return False, "Pet already exists."
        
        self.data['pets'].append({'name': name, 'type': type_})
        self.save()
        return True, "Pet added."

    def log_feeding(self, pet_name: str, food: str, qty: str):
        record = {
            'timestamp': DateHelper.now_str(),
            'pet': pet_name,
            'food': food,
            'quantity': qty
        }
        self.data['records'].insert(0, record) # Add to top
        self.save()
        return True

# --- MODULE B: VET SYSTEM ---
class VetManager(JSONManager):
    def __init__(self):
        super().__init__("data_vet.json")
        self.load()

    def add_appointment(self, appt_data: Dict):
        appt_data['id'] = len(self.data) + 1
        appt_data['status'] = 'Scheduled'
        self.data.append(appt_data)
        self.save()

    def update_appointment(self, appt_id: int, notes: str, cost: float):
        for appt in self.data:
            if appt['id'] == appt_id:
                appt['notes'] = notes
                appt['cost'] = cost
                appt['status'] = 'Completed'
                self.save()
                return True
        return False

# --- MODULE C: GROOMING SCHEDULE ---
class GroomingManager(JSONManager):
    def __init__(self):
        super().__init__("data_grooming.json")
        self.load()

    def add_task(self, start_str: str, duration_mins: int, task: str):
        start_dt = DateHelper.from_str(start_str)
        end_dt = start_dt + timedelta(minutes=duration_mins)
        
        # Conflict Detection
        for item in self.data:
            exist_start = datetime.fromisoformat(item['start_iso'])
            exist_end = datetime.fromisoformat(item['end_iso'])
            
            # Logic: (StartA < EndB) and (EndA > StartB)
            if start_dt < exist_end and end_dt > exist_start:
                return False, f"Conflict with task: {item['task']}"

        self.data.append({
            'start_display': DateHelper.to_str(start_dt),
            'end_display': DateHelper.to_str(end_dt),
            'start_iso': start_dt.isoformat(),
            'end_iso': end_dt.isoformat(),
            'task': task
        })
        # Sort by start time
        self.data.sort(key=lambda x: x['start_iso'])
        self.save()
        return True, "Task scheduled."

    def delete_task(self, index: int):
        if 0 <= index < len(self.data):
            del self.data[index]
            self.save()

# ============================================================================
# 3. GUI COMPONENTS (FRONTEND)
# ============================================================================

class UIHelper:
    """Helpers to reduce GUI boilerplate code."""
    @staticmethod
    def create_table(parent, columns: tuple):
        tree = ttk.Treeview(parent, columns=columns, show='headings', selectmode='browse')
        for col in columns:
            tree.heading(col, text=col)
            # Dynamic width calculation could go here, strictly setting defaults for now
            tree.column(col, width=120) 
        
        scroll_y = ttk.Scrollbar(parent, orient="vertical", command=tree.yview)
        tree.configure(yscrollcommand=scroll_y.set)
        
        tree.pack(side='left', fill='both', expand=True)
        scroll_y.pack(side='right', fill='y')
        return tree

    @staticmethod
    def create_form_row(parent, label_text, row_idx):
        ttk.Label(parent, text=label_text).grid(row=row_idx, column=0, sticky='e', padx=10, pady=5)
        entry = ttk.Entry(parent, width=30)
        entry.grid(row=row_idx, column=1, sticky='w', padx=10, pady=5)
        return entry

# --- TAB 1: FEEDING ---
class FeedingTab(ttk.Frame):
    def __init__(self, parent):
        super().__init__(parent)
        self.manager = FeedingManager()
        self._setup_layout()

    def _setup_layout(self):
        paned = ttk.PanedWindow(self, orient='horizontal')
        paned.pack(fill='both', expand=True, padx=10, pady=10)

        # Left: Controls
        left_frame = ttk.LabelFrame(paned, text="Actions", padding=10)
        paned.add(left_frame, weight=1)

        # Register Pet Area
        ttk.Label(left_frame, text="New Pet Name:", font=Config.HEADER_FONT).pack(anchor='w')
        self.ent_pet_name = ttk.Entry(left_frame)
        self.ent_pet_name.pack(fill='x', pady=5)
        
        self.cmb_type = ttk.Combobox(left_frame, values=["Dog", "Cat", "Bird", "Other"])
        self.cmb_type.set("Dog")
        self.cmb_type.pack(fill='x', pady=5)
        
        ttk.Button(left_frame, text="Add Pet", command=self.add_pet).pack(fill='x', pady=5)
        
        ttk.Separator(left_frame, orient='horizontal').pack(fill='x', pady=15)

        # Log Feeding Area
        ttk.Label(left_frame, text="Log Meal:", font=Config.HEADER_FONT).pack(anchor='w')
        
        ttk.Label(left_frame, text="Select Pet:").pack(anchor='w')
        self.cmb_pet_select = ttk.Combobox(left_frame, state="readonly")
        self.cmb_pet_select.pack(fill='x', pady=2)

        ttk.Label(left_frame, text="Food Type:").pack(anchor='w')
        self.ent_food = ttk.Entry(left_frame)
        self.ent_food.pack(fill='x', pady=2)
        
        ttk.Label(left_frame, text="Quantity (g):").pack(anchor='w')
        self.ent_qty = ttk.Entry(left_frame)
        self.ent_qty.pack(fill='x', pady=2)

        ttk.Button(left_frame, text="Log Feeding", command=self.log_meal).pack(fill='x', pady=10)

        # Right: History
        right_frame = ttk.LabelFrame(paned, text="Feeding History", padding=10)
        paned.add(right_frame, weight=3)

        self.tree = UIHelper.create_table(right_frame, ("Time", "Pet", "Food", "Qty"))
        
        self.refresh_ui()

    def add_pet(self):
        name = self.ent_pet_name.get().strip()
        if not name: return
        success, msg = self.manager.add_pet(name, self.cmb_type.get())
        if success:
            messagebox.showinfo("Success", msg)
            self.ent_pet_name.delete(0, 'end')
            self.refresh_ui()
        else:
            messagebox.showerror("Error", msg)

    def log_meal(self):
        pet = self.cmb_pet_select.get()
        food = self.ent_food.get()
        qty = self.ent_qty.get()
        if not all([pet, food, qty]):
            messagebox.showwarning("Missing Info", "Please fill all fields")
            return
        
        self.manager.log_feeding(pet, food, qty)
        self.refresh_ui()
        self.ent_food.delete(0, 'end')
        self.ent_qty.delete(0, 'end')

    def refresh_ui(self):
        # Update Combo
        pets = [p['name'] for p in self.manager.data.get('pets', [])]
        self.cmb_pet_select['values'] = pets
        if pets: self.cmb_pet_select.current(0)

        # Update Tree
        for i in self.tree.get_children(): self.tree.delete(i)
        for r in self.manager.data.get('records', []):
            self.tree.insert("", "end", values=(r['timestamp'], r['pet'], r['food'], r['quantity']))

# --- TAB 2: VET SYSTEM ---
class VetTab(ttk.Frame):
    def __init__(self, parent):
        super().__init__(parent)
        self.manager = VetManager()
        self.notebook = ttk.Notebook(self)
        self.notebook.pack(fill='both', expand=True)

        self.tab_list = ttk.Frame(self.notebook, padding=10)
        self.tab_add = ttk.Frame(self.notebook, padding=10)
        
        self.notebook.add(self.tab_list, text="Appointments List")
        self.notebook.add(self.tab_add, text="New Appointment")
        
        self._setup_list_view()
        self._setup_add_view()

    def _setup_list_view(self):
        # Filter Bar
        bar = ttk.Frame(self.tab_list)
        bar.pack(fill='x', pady=5)
        ttk.Button(bar, text="Refresh", command=self.refresh_list).pack(side='right')
        ttk.Button(bar, text="Update Selected", command=self.open_update_dialog).pack(side='right', padx=5)

        self.tree = UIHelper.create_table(self.tab_list, ("ID", "Date", "Pet", "Owner", "Vet", "Status"))
        self.refresh_list()

    def _setup_add_view(self):
        # Form Container
        container = ttk.Frame(self.tab_add)
        container.pack(fill='both', expand=True, padx=50)

        self.inputs = {}
        fields = ["Owner Name", "Owner Phone", "Pet Name", "Species", "Date (YYYY-MM-DD HH:MM)", "Reason", "Vet Name"]
        
        for i, field in enumerate(fields):
            self.inputs[field] = UIHelper.create_form_row(container, field, i)

        # Set default date
        self.inputs["Date (YYYY-MM-DD HH:MM)"].insert(0, DateHelper.now_str())

        ttk.Button(container, text="Save Appointment", command=self.save_appt).grid(row=len(fields), column=1, pady=20, sticky='ew')

    def save_appt(self):
        data = {k: v.get() for k, v in self.inputs.items()}
        if not data["Pet Name"] or not data["Owner Name"]:
            messagebox.showerror("Error", "Pet and Owner names are required")
            return
            
        clean_data = {
            'owner': {'name': data['Owner Name'], 'phone': data['Owner Phone']},
            'pet': {'name': data['Pet Name'], 'species': data['Species']},
            'date': data['Date (YYYY-MM-DD HH:MM)'],
            'reason': data['Reason'],
            'vet': data['Vet Name'],
            'notes': '', 'cost': 0.0
        }
        
        self.manager.add_appointment(clean_data)
        messagebox.showinfo("Success", "Appointment Booked")
        self.refresh_list()
        self.notebook.select(0) # Go to list
        # Clear form
        for v in self.inputs.values(): v.delete(0, 'end')

    def refresh_list(self):
        for i in self.tree.get_children(): self.tree.delete(i)
        for r in self.manager.data:
            self.tree.insert("", "end", values=(
                r['id'], r['date'], r['pet']['name'], r['owner']['name'], r['vet'], r['status']
            ))

    def open_update_dialog(self):
        sel = self.tree.selection()
        if not sel: return
        
        item_vals = self.tree.item(sel[0])['values']
        appt_id = item_vals[0]
        
        # Create Popup
        win = tk.Toplevel(self)
        win.title(f"Update Appt #{appt_id}")
        win.geometry("400x300")
        
        ttk.Label(win, text="Clinical Notes:").pack(pady=5)
        txt = tk.Text(win, height=5, width=40)
        txt.pack(pady=5)
        
        ttk.Label(win, text="Total Cost:").pack(pady=5)
        ent_cost = ttk.Entry(win)
        ent_cost.pack(pady=5)
        
        def save():
            try:
                c = float(ent_cost.get())
                self.manager.update_appointment(appt_id, txt.get("1.0", "end-1c"), c)
                self.refresh_list()
                win.destroy()
            except ValueError:
                messagebox.showerror("Error", "Cost must be a number")

        ttk.Button(win, text="Save & Complete", command=save).pack(pady=10)

# --- TAB 3: GROOMING ---
class GroomingTab(ttk.Frame):
    def __init__(self, parent):
        super().__init__(parent)
        self.manager = GroomingManager()
        self._setup_ui()

    def _setup_ui(self):
        # Top: Add Task
        top_frame = ttk.LabelFrame(self, text="Schedule Grooming", padding=10)
        top_frame.pack(fill='x', padx=10, pady=5)

        f1 = ttk.Frame(top_frame)
        f1.pack(fill='x')
        
        ttk.Label(f1, text="Start Time:").pack(side='left')
        self.ent_start = ttk.Entry(f1, width=20)
        self.ent_start.pack(side='left', padx=5)
        self.ent_start.insert(0, DateHelper.now_str())
        
        ttk.Label(f1, text="Duration (mins):").pack(side='left', padx=10)
        self.ent_dur = ttk.Entry(f1, width=10)
        self.ent_dur.insert(0, "60")
        self.ent_dur.pack(side='left')

        f2 = ttk.Frame(top_frame)
        f2.pack(fill='x', pady=5)
        ttk.Label(f2, text="Task Description:").pack(side='left')
        self.ent_task = ttk.Entry(f2)
        self.ent_task.pack(side='left', fill='x', expand=True, padx=5)

        btn_frame = ttk.Frame(top_frame)
        btn_frame.pack(fill='x', pady=5)
        ttk.Button(btn_frame, text="Add Task", command=self.add_task).pack(side='left')
        ttk.Button(btn_frame, text="Delete Selected", command=self.delete_task).pack(side='left', padx=10)

        # Bottom: List
        self.tree = UIHelper.create_table(self, ("Start", "End", "Task"))
        self.refresh_list()

    def add_task(self):
        try:
            dur = int(self.ent_dur.get())
            success, msg = self.manager.add_task(self.ent_start.get(), dur, self.ent_task.get())
            if success:
                self.refresh_list()
                self.ent_task.delete(0, 'end')
            else:
                messagebox.showerror("Conflict", msg)
        except ValueError:
            messagebox.showerror("Error", "Invalid duration or date format")

    def delete_task(self):
        sel = self.tree.selection()
        if sel:
            idx = self.tree.index(sel[0])
            self.manager.delete_task(idx)
            self.refresh_list()

    def refresh_list(self):
        for i in self.tree.get_children(): self.tree.delete(i)
        for item in self.manager.data:
            self.tree.insert("", "end", values=(item['start_display'], item['end_display'], item['task']))

# ============================================================================
# 4. MAIN APPLICATION ENTRY
# ============================================================================

class MainApp(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title(Config.APP_TITLE)
        self.geometry(Config.GEOMETRY)
        self.configure(bg=Config.BG_COLOR)
        
        self._setup_styles()
        self._build_ui()

    def _setup_styles(self):
        style = ttk.Style(self)
        style.theme_use('clam')
        
        # Configure Colors
        style.configure("TFrame", background=Config.BG_COLOR)
        style.configure("TLabel", background=Config.BG_COLOR, font=Config.BODY_FONT, foreground=Config.TEXT_COLOR)
        style.configure("TButton", font=Config.BODY_FONT, padding=5)
        
        # Notebook Style
        style.configure("TNotebook", background=Config.BG_COLOR, tabmargins=[5, 5, 5, 0])
        style.configure("TNotebook.Tab", padding=[15, 5], font=Config.HEADER_FONT)
        style.map("TNotebook.Tab", background=[("selected", Config.PRIMARY)], foreground=[("selected", "white")])

        # Header Label Style
        style.configure("Header.TLabel", font=("Segoe UI", 18, "bold"), foreground=Config.PRIMARY, padding=10)

    def _build_ui(self):
        # Header
        header_frame = ttk.Frame(self)
        header_frame.pack(fill='x', pady=0)
        # A green stripe at the top
        tk.Frame(header_frame, bg=Config.PRIMARY, height=10).pack(fill='x')
        ttk.Label(header_frame, text=Config.APP_TITLE, style="Header.TLabel").pack()

        # Navigation
        notebook = ttk.Notebook(self)
        notebook.pack(fill='both', expand=True, padx=10, pady=10)

        # Tabs
        notebook.add(FeedingTab(notebook), text=" 🍖 Feeding Tracker ")
        notebook.add(VetTab(notebook), text=" 🏥 Vet Management ")
        notebook.add(GroomingTab(notebook), text=" ✂️ Grooming Schedule ")

        # Status Bar
        status_frame = tk.Frame(self, bg=Config.SECONDARY, height=25)
        status_frame.pack(fill='x', side='bottom')
        tk.Label(status_frame, text="System Ready", bg=Config.SECONDARY, fg='white', font=("Segoe UI", 8)).pack(side='right', padx=10)

if __name__ == "__main__":
    app = MainApp()
    app.mainloop()