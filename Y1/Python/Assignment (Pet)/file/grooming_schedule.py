"""
Grooming Schedule Module
"""
import tkinter as tk
from tkinter import ttk, messagebox
from datetime import datetime, timedelta
from typing import Dict, List, Tuple
import uuid

from shared_utils import (
    Config, DateHelper, JSONManager, ModernButton, ModernCard, StatCard,
    create_table, create_labeled_entry, create_labeled_combobox, ensure_tree_has_rows, Toast
)


class GroomingManager(JSONManager):
    def __init__(self):
        super().__init__("grooming_data.json")
    
    def _safe_existing_slots(self):
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
            for s, e, item in self._safe_existing_slots():
                if start < e and end > s:
                    return False, f"Time conflict with: {item.get('task', 'existing task')}"
            self.data.append({
                'id': str(uuid.uuid4()), 'start': start.strftime(DateHelper.DISPLAY_FMT),
                'end': end.strftime(DateHelper.DISPLAY_FMT), 'start_iso': start.isoformat(),
                'end_iso': end.isoformat(), 'duration': duration, 'task': task, 'pet': pet,
                'created_at': datetime.now().isoformat()
            })
            self.data.sort(key=lambda x: x['start_iso'])
            self.save()
            return True, "Task scheduled!"
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
        total, upcoming, today_tasks = len(self.data), 0, 0
        now, today = datetime.now(), datetime.now().date()
        for t in self.data:
            start_iso = t.get('start_iso') or t.get('start')
            if not start_iso: continue
            try:
                dt = datetime.fromisoformat(start_iso)
                if dt > now: upcoming += 1
                if dt.date() == today: today_tasks += 1
            except: continue
        return {'total': total, 'upcoming': upcoming, 'today': today_tasks}


