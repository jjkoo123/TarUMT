import tkinter as tk
from tkinter import ttk, messagebox, scrolledtext
import json
import os
from datetime import datetime, timedelta
from typing import List, Dict, Any, Optional, Tuple
from pathlib import Path
import uuid

# ============================================================================
# SHARED UTILITIES & CONFIGURATION
# ============================================================================

class Config:
    """Central configuration for styling and constants."""
    APP_TITLE = "🐾 Ultimate Pet Care Suite"
    
    COLORS = {
        'primary': '#2E7D32',
        'primary_light': '#4CAF50',
        'primary_dark': '#1B5E20',
        'secondary': '#FF6F00',
        'secondary_light': '#FFA726',
        'accent': '#0288D1',
        'accent_light': '#4FC3F7',
        'danger': '#D32F2F',
        'danger_light': '#EF5350',
        'success': '#388E3C',
        'success_light': '#66BB6A',
        'warning': '#F57C00',
        'warning_light': '#FFB74D',
        'bg': '#ECEFF1',
        'card': '#FFFFFF',
        'text': '#212121',
        'text_light': '#616161',
        'text_muted': '#9E9E9E',
        'border': '#CFD8DC',
        'input_bg': '#FAFAFA',
        'hover': '#F5F5F5',
    }
    
    FONTS = {
        'header': ('Segoe UI', 26, 'bold'),
        'subtitle': ('Segoe UI', 13, 'bold'),
        'label': ('Segoe UI', 10, 'bold'),
        'body': ('Segoe UI', 10),
        'small': ('Segoe UI', 9),
        'button': ('Segoe UI', 10, 'bold'),
        'mono': ('Courier New', 9),
    }
    
    PADDING = {'xs': 5, 'sm': 8, 'md': 12, 'lg': 16, 'xl': 20}

class Validator:
    @staticmethod
    def required(val: str, name: str) -> Optional[str]:
        return None if val.strip() else f"{name} is required"

    @staticmethod
    def positive_int(val: str, name: str) -> Tuple[Optional[int], Optional[str]]:
        try:
            num = int(val)
            if num <= 0:
                return None, f"{name} must be positive"
            return num, None
        except Exception:
            return None, f"{name} must be a number"

    @staticmethod
    def positive_float(val: str, name: str) -> Tuple[Optional[float], Optional[str]]:
        try:
            num = float(val)
            if num < 0:
                return None, f"{name} cannot be negative"
            return num, None
        except Exception:
            return None, f"{name} must be a number"


class DateHelper:
    DISPLAY_FMT = "%Y-%m-%d %H:%M"
    SHORT_FMT = "%d/%m/%Y"
    TIME_FMT = "%H:%M"
    FORMATS = [DISPLAY_FMT, "%Y-%m-%d", SHORT_FMT]

    @staticmethod
    def now_str() -> str:
        return datetime.now().strftime(DateHelper.DISPLAY_FMT)

    @staticmethod
    def now_short() -> str:
        return datetime.now().strftime(DateHelper.SHORT_FMT)

    @staticmethod
    def parse(dt_str: str) -> Tuple[Optional[datetime], Optional[str]]:
        for fmt in DateHelper.FORMATS:
            try:
                return datetime.strptime(dt_str.strip(), fmt), None
            except Exception:
                continue
        return None, "Invalid date/time format"

    @staticmethod
    def from_str(dt_str: str) -> datetime:
        dt, _ = DateHelper.parse(dt_str)
        return dt or datetime.now()

class JSONManager:
    def __init__(self, filename: str):
        self.filepath = Path(filename)
        self.data = []
        self.load()
    
    def load(self):
        try:
            if self.filepath.exists():
                with open(self.filepath, 'r', encoding='utf-8') as f:
                    content = f.read().strip()
                    self.data = json.loads(content) if content else []
            else:
                self.data = []
                self.save()
        except (json.JSONDecodeError, IOError) as e:
            print(f"Load error for {self.filepath}: {e}")
            self.data = []
    
    def save(self):
        try:
            self.filepath.parent.mkdir(parents=True, exist_ok=True)
            tmp = self.filepath.with_suffix(self.filepath.suffix + ".tmp")
            with open(tmp, 'w', encoding='utf-8') as f:
                json.dump(self.data, f, indent=2, ensure_ascii=False)
            tmp.replace(self.filepath)
        except Exception as e:
            print(f"Save error: {e}")

# ============================================================================
# BACKEND MANAGERS
# ============================================================================

class FeedingManager:
    def __init__(self):
        self.filepath = Path("feeding_data.json")
        self.pets = []
        self.records = []
        self._load()
    
    def _load(self):
        try:
            if self.filepath.exists():
                with open(self.filepath, 'r', encoding='utf-8') as f:
                    content = f.read().strip()
                    if content:
                        data = json.loads(content)
                        self.pets = data.get('pets', [])
                        self.records = data.get('records', [])
        except (json.JSONDecodeError, IOError) as e:
            print(f"Feeding load error: {e}")
            self.pets = []
            self.records = []
    
    def _save(self):
        try:
            with open(self.filepath, 'w', encoding='utf-8') as f:
                json.dump({'pets': self.pets, 'records': self.records}, f, indent=2, ensure_ascii=False)
        except Exception as e:
            print(f"Save error: {e}")
    
    def add_pet(self, name: str, pet_type: str, age: int, breed: str = "") -> Tuple[bool, str]:
        err = Validator.required(name, "Pet name")
        if err:
            return False, err
        if any(p['name'].lower() == name.lower() for p in self.pets):
            return False, "Pet already exists"
        age_int, err = Validator.positive_int(str(age), "Age")
        if err:
            return False, err
        self.pets.append({
            'name': name, 
            'type': pet_type, 
            'age': age_int, 
            'breed': breed,
            'added_date': datetime.now().isoformat()
        })
        self._save()
        return True, f"✓ {name} added successfully!"
    
    def remove_pet(self, name: str) -> Tuple[bool, str]:
        original = len(self.pets)
        self.pets = [p for p in self.pets if p['name'].lower() != name.lower()]
        if len(self.pets) < original:
            self.records = [r for r in self.records if r['pet'].lower() != name.lower()]
            self._save()
            return True, f"✓ {name} removed"
        return False, "Pet not found"
    
    def add_record(self, pet_name: str, food: str, quantity: str, notes: str = "") -> Tuple[bool, str]:
        if not any(p['name'].lower() == pet_name.lower() for p in self.pets):
            return False, "Pet not found"
        qty, err = Validator.positive_float(quantity, "Quantity")
        if err:
            return False, err
        self.records.insert(0, {
            'time': DateHelper.now_str(),
            'pet': pet_name,
            'food': food,
            'quantity': qty,
            'notes': notes
        })
        self._save()
        return True, "✓ Feeding logged!"
    
    def get_stats(self) -> Dict:
        """Calculate statistics"""
        total_pets = len(self.pets)
        total_feedings = len(self.records)
        
        # Feedings today
        today = datetime.now().date()
        feedings_today = len([r for r in self.records 
                             if DateHelper.from_str(r['time']).date() == today])
        
        # Most fed pet
        if self.records:
            pet_counts = {}
            for r in self.records:
                pet = r.get('pet', 'Unknown')
                pet_counts[pet] = pet_counts.get(pet, 0) + 1
            most_fed = max(pet_counts, key=pet_counts.get) if pet_counts else "N/A"
        else:
            most_fed = "N/A"
        
        return {
            'total_pets': total_pets,
            'total_feedings': total_feedings,
            'feedings_today': feedings_today,
            'most_fed': most_fed
        }

