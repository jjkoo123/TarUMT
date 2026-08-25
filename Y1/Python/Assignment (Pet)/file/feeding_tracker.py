"""
Feeding Tracker Module
This module handles the Feeding Tracker functionality.
"""
import tkinter as tk
from tkinter import ttk, messagebox, scrolledtext
from datetime import datetime
from typing import Dict, Tuple, Optional
import json
from pathlib import Path

# Import shared utilities from main module
from shared_utils import (
    Config, Validator, DateHelper, JSONManager,
    ModernButton, ModernCard, StatCard, SearchBar,
    create_table, create_labeled_entry, ensure_tree_has_rows, Toast
)


class FeedingManager(JSONManager):
    def __init__(self):
        super().__init__("feeding_data.json")
        self.pets = []
        self.records = []
        self._load_local()
    
    def _load_local(self):
        """Map JSONManager data to internal structure"""
        if isinstance(self.data, dict):
            self.pets = self.data.get('pets', [])
            self.records = self.data.get('records', [])
        else:
            # Handle migration from old list-based data if any
            self.data = {'pets': [], 'records': []}
            self.save()

    def _update_data(self):
        """Sync internal structure back to JSONManager data"""
        self.data = {'pets': self.pets, 'records': self.records}
        self.save()
    
    
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
        self._update_data()
        return True, f"✓ {name} added successfully!"
    
    def remove_pet(self, name: str) -> Tuple[bool, str]:
        original = len(self.pets)
        self.pets = [p for p in self.pets if p['name'].lower() != name.lower()]
        if len(self.pets) < original:
            self.records = [r for r in self.records if r['pet'].lower() != name.lower()]
            self._update_data()
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
        self._update_data()
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


class FeedingTab(ttk.Frame):
    def __init__(self, parent, bridge=None):
        super().__init__(parent)
        self.manager = FeedingManager()
        self.search_term = ""
        self.bridge = bridge
        self._build_ui()
        
        # Register for pet list requests (from other tabs on startup)
        if self.bridge:
            self.bridge.register('request_pet_list', lambda _: self._emit_pet_list())
        
        self._emit_pet_list()
    
    def _build_ui(self):
        container = tk.Frame(self, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Stats Row
        stats_frame = tk.Frame(container, bg=Config.COLORS['bg'])
        stats_frame.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        self.stat_cards = []
        stat_configs = [
            ("Total Pets", "🐾", Config.COLORS['primary']),
            ("Total Feedings", "🍖", Config.COLORS['accent']),
            ("Today", "📅", Config.COLORS['success']),
            ("Most Fed", "🏆", Config.COLORS['warning'])
        ]
        
        for i, (label, icon, color) in enumerate(stat_configs):
            card = StatCard(stats_frame, label, 0, icon, color)
            card.pack(side='left', fill='x', expand=True, padx=(0 if i == 0 else Config.PADDING['sm'], 0))
            self.stat_cards.append(card)
        
        # Main Content
        content = tk.Frame(container, bg=Config.COLORS['bg'])
        content.pack(fill='both', expand=True)
        
        # Left Panel (Scrollable)
        left_container = tk.Frame(content, bg=Config.COLORS['bg'], width=360)
        left_container.pack(side='left', fill='y', padx=(0, Config.PADDING['md']))
        left_container.pack_propagate(False)
        
        canvas = tk.Canvas(left_container, bg=Config.COLORS['bg'], highlightthickness=0)
        scrollbar = ttk.Scrollbar(left_container, orient="vertical", command=canvas.yview)
        scrollable_frame = tk.Frame(canvas, bg=Config.COLORS['bg'])

        scrollable_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )

        canvas.create_window((0, 0), window=scrollable_frame, anchor="nw", width=340)
        canvas.configure(yscrollcommand=scrollbar.set)
        
        canvas.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")

        # Add Pet Card inside scrollable frame
        self._create_add_pet_card(scrollable_frame)
        
        # Log Feeding Card inside scrollable frame
        self._create_log_feeding_card(scrollable_frame)
        
        # Right Panel
        right = tk.Frame(content, bg=Config.COLORS['bg'])
        right.pack(side='right', fill='both', expand=True)
        
        # History Card
        self._create_history_card(right)
        
        # Enable mousewheel scrolling only when mouse is over the canvas
        def _on_mousewheel(event):
            canvas.yview_scroll(int(-1*(event.delta/120)), "units")
        
        def _bind_mousewheel(event):
            canvas.bind_all("<MouseWheel>", _on_mousewheel)
        
        def _unbind_mousewheel(event):
            canvas.unbind_all("<MouseWheel>")
        
        canvas.bind("<Enter>", _bind_mousewheel)
        canvas.bind("<Leave>", _unbind_mousewheel)

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
        
        # Bind event to auto-focus breed field when "Other" is selected
        def on_type_change(event):
            if self.type_var.get() == "Other":
                self.breed_e.focus_set()
        self.type_cb.bind("<<ComboboxSelected>>", on_type_change)
        
        self.age_e = create_labeled_entry(form, "Age (years):", 2, '🎂', 22)
        self.breed_e = create_labeled_entry(form, "Breed:", 3, '🧬', 22)
        
        form.grid_columnconfigure(1, weight=1)
        
        btn_frame = tk.Frame(form, bg=Config.COLORS['card'])
        btn_frame.grid(row=4, column=0, columnspan=2, pady=(Config.PADDING['md'], 0))
        
        ModernButton(btn_frame, "Add Pet", self.add_pet, 'primary').pack(side='left', padx=2, fill='x', expand=True)
        ModernButton(btn_frame, "Clear", self.clear_add_form, 'secondary').pack(side='left', padx=2, fill='x', expand=True)
    
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
        
        self.food_e = create_labeled_entry(form, "Food Type:", 1, '🍖', 22,)
        self.qty_e = create_labeled_entry(form, "Quantity (g):", 2, '⚖️', 22)
        self.notes_e = create_labeled_entry(form, "Notes:", 3, '📝', 22, 'text')
        
        form.grid_columnconfigure(1, weight=1)
        
        ModernButton(form, "Log Feeding", self.log_feeding, 'success'
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
        age = self.age_e.get().strip()
        
        if not name:
            messagebox.showerror("Error", "Pet name is required")
            return
        
        if not age:
            messagebox.showerror("Error", "Age is required")
            return
        
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
        notes = self.notes_e.get("1.0", "end-1c").strip()
        
        if not all([pet, food, qty]):
            messagebox.showwarning("Required Fields", "Please fill all required fields")
            return
        
        success, msg = self.manager.add_record(pet, food, qty, notes)
        
        if success:
            Toast.show(self, msg, type='success')
            self.food_e.delete(0, 'end')
            self.qty_e.delete(0, 'end')
            self.notes_e.delete("1.0", "end")
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
        current_pet = self.pet_var.get()
        if pets:
            if not current_pet or current_pet not in pets:
                self.pet_cb.current(0)
        else:
            self.pet_var.set('')
        
        # Update table
        self.refresh_table()
        
        # Update stats
        stats = self.manager.get_stats()
        stat_values = [
            stats['total_pets'],
            stats['total_feedings'],
            stats['feedings_today'],
            str(stats['most_fed'])[:10]
        ]
        
        for card, value in zip(self.stat_cards, stat_values):
            card.update_data(value=value)


def get_feeding_tab():
    """Returns the FeedingTab class for use in the main application."""
    return FeedingTab