class GroomingTab(ttk.Frame):
    def __init__(self, parent, bridge=None):
        super().__init__(parent)
        self.manager = GroomingManager()
        self.bridge = bridge
        self._build_ui()
        if self.bridge: 
            self.bridge.register('pet_list', self._on_pet_list)
            # Request initial list
            self.bridge.emit('request_pet_list')
    
    def _build_ui(self):
        # Main container
        main_container = tk.Frame(self, bg=Config.COLORS['bg'])
        main_container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Create canvas and scrollbar for scrollable content
        canvas = tk.Canvas(main_container, bg=Config.COLORS['bg'], highlightthickness=0)
        scrollbar = ttk.Scrollbar(main_container, orient="vertical", command=canvas.yview)
        container = tk.Frame(canvas, bg=Config.COLORS['bg'])
        
        # Update scroll region when content changes
        container.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )
        
        # Create window for container - will be resized below
        canvas_window = canvas.create_window((0, 0), window=container, anchor="nw")
        canvas.configure(yscrollcommand=scrollbar.set)
        
        # Bind canvas resize to update window width
        def on_canvas_resize(event):
            canvas.itemconfig(canvas_window, width=event.width)
        canvas.bind("<Configure>", on_canvas_resize)
        
        canvas.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")
        
        # Enable mousewheel scrolling only when mouse is over canvas
        def _on_mousewheel(event):
            canvas.yview_scroll(int(-1*(event.delta/120)), "units")
        
        def _bind_mousewheel(event):
            canvas.bind_all("<MouseWheel>", _on_mousewheel)
        
        def _unbind_mousewheel(event):
            canvas.unbind_all("<MouseWheel>")
        
        canvas.bind("<Enter>", _bind_mousewheel)
        canvas.bind("<Leave>", _unbind_mousewheel)
        
        # Stats frame
        stats_frame = tk.Frame(container, bg=Config.COLORS['bg'])
        stats_frame.pack(fill='x', pady=(0, Config.PADDING['md']))
        self.stat_cards: List[StatCard] = []
        stat_configs = [
            ("Total Tasks", "📊", Config.COLORS['primary']),
            ("Upcoming", "⏰", Config.COLORS['accent']),
            ("Today", "📅", Config.COLORS['success'])
        ]
        for i, (label, icon, color) in enumerate(stat_configs):
            card = StatCard(stats_frame, label, 0, icon, color)
            card.pack(side='left', fill='x', expand=True, padx=(0 if i == 0 else Config.PADDING['sm'], 0))
            self.stat_cards.append(card)
        
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
        
        list_card = ModernCard(container, '📅 Scheduled Grooming Tasks')
        list_card.pack(fill='both', expand=True)
        toolbar = tk.Frame(list_card, bg=Config.COLORS['card'])
        toolbar.pack(fill='x', padx=Config.PADDING['md'], pady=Config.PADDING['sm'])
        ModernButton(toolbar, "Delete Selected", self.delete_task, 'danger', '🗑️').pack(side='right', padx=2)
        ModernButton(toolbar, "Refresh", self.refresh, 'secondary', '🔄').pack(side='right', padx=2)
        
        table_frame = tk.Frame(list_card, bg=Config.COLORS['card'])
        table_frame.pack(fill='both', expand=True, padx=Config.PADDING['md'], pady=(0, Config.PADDING['md']))
        self.tree = create_table(table_frame, ("Start", "End", "Duration", "Pet", "Task", "ID"),
                                {'Start': 140, 'End': 140, 'Duration': 90, 'Pet': 120, 'Task': 200, 'ID': 0})
        self.tree.column("ID", width=0, stretch=tk.NO)
        self.refresh()
    
    def add_task(self):
        task, pet = self.task_e.get().strip(), self.pet_var.get().strip()
        if not task:
            messagebox.showwarning("Required Field", "Please enter a task description"); return
        try:
            dur = int(self.dur_e.get())
            if dur <= 0: messagebox.showerror("Invalid Duration", "Duration must be positive"); return
        except ValueError:
            messagebox.showerror("Invalid Input", "Please enter valid duration"); return
        success, msg = self.manager.add_task(self.start_e.get(), dur, task, pet)
        if success: Toast.show(self, msg, type='success'); self.clear_form(); self.refresh()
        else: messagebox.showerror("Scheduling Error", msg)
    
    def delete_task(self):
        sel = self.tree.selection()
        if not sel: messagebox.showwarning("No Selection", "Please select a task"); return
        task_id = self.tree.item(sel[0])['values'][5]
        if messagebox.askyesno("Confirm Delete", "Delete this grooming task?"):
            if self.manager.delete_task(task_id): Toast.show(self, "Task deleted", type='success'); self.refresh()
    
    def _on_pet_list(self, pets):
        if not pets: 
            self.pet_cb['values'] = []
            self.pet_var.set('')
            return
        self.pet_cb['values'] = pets
        current = self.pet_var.get()
        if not current or current not in pets:
            self.pet_var.set(pets[0])

    def clear_form(self):
        if self.pet_cb['values']: self.pet_var.set(self.pet_cb['values'][0])
        else: self.pet_var.set('')
        self.task_e.delete(0, 'end'); self.start_e.delete(0, 'end')
        self.start_e.insert(0, DateHelper.now_str()); self.dur_e.delete(0, 'end'); self.dur_e.insert(0, "60")
    
    def refresh(self):
        for item in self.tree.get_children(): self.tree.delete(item)
        now = datetime.now()
        for t in self.manager.data:
            start_iso = t.get('start_iso') or t.get('start')
            if not start_iso: continue
            try: start_dt = datetime.fromisoformat(start_iso)
            except: continue
            if start_dt.date() < now.date(): tags = ('past',)
            elif start_dt.date() == now.date(): tags = ('today',)
            else: tags = ('future',)
            self.tree.insert("", "end", values=(t.get('start', ''), t.get('end', ''), 
                f"{t.get('duration', 0)} min", t.get('pet', 'N/A'), t.get('task', ''), t.get('id', '')), tags=tags)
        self.tree.tag_configure('past', foreground=Config.COLORS['text_muted'])
        self.tree.tag_configure('today', foreground=Config.COLORS['accent'])
        self.tree.tag_configure('future', foreground=Config.COLORS['success'])
        stats = self.manager.get_stats()
        stat_values = [stats['total'], stats['upcoming'], stats['today']]
        for card, value in zip(self.stat_cards, stat_values):
            card.update_data(value=value)
        ensure_tree_has_rows(self.tree)


def get_grooming_tab():
    """Returns the GroomingTab class for use in the main application."""
    return GroomingTab