class VetManager(JSONManager):
    def __init__(self):
        super().__init__("vet_data.json")
    
    def add_appointment(self, data: Dict) -> int:
        appt_id = max([a.get('id', 0) for a in self.data], default=0) + 1
        data['id'] = appt_id
        data['status'] = 'Scheduled'
        data['created_at'] = datetime.now().isoformat()
        self.data.insert(0, data)
        self.save()
        return appt_id
    
    def update_appointment(self, appt_id: int, notes: str, cost: float) -> bool:
        for appt in self.data:
            if appt['id'] == appt_id:
                appt['notes'] = notes
                appt['cost'] = cost
                appt['status'] = 'Completed'
                appt['completed_at'] = datetime.now().isoformat()
                self.save()
                return True
        return False
    
    def delete_appointment(self, appt_id: int) -> bool:
        original = len(self.data)
        self.data = [a for a in self.data if a['id'] != appt_id]
        if len(self.data) < original:
            self.save()
            return True
        return False
    
    def get_stats(self) -> Dict:
        total = len(self.data)
        scheduled = len([a for a in self.data if a.get('status') == 'Scheduled'])
        completed = total - scheduled
        
        # This week's appointments
        today = datetime.now().date()
        week_start = today - timedelta(days=today.weekday())
        week_end = week_start + timedelta(days=7)
        
        this_week = len([a for a in self.data 
                        if week_start <= DateHelper.from_str(a['date']).date() < week_end])
        
        return {
            'total': total,
            'scheduled': scheduled,
            'completed': completed,
            'this_week': this_week
        }

class GroomingManager(JSONManager):
    def __init__(self):
        super().__init__("grooming_data.json")
    
    def _safe_existing_slots(self):
        """Yield (start_dt, end_dt, item) for conflict checks, tolerating old data."""
        for item in self.data:
            try:
                s = datetime.fromisoformat(item.get('start_iso') or item['start'])
                e = datetime.fromisoformat(item.get('end_iso') or item['end'])
                yield s, e, item
            except Exception:
                continue

    def add_task(self, start_str: str, duration: int, task: str, pet: str = "") -> Tuple[bool, str]:
        try:
            start = DateHelper.from_str(start_str)
            end = start + timedelta(minutes=duration)

            # Check for conflicts (robust to legacy records)
            for s, e, item in self._safe_existing_slots():
                if start < e and end > s:
                    return False, f"⚠️ Time conflict with: {item.get('task', 'existing task')}"

            self.data.append({
                'id': str(uuid.uuid4()),
                'start': start.strftime(DateHelper.DISPLAY_FMT),
                'end': end.strftime(DateHelper.DISPLAY_FMT),
                'start_iso': start.isoformat(),
                'end_iso': end.isoformat(),
                'duration': duration,
                'task': task,
                'pet': pet,
                'created_at': datetime.now().isoformat()
            })
            self.data.sort(key=lambda x: x['start_iso'])
            self.save()
            return True, "✓ Task scheduled!"
        except Exception as e:
            return False, f"Error: {str(e)}"
    
    def delete_task(self, task_id: str) -> bool:
        original = len(self.data)
        self.data = [t for t in self.data if t['id'] != task_id]
        if len(self.data) < original:
            self.save()
            return True
        return False
    
    def get_stats(self) -> Dict:
        total = len(self.data)
        
        # Upcoming tasks (future)
        now = datetime.now()
        upcoming = 0
        today_tasks = 0
        today = now.date()

        for t in self.data:
            start_iso = t.get('start_iso') or t.get('start')
            if not start_iso:
                continue
            try:
                dt = datetime.fromisoformat(start_iso)
                if dt > now:
                    upcoming += 1
                if dt.date() == today:
                    today_tasks += 1
            except Exception:
                continue
        
        return {
            'total': total,
            'upcoming': upcoming,
            'today': today_tasks
        }

# ============================================================================
# CROSS-TAB LINKING
# ============================================================================

class AppBridge:
    """Enables communication between tabs"""
    def __init__(self):
        self.callbacks = {}
    
    def register(self, event_name, callback):
        if event_name not in self.callbacks:
            self.callbacks[event_name] = []
        self.callbacks[event_name].append(callback)
    
    def emit(self, event_name, data=None):
        if event_name in self.callbacks:
            for callback in self.callbacks[event_name]:
                callback(data)

# ============================================================================
# ENHANCED GUI COMPONENTS
# ============================================================================

class ModernButton(tk.Button):
    """Enhanced button with animations and better styling"""
    def __init__(self, parent, text, command, style='primary', icon='', **kw):
        colors = {
            'primary': (Config.COLORS['primary'], Config.COLORS['primary_dark'], 'white'),
            'success': (Config.COLORS['success'], Config.COLORS['success_light'], 'white'),
            'danger': (Config.COLORS['danger'], Config.COLORS['danger_light'], 'white'),
            'secondary': (Config.COLORS['hover'], Config.COLORS['border'], Config.COLORS['text']),
            'accent': (Config.COLORS['accent'], Config.COLORS['accent_light'], 'white'),
            'warning': (Config.COLORS['warning'], Config.COLORS['warning_light'], 'white'),
            'light': ('white', Config.COLORS['hover'], 'black')  # ADD THIS LINE - white bg, black text
        }
        bg, hover, fg = colors.get(style, colors['primary'])
        
        super().__init__(
            parent, text=f"{icon} {text}" if icon else text, command=command,
            bg=bg, fg=fg, font=Config.FONTS['button'], relief='flat', 
            cursor='hand2', padx=Config.PADDING['md'], pady=Config.PADDING['sm'],
            borderwidth=0, **kw
        )
        self.def_bg, self.hov_bg = bg, hover
        self.bind('<Enter>', self._on_enter)
        self.bind('<Leave>', self._on_leave)
        self.bind('<ButtonPress-1>', self._on_press)
        self.bind('<ButtonRelease-1>', self._on_release)
    
    def _on_enter(self, e):
        self.config(bg=self.hov_bg)
    
    def _on_leave(self, e):
        self.config(bg=self.def_bg)
    
    def _on_press(self, e):
        self.config(relief='sunken')
    
    def _on_release(self, e):
        self.config(relief='flat')
    
