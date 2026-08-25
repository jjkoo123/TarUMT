"""
Shared Utilities Module
Contains shared configuration, validators, helpers, and UI components.
"""
import tkinter as tk
from tkinter import ttk, scrolledtext
from datetime import datetime
from typing import Optional, Tuple
import json
from pathlib import Path


class Config:
    """Central configuration for styling and constants."""
    APP_TITLE = "🐾 Ultimate Pet Care Suite"
    
    COLORS = {
        'primary': '#2E7D32', 'primary_light': '#4CAF50', 'primary_dark': '#1B5E20',
        'secondary': '#FF6F00', 'secondary_light': '#FFA726',
        'accent': '#0288D1', 'accent_light': '#4FC3F7',
        'danger': '#D32F2F', 'danger_light': '#EF5350',
        'success': '#388E3C', 'success_light': '#66BB6A',
        'warning': '#F57C00', 'warning_light': '#FFB74D',
        'bg': '#ECEFF1', 'card': '#FFFFFF',
        'text': '#212121', 'text_light': '#616161', 'text_muted': '#9E9E9E',
        'border': '#CFD8DC', 'input_bg': '#FAFAFA', 'hover': '#F5F5F5',
    }
    
    FONTS = {
        'header': ('Segoe UI', 26, 'bold'), 'subtitle': ('Segoe UI', 13, 'bold'),
        'label': ('Segoe UI', 10, 'bold'), 'body': ('Segoe UI', 10),
        'small': ('Segoe UI', 9), 'button': ('Segoe UI', 10, 'bold'), 'mono': ('Courier New', 9),
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
            if num <= 0: return None, f"{name} must be positive"
            return num, None
        except Exception: return None, f"{name} must be a number"

    @staticmethod
    def positive_float(val: str, name: str) -> Tuple[Optional[float], Optional[str]]:
        try:
            # Strip common units like 'g', 'kg', 'ml' if present
            clean_val = str(val).lower()
            for unit in ['g', 'kg', 'ml', 'oz']:
                if clean_val.endswith(unit):
                    clean_val = clean_val[:-len(unit)].strip()
                    break
            
            num = float(clean_val)
            if num < 0: return None, f"{name} cannot be negative"
            return num, None
        except Exception: return None, f"{name} must be a number"


class DateHelper:
    DISPLAY_FMT = "%Y-%m-%d %H:%M"
    SHORT_FMT = "%d/%m/%Y"
    TIME_FMT = "%H:%M"
    FORMATS = [DISPLAY_FMT, "%Y-%m-%d", SHORT_FMT]

    @staticmethod
    def now_str() -> str: return datetime.now().strftime(DateHelper.DISPLAY_FMT)
    @staticmethod
    def now_short() -> str: return datetime.now().strftime(DateHelper.SHORT_FMT)

    @staticmethod
    def parse(dt_str: str) -> Tuple[Optional[datetime], Optional[str]]:
        for fmt in DateHelper.FORMATS:
            try: return datetime.strptime(dt_str.strip(), fmt), None
            except Exception: continue
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
        except Exception as e: print(f"Save error: {e}")


class AppBridge:
    """Enables communication between tabs"""
    def __init__(self):
        self.callbacks = {}
    
    def register(self, event_name, callback):
        if event_name not in self.callbacks: self.callbacks[event_name] = []
        self.callbacks[event_name].append(callback)
    
    def emit(self, event_name, data=None):
        if event_name in self.callbacks:
            for callback in self.callbacks[event_name]: callback(data)


class ModernButton(tk.Button):
    def __init__(self, parent, text, command, style='primary', icon='', **kw):
        colors = {
            'primary': (Config.COLORS['primary'], Config.COLORS['primary_dark'], 'white'),
            'success': (Config.COLORS['success'], Config.COLORS['success_light'], 'white'),
            'danger': (Config.COLORS['danger'], Config.COLORS['danger_light'], 'white'),
            'secondary': (Config.COLORS['hover'], Config.COLORS['border'], Config.COLORS['text']),
            'accent': (Config.COLORS['accent'], Config.COLORS['accent_light'], 'white'),
            'warning': (Config.COLORS['warning'], Config.COLORS['warning_light'], 'white'),
            'light': ('white', Config.COLORS['hover'], 'black')
        }
        bg, hover, fg = colors.get(style, colors['primary'])
        super().__init__(parent, text=f"{icon} {text}" if icon else text, command=command,
            bg=bg, fg=fg, font=Config.FONTS['button'], relief='flat', 
            cursor='hand2', padx=Config.PADDING['md'], pady=Config.PADDING['sm'], borderwidth=0, **kw)
        self.def_bg, self.hov_bg = bg, hover
        self.bind('<Enter>', lambda e: self.config(bg=self.hov_bg))
        self.bind('<Leave>', lambda e: self.config(bg=self.def_bg))
        self.bind('<ButtonPress-1>', lambda e: self.config(relief='sunken'))
        self.bind('<ButtonRelease-1>', lambda e: self.config(relief='flat'))


class ModernCard(tk.Frame):
    def __init__(self, parent, title='', **kw):
        super().__init__(parent, bg=Config.COLORS['card'], relief='flat', **kw)
        self.configure(highlightbackground=Config.COLORS['border'], highlightthickness=1, bd=2, relief='raised')
        if title:
            header = tk.Frame(self, bg=Config.COLORS['card'])
            header.pack(fill='x', padx=Config.PADDING['md'], pady=(Config.PADDING['md'], Config.PADDING['xs']))
            tk.Label(header, text=title, font=Config.FONTS['subtitle'], bg=Config.COLORS['card'], 
                     fg=Config.COLORS['text'], anchor='w').pack(side='left', fill='x', expand=True)
            separator = tk.Frame(self, bg=Config.COLORS['primary_light'], height=2)
            separator.pack(fill='x', padx=Config.PADDING['md'])


class StatCard(tk.Frame):
    def __init__(self, parent, label, value, icon, color):
        super().__init__(parent, bg=Config.COLORS['card'], highlightbackground=Config.COLORS['border'],
                        highlightthickness=1, relief='flat')
        self.pack_propagate(False)
        self.config(height=125)
        self.icon_label = tk.Label(self, text=icon, font=('Segoe UI', 28), bg=Config.COLORS['card'], fg=color)
        self.icon_label.pack(pady=(Config.PADDING['sm'], 0))
        self.value_label = tk.Label(self, text=str(value), font=('Segoe UI', 20, 'bold'), bg=Config.COLORS['card'], fg=color)
        self.value_label.pack()
        self.label_label = tk.Label(self, text=label, font=Config.FONTS['small'], bg=Config.COLORS['card'], fg=Config.COLORS['text_light'])
        self.label_label.pack()
        self.bind('<Enter>', lambda e: self.config(bg=Config.COLORS['hover']))
        self.bind('<Leave>', lambda e: self.config(bg=Config.COLORS['card']))
        for widget in [self.icon_label, self.value_label, self.label_label]:
            widget.bind('<Enter>', lambda e: self.config(bg=Config.COLORS['hover']))
            widget.bind('<Leave>', lambda e: self.config(bg=Config.COLORS['card']))

    def update_data(self, value=None, label=None, icon=None, color=None):
        if icon is not None: self.icon_label.config(text=icon)
        if value is not None: self.value_label.config(text=str(value))
        if label is not None: self.label_label.config(text=label)
        if color is not None:
            self.icon_label.config(fg=color)
            self.value_label.config(fg=color)


class SearchBar(tk.Frame):
    def __init__(self, parent, placeholder="Search...", command=None):
        super().__init__(parent, bg=Config.COLORS['card'])
        self.command, self.placeholder, self.placeholder_active = command, placeholder, True
        tk.Label(self, text="🔍", font=('Segoe UI', 12), bg=Config.COLORS['card']).pack(side='left', padx=(Config.PADDING['sm'], 0))
        self.entry = tk.Entry(self, font=Config.FONTS['body'], relief='flat', bg=Config.COLORS['card'], 
                              fg=Config.COLORS['text_muted'], borderwidth=0, insertbackground=Config.COLORS['text'])
        self.entry.pack(side='left', fill='x', expand=True, padx=Config.PADDING['xs'])
        self.entry.insert(0, placeholder)
        self.clear_btn = tk.Label(self, text="✕", font=('Segoe UI', 10), bg=Config.COLORS['card'], 
                                  fg=Config.COLORS['text_light'], cursor='hand2')
        self.clear_btn.pack(side='right', padx=Config.PADDING['sm'])
        self.clear_btn.bind('<Button-1>', self.clear)
        self.entry.bind('<FocusIn>', self._on_focus_in)
        self.entry.bind('<FocusOut>', self._on_focus_out)
        self.entry.bind('<KeyRelease>', self._on_key_release)
        self.configure(highlightbackground=Config.COLORS['border'], highlightthickness=1)
    
    def _on_focus_in(self, e):
        if self.placeholder_active:
            self.entry.delete(0, 'end'); self.entry.config(fg=Config.COLORS['text']); self.placeholder_active = False
    
    def _on_focus_out(self, e):
        if not self.entry.get():
            self.entry.insert(0, self.placeholder); self.entry.config(fg=Config.COLORS['text_muted']); self.placeholder_active = True
    
    def _on_key_release(self, e):
        if self.command and not self.placeholder_active: self.command(self.get())
    
    def get(self): return "" if self.placeholder_active else self.entry.get()
    def clear(self, e=None): self.entry.delete(0, 'end'); self.entry.focus(); self.command("") if self.command else None


class Toast:
    @staticmethod
    def show(parent, message, duration=2000, type='info'):
        colors = {'info': (Config.COLORS['accent'], 'white'), 'success': (Config.COLORS['success'], 'white'),
                  'error': (Config.COLORS['danger'], 'white'), 'warning': (Config.COLORS['warning'], 'white')}
        bg, fg = colors.get(type, colors['info'])
        toast = tk.Toplevel(parent)
        toast.overrideredirect(True)
        toast.attributes('-alpha', 0.95)
        # Ensure it doesn't stay on top of other apps but stay on top of the parent
        toast.transient(parent)
        
        frame = tk.Frame(toast, bg=bg, padx=20, pady=12)
        frame.pack()
        tk.Label(frame, text=message, font=Config.FONTS['body'], bg=bg, fg=fg).pack()
        toast.update_idletasks()
        
        # Position relative to parent or screen center
        try:
            parent.update_idletasks()
            px = parent.winfo_rootx()
            py = parent.winfo_rooty()
            pw = parent.winfo_width()
            ph = parent.winfo_height()
            
            x = px + (pw - toast.winfo_width()) // 2
            y = py + ph - 100
        except:
            x, y = 100, 100
            
        toast.geometry(f"+{x}+{y}")
        
        # Fade out and destroy
        def fade():
            try:
                alpha = float(toast.attributes('-alpha'))
                if alpha > 0.1:
                    toast.attributes('-alpha', alpha - 0.1)
                    toast.after(50, fade)
                else:
                    toast.destroy()
            except:
                pass

        toast.after(duration, fade)


def create_table(parent, columns, widths=None, show_index=True):
    style = ttk.Style(parent)
    style.configure("Custom.Treeview", rowheight=30, font=Config.FONTS['body'], background=Config.COLORS['card'],
                    fieldbackground=Config.COLORS['card'], foreground=Config.COLORS['text'])
    style.configure("Custom.Treeview.Heading", font=Config.FONTS['label'], background=Config.COLORS['border'],
                    foreground=Config.COLORS['text'])
    tree = ttk.Treeview(parent, columns=columns, show='headings', selectmode='browse', style="Custom.Treeview")
    for col in columns:
        tree.heading(col, text=col)
        w = widths.get(col, 120) if widths else 120
        tree.column(col, width=w, anchor='w' if col != 'ID' else 'center')
    vsb = ttk.Scrollbar(parent, orient="vertical", command=tree.yview)
    hsb = ttk.Scrollbar(parent, orient="horizontal", command=tree.xview)
    tree.configure(yscrollcommand=vsb.set, xscrollcommand=hsb.set)
    tree.grid(row=0, column=0, sticky='nsew'); vsb.grid(row=0, column=1, sticky='ns'); hsb.grid(row=1, column=0, sticky='ew')
    parent.grid_rowconfigure(0, weight=1); parent.grid_columnconfigure(0, weight=1)
    return tree


def create_labeled_entry(parent, label, row, icon='', width=25, entry_type='entry'):
    lbl_frame = tk.Frame(parent, bg=Config.COLORS['card'])
    lbl_frame.grid(row=row, column=0, sticky='nw', pady=Config.PADDING['sm'])
    if icon: tk.Label(lbl_frame, text=icon, font=('Segoe UI', 11), bg=Config.COLORS['card']).pack(side='left', padx=(0, 4))
    tk.Label(lbl_frame, text=label, font=Config.FONTS['label'], bg=Config.COLORS['card'], fg=Config.COLORS['text']).pack(side='left')
    border_kwargs = {'bg': Config.COLORS['input_bg'], 'font': Config.FONTS['body'], 'relief': 'solid', 'borderwidth': 1,
                     'highlightthickness': 1, 'highlightbackground': Config.COLORS['border'], 'highlightcolor': Config.COLORS['primary_light']}
    if entry_type == 'text': widget = scrolledtext.ScrolledText(parent, width=width, height=4, **border_kwargs)
    else: widget = tk.Entry(parent, width=width, **border_kwargs)
    widget.grid(row=row, column=1, sticky='ew', pady=Config.PADDING['sm'], padx=(Config.PADDING['sm'], 0))
    return widget


def create_labeled_combobox(parent, label, row, icon='', values=(), width=22):
    lbl_frame = tk.Frame(parent, bg=Config.COLORS['card'])
    lbl_frame.grid(row=row, column=0, sticky='nw', pady=Config.PADDING['sm'])
    if icon: tk.Label(lbl_frame, text=icon, font=('Segoe UI', 11), bg=Config.COLORS['card']).pack(side='left', padx=(0, 4))
    tk.Label(lbl_frame, text=label, font=Config.FONTS['label'], bg=Config.COLORS['card'], fg=Config.COLORS['text']).pack(side='left')
    cb = ttk.Combobox(parent, values=values, width=width, state='readonly', font=Config.FONTS['body'])
    cb.grid(row=row, column=1, sticky='ew', pady=Config.PADDING['sm'], padx=(Config.PADDING['sm'], 0))
    return cb


def ensure_tree_has_rows(tree, placeholder="No data"):
    if not tree.get_children():
        cols = tree["columns"]
        vals = [placeholder] + [''] * (len(cols) - 1)
        tree.insert("", "end", values=vals, tags=('empty',))
        tree.tag_configure('empty', foreground=Config.COLORS['text_muted'])