class ModernCard(tk.Frame):
    """Enhanced card with shadow effect"""
    def __init__(self, parent, title='', **kw):
        super().__init__(parent, bg=Config.COLORS['card'], relief='flat', **kw)
        
        # Shadow effect styling
        self.configure(
            highlightbackground=Config.COLORS['border'],
            highlightthickness=1,
            bd=2,
            relief='raised'
        )
        
        if title:
            header = tk.Frame(self, bg=Config.COLORS['card'])
            header.pack(fill='x', padx=Config.PADDING['md'], 
                       pady=(Config.PADDING['md'], Config.PADDING['xs']))
            
            tk.Label(
                header, text=title, font=Config.FONTS['subtitle'],
                bg=Config.COLORS['card'], fg=Config.COLORS['text'], anchor='w'
            ).pack(side='left', fill='x', expand=True)
            
            # Decorative line
            separator = tk.Frame(self, bg=Config.COLORS['primary_light'], height=2)
            separator.pack(fill='x', padx=Config.PADDING['md'])

class StatCard(tk.Frame):
    """Animated statistics card"""
    def __init__(self, parent, label, value, icon, color):
        super().__init__(parent, bg=Config.COLORS['card'], 
                        highlightbackground=Config.COLORS['border'],
                        highlightthickness=1, relief='flat')
        
        self.pack_propagate(False)
        self.config(height=110) # slightly taller to accommodate text
        
        # Icon
        self.icon_label = tk.Label(
            self, text=icon, font=('Segoe UI', 28),
            bg=Config.COLORS['card'], fg=color
        )
        self.icon_label.pack(pady=(Config.PADDING['sm'], 0))
        
        # Value
        self.value_label = tk.Label(
            self, text=str(value), font=('Segoe UI', 20, 'bold'),
            bg=Config.COLORS['card'], fg=color
        )
        self.value_label.pack()
        
        # Label
        self.label_label = tk.Label(
            self, text=label, font=Config.FONTS['small'],
            bg=Config.COLORS['card'], fg=Config.COLORS['text_light']
        )
        self.label_label.pack()
        
        # Hover effect
        self.bind('<Enter>', lambda e: self.config(bg=Config.COLORS['hover']))
        self.bind('<Leave>', lambda e: self.config(bg=Config.COLORS['card']))
        for widget in [self.icon_label, self.value_label, self.label_label]:
            widget.bind('<Enter>', lambda e: self.config(bg=Config.COLORS['hover']))
            widget.bind('<Leave>', lambda e: self.config(bg=Config.COLORS['card']))

class SearchBar(tk.Frame):
    """Enhanced search bar with clear button"""
    def __init__(self, parent, placeholder="Search...", command=None):
        super().__init__(parent, bg=Config.COLORS['card'])
        
        self.command = command
        self.placeholder = placeholder
        self.placeholder_active = True
        
        # Search icon
        tk.Label(
            self, text="🔍", font=('Segoe UI', 12),
            bg=Config.COLORS['card']
        ).pack(side='left', padx=(Config.PADDING['sm'], 0))
        
        # Entry
        self.entry = tk.Entry(
            self, font=Config.FONTS['body'], relief='flat',
            bg=Config.COLORS['card'], fg=Config.COLORS['text_muted'],
            borderwidth=0, insertbackground=Config.COLORS['text']
        )
        self.entry.pack(side='left', fill='x', expand=True, padx=Config.PADDING['xs'])
        self.entry.insert(0, placeholder)
        
        # Clear button
        self.clear_btn = tk.Label(
            self, text="✕", font=('Segoe UI', 10),
            bg=Config.COLORS['card'], fg=Config.COLORS['text_light'],
            cursor='hand2'
        )
        self.clear_btn.pack(side='right', padx=Config.PADDING['sm'])
        self.clear_btn.bind('<Button-1>', self.clear)
        
        # Bindings
        self.entry.bind('<FocusIn>', self._on_focus_in)
        self.entry.bind('<FocusOut>', self._on_focus_out)
        self.entry.bind('<KeyRelease>', self._on_key_release)
        
        # Border
        self.configure(
            highlightbackground=Config.COLORS['border'],
            highlightthickness=1
        )
    
    def _on_focus_in(self, e):
        if self.placeholder_active:
            self.entry.delete(0, 'end')
            self.entry.config(fg=Config.COLORS['text'])
            self.placeholder_active = False
    
    def _on_focus_out(self, e):
        if not self.entry.get():
            self.entry.insert(0, self.placeholder)
            self.entry.config(fg=Config.COLORS['text_muted'])
            self.placeholder_active = True
    
    def _on_key_release(self, e):
        if self.command and not self.placeholder_active:
            self.command(self.get())
    
    def get(self):
        return "" if self.placeholder_active else self.entry.get()
    
    def clear(self, e=None):
        self.entry.delete(0, 'end')
        self.entry.focus()
        if self.command:
            self.command("")

def create_table(parent, columns, widths=None, show_index=True):
    """Enhanced table with better styling"""
    style = ttk.Style(parent)
    # Define the custom style before using it
    style.configure(
        "Custom.Treeview",
        rowheight=30,
        font=Config.FONTS['body'],
        background=Config.COLORS['card'],
        fieldbackground=Config.COLORS['card'],
        foreground=Config.COLORS['text']
    )
    style.configure(
        "Custom.Treeview.Heading",
        font=Config.FONTS['label'],
        background=Config.COLORS['border'],
        foreground=Config.COLORS['text']
    )

    tree = ttk.Treeview(parent, columns=columns, show='headings',
                        selectmode='browse', style="Custom.Treeview")

    for col in columns:
        tree.heading(col, text=col)
        w = widths.get(col, 120) if widths else 120
        tree.column(col, width=w, anchor='w' if col != 'ID' else 'center')
    
    # Scrollbars
    vsb = ttk.Scrollbar(parent, orient="vertical", command=tree.yview)
    hsb = ttk.Scrollbar(parent, orient="horizontal", command=tree.xview)
    tree.configure(yscrollcommand=vsb.set, xscrollcommand=hsb.set)
    
    tree.grid(row=0, column=0, sticky='nsew')
    vsb.grid(row=0, column=1, sticky='ns')
    hsb.grid(row=1, column=0, sticky='ew')
    
    parent.grid_rowconfigure(0, weight=1)
    parent.grid_columnconfigure(0, weight=1)
    
    return tree

def create_labeled_entry(parent, label, row, icon='', width=25, entry_type='entry'):
    """Create labeled entry with icon"""
    lbl_frame = tk.Frame(parent, bg=Config.COLORS['card'])
    lbl_frame.grid(row=row, column=0, sticky='nw', pady=Config.PADDING['sm'])
    
    if icon:
        tk.Label(lbl_frame, text=icon, font=('Segoe UI', 11),
                 bg=Config.COLORS['card']).pack(side='left', padx=(0, 4))
    
    tk.Label(lbl_frame, text=label, font=Config.FONTS['label'],
             bg=Config.COLORS['card'], fg=Config.COLORS['text']).pack(side='left')
    
    border_kwargs = {
        'bg': Config.COLORS['input_bg'],
        'font': Config.FONTS['body'],
        'relief': 'solid',
        'borderwidth': 1,              # thin border
        'highlightthickness': 1,       # thin focus border
        'highlightbackground': Config.COLORS['border'],
        'highlightcolor': Config.COLORS['primary_light'],
    }
    
    if entry_type == 'text':
        widget = scrolledtext.ScrolledText(
            parent, width=width, height=4, **border_kwargs
        )
    else:
        widget = tk.Entry(parent, width=width, **border_kwargs)
    
    widget.grid(row=row, column=1, sticky='ew', pady=Config.PADDING['sm'],
                padx=(Config.PADDING['sm'], 0))
    
    return widget

def create_labeled_combobox(parent, label, row, icon='', values=(), width=22):
    lbl_frame = tk.Frame(parent, bg=Config.COLORS['card'])
    lbl_frame.grid(row=row, column=0, sticky='nw', pady=Config.PADDING['sm'])
    if icon:
        tk.Label(lbl_frame, text=icon, font=('Segoe UI', 11),
                 bg=Config.COLORS['card']).pack(side='left', padx=(0, 4))
    tk.Label(lbl_frame, text=label, font=Config.FONTS['label'],
             bg=Config.COLORS['card'], fg=Config.COLORS['text']).pack(side='left')
    cb = ttk.Combobox(parent, values=values, width=width,
                      state='readonly', font=Config.FONTS['body'])
    cb.grid(row=row, column=1, sticky='ew', pady=Config.PADDING['sm'],
            padx=(Config.PADDING['sm'], 0))
    return cb

def ensure_tree_has_rows(tree, placeholder="No data"):
    if not tree.get_children():
        cols = tree["columns"]
        vals = [placeholder] + [''] * (len(cols) - 1)
        tree.insert("", "end", values=vals, tags=('empty',))
        tree.tag_configure('empty', foreground=Config.COLORS['text_muted'])

class Toast:
    """Toast notification"""
    @staticmethod
    def show(parent, message, duration=2000, type='info'):
        colors = {
            'info': (Config.COLORS['accent'], 'white'),
            'success': (Config.COLORS['success'], 'white'),
            'error': (Config.COLORS['danger'], 'white'),
            'warning': (Config.COLORS['warning'], 'white')
        }
        bg, fg = colors.get(type, colors['info'])
        
        toast = tk.Toplevel(parent)
        toast.overrideredirect(True)
        toast.attributes('-alpha', 0.95)
        
        frame = tk.Frame(toast, bg=bg, padx=20, pady=12)
        frame.pack()
        
        tk.Label(frame, text=message, font=Config.FONTS['body'],
                bg=bg, fg=fg).pack()
        
        # Position
        toast.update_idletasks()
        try:
            x = parent.winfo_rootx() + (parent.winfo_width() - toast.winfo_width()) // 2
            y = parent.winfo_rooty() + parent.winfo_height() - 100
        except:
            x = 100
            y = 100
        toast.geometry(f"+{x}+{y}")
        
        toast.after(duration, toast.destroy)

# ============================================================================
# TABS
# ============================================================================

class FeedingTab(ttk.Frame):
    def __init__(self, parent, bridge=None):
        super().__init__(parent)
        self.manager = FeedingManager()
        self.search_term = ""
        self.bridge = bridge
        self._build_ui()
        self._emit_pet_list()
    
    def _build_ui(self):
        container = tk.Frame(self, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Stats Row
        stats_frame = tk.Frame(container, bg=Config.COLORS['bg'])
        stats_frame.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        self.stat_cards = []
        for i in range(4):
            card = StatCard(stats_frame, "", 0, "", Config.COLORS['primary'])
            card.pack(side='left', fill='x', expand=True, padx=(0 if i == 0 else Config.PADDING['sm'], 0))
            self.stat_cards.append(card)
        
        # Main Content
        content = tk.Frame(container, bg=Config.COLORS['bg'])
        content.pack(fill='both', expand=True)
        
        # Left Panel
        left = tk.Frame(content, bg=Config.COLORS['bg'], width=340)
        left.pack(side='left', fill='y', padx=(0, Config.PADDING['md']))
        left.pack_propagate(False)
        
        # Add Pet Card
        self._create_add_pet_card(left)
        
        # Log Feeding Card
        self._create_log_feeding_card(left)
        
        # Right Panel
        right = tk.Frame(content, bg=Config.COLORS['bg'])
        right.pack(side='right', fill='both', expand=True)
        
        # History Card
        self._create_history_card(right)
        
        self.refresh()
    
    def _create_add_pet_card(self, parent):
        card = ModernCard(parent, '➕ Add New Pet')
        card.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        form = tk.Frame(card, bg=Config.COLORS['card'])
        form.pack(fill='x', padx=Config.PADDING['lg'], pady=Config.PADDING['md'])
        
        self.name_e = create_labeled_entry(form, "Pet Name:", 0, '🏷️', 22)
        
        # Type dropdown with icon
        tk.Label(form, text='🐾 Pet Type:', font=Config.FONTS['label'], 
                bg=Config.COLORS['card']).grid(row=1, column=0, sticky='w', 
                                               pady=Config.PADDING['sm'])
        self.type_var = tk.StringVar(value="Dog")
        self.type_cb = ttk.Combobox(form, textvariable=self.type_var,
                                    values=["Dog", "Cat", "Bird", "Rabbit", "Hamster", "Other"],
                                    state='readonly', width=20, font=Config.FONTS['body'])
        self.type_cb.grid(row=1, column=1, sticky='ew', pady=Config.PADDING['sm'],
                         padx=(Config.PADDING['sm'], 0))
        
        self.age_e = create_labeled_entry(form, "Age (years):", 2, '🎂', 22)
        self.breed_e = create_labeled_entry(form, "Breed:", 3, '🧬', 22)
        
        form.grid_columnconfigure(1, weight=1)
        
        btn_frame = tk.Frame(form, bg=Config.COLORS['card'])
        btn_frame.grid(row=4, column=0, columnspan=2, pady=(Config.PADDING['md'], 0))
        
        # CHANGE: Remove icons and use 'light' style for black text
        ModernButton(btn_frame, "Add Pet", self.add_pet, 'light').pack(side='left', padx=2, fill='x', expand=True)
        ModernButton(btn_frame, "Clear", self.clear_add_form, 'light').pack(side='left', padx=2, fill='x', expand=True)
    
    def _create_log_feeding_card(self, parent):
        card = ModernCard(parent, '📝 Log Feeding')
        card.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        form = tk.Frame(card, bg=Config.COLORS['card'])
        form.pack(fill='x', padx=Config.PADDING['lg'], pady=Config.PADDING['md'])
        
        tk.Label(form, text='🐾 Select Pet:', font=Config.FONTS['label'],
                bg=Config.COLORS['card']).grid(row=0, column=0, sticky='w',
                                               pady=Config.PADDING['sm'])
        self.pet_var = tk.StringVar()
        self.pet_cb = ttk.Combobox(form, textvariable=self.pet_var,
                                   state='readonly', width=20, font=Config.FONTS['body'])
        self.pet_cb.grid(row=0, column=1, sticky='ew', pady=Config.PADDING['sm'],
                        padx=(Config.PADDING['sm'], 0))
        
        self.food_e = create_labeled_entry(form, "Food Type:", 1, '🍖', 22)
        self.qty_e = create_labeled_entry(form, "Quantity (g):", 2, '⚖️', 22)
        self.notes_e = create_labeled_entry(form, "Notes:", 3, '📝', 22, 'text')
        
        form.grid_columnconfigure(1, weight=1)
        
        # CHANGE: Remove icon and use 'light' style for black text
        ModernButton(form, "Log Feeding", self.log_feeding, 'light'
                    ).grid(row=4, column=0, columnspan=2, pady=(Config.PADDING['md'], 0), sticky='ew')
    
    def _create_history_card(self, parent):
        card = ModernCard(parent, '📊 Feeding History')
        card.pack(fill='both', expand=True)
        
        # Toolbar
        toolbar = tk.Frame(card, bg=Config.COLORS['card'])
        toolbar.pack(fill='x', padx=Config.PADDING['md'], pady=Config.PADDING['sm'])
        
        # Search bar
        search = SearchBar(toolbar, "Search by pet or food...", self.on_search)
        search.pack(side='left', fill='x', expand=True, padx=(0, Config.PADDING['sm']))
        
        ModernButton(toolbar, "Refresh", self.refresh, 'secondary', '🔄').pack(side='right', padx=2)
        
        # Table
        table_frame = tk.Frame(card, bg=Config.COLORS['card'])
        table_frame.pack(fill='both', expand=True, padx=Config.PADDING['md'],
                        pady=(0, Config.PADDING['md']))
        
        self.tree = create_table(table_frame, ("Time", "Pet", "Food", "Qty", "Notes"),
                                {'Time': 140, 'Pet': 120, 'Food': 150, 'Qty': 80, 'Notes': 200})
    
    def add_pet(self):
        name = self.name_e.get().strip()
        # ...existing validation...
        success, msg = self.manager.add_pet(
            name, self.type_var.get(), age, self.breed_e.get().strip()
        )
        if success:
            Toast.show(self, msg, type='success')
            if self.bridge:
                self._emit_pet_list()
            self.clear_add_form()
            self.refresh()
        else:
            messagebox.showerror("Error", msg)

    def _emit_pet_list(self):
        if self.bridge:
            self.bridge.emit('pet_list', [p['name'] for p in self.manager.pets])
    
    def log_feeding(self):
        pet = self.pet_var.get()
        food = self.food_e.get().strip()
        qty = self.qty_e.get().strip()
        # FIX: ScrolledText uses .get("1.0", "end-1c") not .get()
        notes = self.notes_e.get("1.0", "end-1c").strip()
        
        if not all([pet, food, qty]):
            messagebox.showwarning("Required Fields", "Please fill all required fields")
            return
        
        success, msg = self.manager.add_record(pet, food, qty, notes)
        
        if success:
            Toast.show(self, msg, type='success')
            self.food_e.delete(0, 'end')
            self.qty_e.delete(0, 'end')
            self.notes_e.delete("1.0", "end")  # FIX: use "1.0", "end" for ScrolledText
            self.refresh()
        else:
            messagebox.showerror("Error", msg)
    
    def clear_add_form(self):
        self.name_e.delete(0, 'end')
        self.age_e.delete(0, 'end')
        self.breed_e.delete(0, 'end')
        self.type_var.set("Dog")
    
    def on_search(self, term):
        self.search_term = term.lower()
        self.refresh_table()
    
    def refresh_table(self):
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        for r in self.manager.records:
            # Safe get methods
            pet_name = r.get('pet', '').lower()
            food_name = r.get('food', '').lower()
            
            if not self.search_term or \
               self.search_term in pet_name or \
               self.search_term in food_name:
                self.tree.insert("", "end", values=(
                    r.get('time', ''), r.get('pet', ''), r.get('food', ''), 
                    f"{r.get('quantity', 0)}g", r.get('notes', '')
                ))
        
        ensure_tree_has_rows(self.tree)
    
    def refresh(self):
        # Update pet dropdown
        pets = [p['name'] for p in self.manager.pets]
        self.pet_cb['values'] = pets
        if pets and not self.pet_var.get():
            self.pet_cb.current(0)
        
        # Update table
        self.refresh_table()
        
        # Update stats
        stats = self.manager.get_stats()
        stat_data = [
            ("Total Pets", stats['total_pets'], "🐾", Config.COLORS['primary']),
            ("Total Feedings", stats['total_feedings'], "📝", Config.COLORS['accent']),
            ("Today", stats['feedings_today'], "📅", Config.COLORS['success']),
            ("Most Fed", str(stats['most_fed'])[:10], "⭐", Config.COLORS['warning'])
        ]
        
        for card, (label, value, icon, color) in zip(self.stat_cards, stat_data):
            for widget in card.winfo_children():
                widget.destroy()
            
            tk.Label(card, text=icon, font=('Segoe UI', 28),
                    bg=Config.COLORS['card'], fg=color).pack(pady=(Config.PADDING['sm'], 0))
            tk.Label(card, text=str(value), font=('Segoe UI', 20, 'bold'),
                    bg=Config.COLORS['card'], fg=color).pack()
            tk.Label(card, text=label, font=Config.FONTS['small'],
                    bg=Config.COLORS['card'], fg=Config.COLORS['text_light']).pack()

class VetTab(ttk.Frame):
    def __init__(self, parent, bridge=None):
        super().__init__(parent)
        self.manager = VetManager()
        self.bridge = bridge  # Add bridge reference
        self._build_ui()
        
        # Listen for pet additions
        if self.bridge:
            self.bridge.register('pet_added', self._on_pet_added)
    
    def _on_pet_added(self, data):
        """Auto-populate pet field when pet is added"""
        # This can auto-fill vet appointment with new pet
        pass
    
    def _build_ui(self):
        container = tk.Frame(self, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Stats Row
        self.stats_frame = tk.Frame(container, bg=Config.COLORS['bg'])
        self.stats_frame.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        self._update_stats()
        
        # Notebook
        nb = ttk.Notebook(container)
        nb.pack(fill='both', expand=True)
        
        list_tab = ttk.Frame(nb)
        add_tab = ttk.Frame(nb)
        
        nb.add(list_tab, text=" 📋 Appointments List ")
        nb.add(add_tab, text=" ➕ New Appointment ")
        
        self._setup_list(list_tab)
        self._setup_add(add_tab)
    
    def _update_stats(self):
        for widget in self.stats_frame.winfo_children():
            widget.destroy()
        
        stats = self.manager.get_stats()
        
        stat_data = [
            ("Total", stats['total'], "📊", Config.COLORS['primary']),
            ("Scheduled", stats['scheduled'], "⏰", Config.COLORS['accent']),
            ("Completed", stats['completed'], "✅", Config.COLORS['success']),
            ("This Week", stats['this_week'], "📅", Config.COLORS['warning'])
        ]
        
        for label, value, icon, color in stat_data:
            card = StatCard(self.stats_frame, label, value, icon, color)
            card.pack(side='left', fill='x', expand=True, 
                     padx=(0, Config.PADDING['sm']))
    
    def _setup_list(self, parent):
        container = tk.Frame(parent, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Toolbar
        toolbar = tk.Frame(container, bg=Config.COLORS['card'], height=60)
        toolbar.pack(fill='x', pady=(0, Config.PADDING['sm']))
        toolbar.pack_propagate(False)
        
        btn_container = tk.Frame(toolbar, bg=Config.COLORS['card'])
        btn_container.pack(side='right', padx=Config.PADDING['md'], pady=Config.PADDING['sm'])
        
        # CHANGE: Remove icons and use 'light' style for black text
        ModernButton(btn_container, "Refresh", self.refresh, 'light').pack(side='right', padx=2)
        ModernButton(btn_container, "Update", self.update_dlg, 'light').pack(side='right', padx=2)
        ModernButton(btn_container, "Delete", self.delete, 'light').pack(side='right', padx=2)
        
        # Table
        table_frame = tk.Frame(container, bg=Config.COLORS['card'])
        table_frame.pack(fill='both', expand=True)
        
        self.tree = create_table(table_frame, 
                                ("ID", "Date", "Pet", "Owner", "Vet", "Status"),
                                {'ID': 60, 'Date': 140, 'Pet': 120, 'Owner': 150, 
                                 'Vet': 120, 'Status': 100})
        self.refresh()
    
    def _setup_add(self, parent):
        container = tk.Frame(parent, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['xl'], pady=Config.PADDING['xl'])
        
        card = ModernCard(container, '➕ Book New Appointment')
        card.pack(fill='both', expand=True)
        
        form = tk.Frame(card, bg=Config.COLORS['card'])
        form.pack(fill='both', padx=Config.PADDING['xl'], pady=Config.PADDING['lg'])
        
        self.inputs = {}
        fields = [
            ("Owner Name", "👤", 30),
            ("Owner Phone", "📞", 30),
            ("Pet Name", "🐾", 30),
            ("Species", "🦴", 30),
            ("Date", "📅", 30),
            ("Reason", "📝", 30),
            ("Vet Name", "👨‍⚕️", 30)
        ]
        
        for i, (field, icon, width) in enumerate(fields):
            self.inputs[field] = create_labeled_entry(form, f"{field}:", i, icon, width)
        
        form.grid_columnconfigure(1, weight=1)
        self.inputs["Date"].insert(0, DateHelper.now_str())
        
        btn_frame = tk.Frame(form, bg=Config.COLORS['card'])
        btn_frame.grid(row=len(fields), column=0, columnspan=2, pady=(Config.PADDING['lg'], 0))
        
        # CHANGE: Remove icons and use 'light' style for black text
        ModernButton(btn_frame, "Book Appointment", self.save, 'light').pack(side='left', padx=2, fill='x', expand=True)
        ModernButton(btn_frame, "Clear Form", self.clear_form, 'light').pack(side='left', padx=2, fill='x', expand=True)
    
    def save(self):
        data = {k: v.get() for k, v in self.inputs.items()}
        
        if not data["Pet Name"] or not data["Owner Name"]:
            messagebox.showerror("Required Fields", "Pet and Owner names are required")
            return
        
        appt_id = self.manager.add_appointment({
            'owner': {'name': data['Owner Name'], 'phone': data['Owner Phone']},
            'pet': {'name': data['Pet Name'], 'species': data['Species']},
            'date': data['Date'],
            'reason': data['Reason'],
            'vet': data['Vet Name'],
            'notes': '',
            'cost': 0.0
        })
        
        Toast.show(self, f"✓ Appointment #{appt_id} booked!", type='success')
        self._update_stats()
        self.refresh()
        self.clear_form()
    
    def clear_form(self):
        for e in self.inputs.values():
            e.delete(0, 'end')
        self.inputs["Date"].insert(0, DateHelper.now_str())
    
    def refresh(self):
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        for a in self.manager.data:
            # Color code by status
            tags = ('scheduled',) if a.get('status') == 'Scheduled' else ('completed',)
            self.tree.insert("", "end", values=(
                a['id'], a['date'], a['pet']['name'],
                a['owner']['name'], a['vet'], a.get('status', 'Scheduled')
            ), tags=tags)
        
        # Tag configuration
        self.tree.tag_configure('scheduled', foreground=Config.COLORS['accent'])
        self.tree.tag_configure('completed', foreground=Config.COLORS['success'])
    
    def update_dlg(self):
        sel = self.tree.selection()
        if not sel:
            messagebox.showwarning("No Selection", "Please select an appointment to update")
            return
        
        try:
            appt_id = int(self.tree.item(sel[0])['values'][0])
        except (IndexError, ValueError):
            messagebox.showerror("Error", "Invalid selection")
            return
        
        dlg = tk.Toplevel(self)
        dlg.title(f"Update Appointment #{appt_id}")
        dlg.geometry("600x500")
        dlg.configure(bg=Config.COLORS['bg'])
        dlg.transient(self)
        dlg.grab_set()
        
        # Center dialog
        dlg.update_idletasks()
        x = self.winfo_rootx() + (self.winfo_width() - dlg.winfo_width()) // 2
        y = self.winfo_rooty() + (self.winfo_height() - dlg.winfo_height()) // 2
        dlg.geometry(f"+{x}+{y}")
        
        card = ModernCard(dlg, f'✏️ Update Appointment #{appt_id}')
        card.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        form = tk.Frame(card, bg=Config.COLORS['card'])
        form.pack(fill='both', padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        tk.Label(form, text="Clinical Notes:", font=Config.FONTS['label'],
                bg=Config.COLORS['card']).pack(anchor='w', pady=(5, 3))
        notes_txt = scrolledtext.ScrolledText(form, height=12, width=60,
                                              font=Config.FONTS['body'], relief='solid', borderwidth=1)
        notes_txt.pack(fill='both', expand=True, pady=(0, Config.PADDING['md']))
        
        tk.Label(form, text="Cost ($):", font=Config.FONTS['label'],
                bg=Config.COLORS['card']).pack(anchor='w', pady=(5, 3))
        cost_e = tk.Entry(form, font=Config.FONTS['body'], relief='solid', borderwidth=1)
        cost_e.pack(fill='x', pady=(0, Config.PADDING['md']))
        cost_e.insert(0, "0.00")
        
        def save():
            try:
                cost_val = float(cost_e.get() or 0)
                if cost_val < 0:
                    messagebox.showerror("Invalid Cost", "Cost cannot be negative")
                    return
                    
                if self.manager.update_appointment(appt_id, notes_txt.get("1.0", "end-1c"), cost_val):
                    Toast.show(self, "✓ Appointment updated!", type='success')
                    self._update_stats()
                    self.refresh()
                    dlg.destroy()
            except ValueError:
                messagebox.showerror("Invalid Input", "Please enter a valid cost")
        
        btn_frame = tk.Frame(form, bg=Config.COLORS['card'])
        btn_frame.pack(fill='x')
        
        # CHANGE: Remove icons and use 'light' style for black text
        ModernButton(btn_frame, "Save & Complete", save, 'light').pack(side='left', fill='x', expand=True, padx=2)
        ModernButton(btn_frame, "Cancel", dlg.destroy, 'light').pack(side='left', fill='x', expand=True, padx=2)
    
    def delete(self):
        sel = self.tree.selection()
        if not sel:
            messagebox.showwarning("No Selection", "Please select an appointment to delete")
            return
        
        appt_id = self.tree.item(sel[0])['values'][0]
        
        if messagebox.askyesno("Confirm Delete", 
                              f"Are you sure you want to delete appointment #{appt_id}?\n\nThis action cannot be undone."):
            if self.manager.delete_appointment(appt_id):
                Toast.show(self, "✓ Appointment deleted", type='success')
                self._update_stats()
                self.refresh()

class GroomingTab(ttk.Frame):
    def __init__(self, parent, bridge=None):
        super().__init__(parent)
        self.manager = GroomingManager()
        self.bridge = bridge
        self._build_ui()
        if self.bridge:
            self.bridge.register('pet_list', self._on_pet_list)
    
    def _build_ui(self):
        container = tk.Frame(self, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Stats Row
        stats_frame = tk.Frame(container, bg=Config.COLORS['bg'])
        stats_frame.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        # keep references to allow refresh
        self.stat_cards: List[StatCard] = []
        for _ in range(3):
            card = StatCard(stats_frame, "", 0, "", Config.COLORS['primary'])
            card.pack(side='left', fill='x', expand=True, padx=(0, Config.PADDING['sm']))
            self.stat_cards.append(card)
        
        # Add Task Section
        add_card = ModernCard(container, '➕ Schedule Grooming Task')
        add_card.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        form = tk.Frame(add_card, bg=Config.COLORS['card'])
        form.pack(fill='both', padx=Config.PADDING['lg'], pady=Config.PADDING['md'])
        
        self.pet_var = tk.StringVar()
        self.pet_cb = create_labeled_combobox(form, "Pet Name:", 0, '🐾', values=[], width=32)
        self.pet_cb.configure(textvariable=self.pet_var)
        
        self.start_e = create_labeled_entry(form, "Start Time:", 1, '🕐', 35)
        self.start_e.insert(0, DateHelper.now_str())
        self.dur_e = create_labeled_entry(form, "Duration (min):", 2, '⏱️', 35)
        self.dur_e.insert(0, "60")
        self.task_e = create_labeled_entry(form, "Task Description:", 3, '✂️', 35)
        
        form.grid_columnconfigure(1, weight=1)
        
        btn_frame = tk.Frame(form, bg=Config.COLORS['card'])
        btn_frame.grid(row=4, column=0, columnspan=2, pady=(Config.PADDING['md'], 0))
        
        ModernButton(btn_frame, "Schedule Task", self.add_task, 'success', '✓').pack(side='left', padx=2)
        ModernButton(btn_frame, "Clear Form", self.clear_form, 'secondary', '🔄').pack(side='left', padx=2)
        
        # Task List Section
        list_card = ModernCard(container, '📅 Scheduled Grooming Tasks')
        list_card.pack(fill='both', expand=True)
        
        toolbar = tk.Frame(list_card, bg=Config.COLORS['card'])
        toolbar.pack(fill='x', padx=Config.PADDING['md'], pady=Config.PADDING['sm'])
        
        ModernButton(toolbar, "Delete Selected", self.delete_task, 'danger', '🗑️').pack(side='right', padx=2)
        ModernButton(toolbar, "Refresh", self.refresh, 'secondary', '🔄').pack(side='right', padx=2)
        
        table_frame = tk.Frame(list_card, bg=Config.COLORS['card'])
        table_frame.pack(fill='both', expand=True, padx=Config.PADDING['md'],
                        pady=(0, Config.PADDING['md']))
        
        self.tree = create_table(table_frame,
                                ("Start", "End", "Duration", "Pet", "Task", "ID"),
                                {'Start': 140, 'End': 140, 'Duration': 90,
                                 'Pet': 120, 'Task': 200, 'ID': 0})
        self.tree.column("ID", width=0, stretch=tk.NO)
        
        self.refresh()
    
    def add_task(self):
        task = self.task_e.get().strip()
        pet = self.pet_var.get().strip()
        
        if not task:
            messagebox.showwarning("Required Field", "Please enter a task description")
            return
        
        try:
            dur = int(self.dur_e.get())
            if dur <= 0:
                messagebox.showerror("Invalid Duration", "Duration must be a positive number")
                return
        except ValueError:
            messagebox.showerror("Invalid Input", "Please enter a valid duration in minutes")
            return
        
        success, msg = self.manager.add_task(self.start_e.get(), dur, task, pet)
        
        if success:
            Toast.show(self, msg, type='success')
            self.clear_form()
            self.refresh()
        else:
            messagebox.showerror("Scheduling Error", msg)
    
    def delete_task(self):
        sel = self.tree.selection()
        if not sel:
            messagebox.showwarning("No Selection", "Please select a task to delete")
            return
        
        task_id = self.tree.item(sel[0])['values'][5]
        
        if messagebox.askyesno("Confirm Delete",
                              "Are you sure you want to delete this grooming task?\n\nThis action cannot be undone."):
            if self.manager.delete_task(task_id):
                Toast.show(self, "✓ Task deleted", type='success')
                self.refresh()
    
    def _on_pet_list(self, pets):
        """Receive shared pet list from Feeding tab and refresh dropdown."""
        if not pets:
            self.pet_cb['values'] = []
            self.pet_var.set('')
            return
        self.pet_cb['values'] = pets
        if self.pet_var.get() not in pets:
            self.pet_var.set(pets[0])

    def clear_form(self):
        # reset fields without referencing non-existent pet_e
        if self.pet_cb['values']:
            self.pet_var.set(self.pet_cb['values'][0])
        else:
            self.pet_var.set('')
        self.task_e.delete(0, 'end')
        self.start_e.delete(0, 'end')
        self.start_e.insert(0, DateHelper.now_str())
        self.dur_e.delete(0, 'end')
        self.dur_e.insert(0, "60")
    
    def refresh(self):
        # update table
        for item in self.tree.get_children():
            self.tree.delete(item)

        now = datetime.now()
        for t in self.manager.data:
            # Skip malformed legacy entries gracefully
            start_iso = t.get('start_iso') or t.get('start')
            if not start_iso:
                continue
            try:
                start_dt = datetime.fromisoformat(start_iso)
            except Exception:
                continue

            if start_dt.date() < now.date():
                tags = ('past',)
            elif start_dt.date() == now.date():
                tags = ('today',)
            else:
                tags = ('future',)
            self.tree.insert("", "end", values=(
                t.get('start', ''), t.get('end', ''), f"{t.get('duration', 0)} min",
                t.get('pet', 'N/A'), t.get('task', ''), t.get('id', '')
            ), tags=tags)

        self.tree.tag_configure('past', foreground=Config.COLORS['text_muted'])
        self.tree.tag_configure('today', foreground=Config.COLORS['accent'])
        self.tree.tag_configure('future', foreground=Config.COLORS['success'])

        # update stats cards
        stats = self.manager.get_stats()
        stat_data = [
            ("Total Tasks", stats['total'], "📊", Config.COLORS['primary']),
            ("Upcoming", stats['upcoming'], "⏰", Config.COLORS['accent']),
            ("Today", stats['today'], "📅", Config.COLORS['success'])
        ]
        for card, (label, value, icon, color) in zip(self.stat_cards, stat_data):
            for child in card.winfo_children():
                child.destroy()
            tk.Label(card, text=icon, font=('Segoe UI', 28),
                     bg=Config.COLORS['card'], fg=color).pack(pady=(Config.PADDING['sm'], 0))
            tk.Label(card, text=str(value), font=('Segoe UI', 20, 'bold'),
                     bg=Config.COLORS['card'], fg=color).pack()
            tk.Label(card, text=label, font=Config.FONTS['small'],
                     bg=Config.COLORS['card'], fg=Config.COLORS['text_light']).pack()

        ensure_tree_has_rows(self.tree)

# ============================================================================
# MAIN APPLICATION
# ============================================================================

class MainApp(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title(Config.APP_TITLE)
        self.geometry("1200x800")
        self.configure(bg=Config.COLORS['bg'])
        
        # Create bridge for cross-tab communication
        self.bridge = AppBridge()
        
        # Configure Global Styles
        self._configure_styles()
        
        # Build UI
        self._build_ui()
        
    def _configure_styles(self):
        style = ttk.Style()
        style.theme_use('clam')
        
        # Notebook Style
        style.configure("TNotebook", background=Config.COLORS['bg'], borderwidth=0)
        style.configure("TNotebook.Tab", 
                       padding=[15, 10], 
                       font=('Segoe UI', 11, 'bold'),
                       background=Config.COLORS['border'],
                       foreground=Config.COLORS['text_light'])
        
        style.map("TNotebook.Tab",
                 background=[('selected', Config.COLORS['primary'])],
                 foreground=[('selected', 'white')],
                 expand=[('selected', [1, 1, 1, 0])])
                 
        # Combobox
        style.configure("TCombobox", padding=5)

    def _build_ui(self):
        # Header
        header = tk.Frame(self, bg=Config.COLORS['primary'], height=70)
        header.pack(fill='x')
        header.pack_propagate(False)
        
        tk.Label(header, text="🐾 Ultimate Pet Care Suite", 
                font=('Segoe UI', 22, 'bold'),
                bg=Config.COLORS['primary'], fg='white').pack(side='left', padx=20)
        
        tk.Label(header, text="v2.1", 
                font=('Segoe UI', 10),
                bg=Config.COLORS['primary_dark'], fg='white', padx=8, pady=2
                ).pack(side='right', padx=20)

        # Tabs - Pass bridge to each tab
        notebook = ttk.Notebook(self)
        notebook.pack(fill='both', expand=True, padx=10, pady=(10, 5))
        
        notebook.add(FeedingTab(notebook, self.bridge), text=" 🍖 Feeding Tracker ")
        notebook.add(VetTab(notebook, self.bridge), text=" 🏥 Vet Management ")
        notebook.add(GroomingTab(notebook, self.bridge), text=" ✂️ Grooming Schedule ")
        
        # Footer
        footer = tk.Frame(self, bg=Config.COLORS['bg'], height=30)
        footer.pack(fill='x')
        tk.Label(footer, text="System Ready • Auto-save Enabled • Cross-linked Modules", 
                font=Config.FONTS['small'], bg=Config.COLORS['bg'], 
                fg=Config.COLORS['text_muted']).pack(side='right', padx=10)

if __name__ == "__main__":
    try:
        app = MainApp()
        app.mainloop()
    except Exception as e:
        messagebox.showerror("Fatal Error", f"Application crashed:\n{str(e)}")