"""
Vet Management Module
This module handles the Vet Management functionality.
"""
import tkinter as tk
from tkinter import ttk, messagebox, scrolledtext
from datetime import datetime, timedelta
from typing import Dict
import json
from pathlib import Path

# Import shared utilities from main module
from shared_utils import (
    Config, DateHelper, JSONManager,
    ModernButton, ModernCard, StatCard,
    create_table, create_labeled_entry, Toast
)


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


class VetTab(ttk.Frame):
    def __init__(self, parent, bridge=None):
        super().__init__(parent)
        self.manager = VetManager()
        self.bridge = bridge
        self._build_ui()
        
        # Listen for pet list updates
        if self.bridge:
            self.bridge.register('pet_list', self._on_pet_list)
            # Request initial list in case it was already emitted
            self.bridge.emit('request_pet_list')
    
    def _on_pet_list(self, pets):
        """Update the pet selection dropdown"""
        if hasattr(self, 'pet_cb'):
            self.pet_cb['values'] = pets
            if pets and not self.pet_var.get():
                self.pet_cb.current(0)
    
    def _build_ui(self):
        container = tk.Frame(self, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Stats Row
        self.stats_frame = tk.Frame(container, bg=Config.COLORS['bg'])
        self.stats_frame.pack(fill='x', pady=(0, Config.PADDING['md']))
        
        self.stat_cards = []
        stat_configs = [
            ("Total", "📊", Config.COLORS['primary']),
            ("Scheduled", "⏰", Config.COLORS['accent']),
            ("Completed", "✅", Config.COLORS['success']),
            ("This Week", "📅", Config.COLORS['warning'])
        ]
        for i, (label, icon, color) in enumerate(stat_configs):
            card = StatCard(self.stats_frame, label, 0, icon, color)
            card.pack(side='left', fill='x', expand=True, padx=(0 if i == 0 else Config.PADDING['sm'], 0))
            self.stat_cards.append(card)
        
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
        stats = self.manager.get_stats()
        stat_values = [stats['total'], stats['scheduled'], stats['completed'], stats['this_week']]
        
        for card, value in zip(self.stat_cards, stat_values):
            card.update_data(value=value)
    
    def _setup_list(self, parent):
        container = tk.Frame(parent, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['lg'], pady=Config.PADDING['lg'])
        
        # Toolbar
        toolbar = tk.Frame(container, bg=Config.COLORS['card'], height=60)
        toolbar.pack(fill='x', pady=(0, Config.PADDING['sm']))
        toolbar.pack_propagate(False)
        
        btn_container = tk.Frame(toolbar, bg=Config.COLORS['card'])
        btn_container.pack(side='right', padx=Config.PADDING['md'], pady=Config.PADDING['sm'])
        
        ModernButton(btn_container, "Refresh", self.refresh, 'primary').pack(side='right', padx=2)
        ModernButton(btn_container, "Update", self.update_dlg, 'accent').pack(side='right', padx=2)
        ModernButton(btn_container, "Delete", self.delete, 'danger').pack(side='right', padx=2)
        
        # Table
        table_frame = tk.Frame(container, bg=Config.COLORS['card'])
        table_frame.pack(fill='both', expand=True)
        
        self.tree = create_table(table_frame, 
                                ("ID", "Date", "Pet", "Owner", "Vet", "Status", "Reason", "Cost"),
                                {'ID': 50, 'Date': 120, 'Pet': 100, 'Owner': 120, 
                                 'Vet': 100, 'Status': 80, 'Reason': 150, 'Cost': 80})
        self.refresh()
    
    def _setup_add(self, parent):
        container = tk.Frame(parent, bg=Config.COLORS['bg'])
        container.pack(fill='both', expand=True, padx=Config.PADDING['xl'], pady=Config.PADDING['xl'])
        
        card = ModernCard(container, '➕ Book New Appointment')
        card.pack(fill='both', expand=True)
        
        form = tk.Frame(card, bg=Config.COLORS['card'])
        form.pack(fill='both', padx=Config.PADDING['xl'], pady=Config.PADDING['lg'])
        
        self.inputs = {}
        # Special handling for Pet Name (Combobox)
        tk.Label(form, text="🐾 Pet Name:", font=Config.FONTS['label'], 
                 bg=Config.COLORS['card']).grid(row=2, column=0, sticky='nw', pady=Config.PADDING['sm'])
        self.pet_var = tk.StringVar()
        self.pet_cb = ttk.Combobox(form, textvariable=self.pet_var, state='readonly', 
                                  width=28, font=Config.FONTS['body'])
        self.pet_cb.grid(row=2, column=1, sticky='ew', pady=Config.PADDING['sm'], padx=(Config.PADDING['sm'], 0))
        self.inputs["Pet Name"] = self.pet_cb

        fields = [
            ("Owner Name", "👤", 30),
            ("Owner Phone", "📞", 30),
            # Pet Name is handled above
            ("Species", "🦴", 30),
            ("Date", "📅", 30),
            ("Reason", "📝", 30),
            ("Vet Name", "👨‍⚕️", 30)
        ]
        
        for i, (field, icon, width) in enumerate(fields):
            row = i if i < 2 else i + 1 # Skip row 2 as it's for Pet Name
            self.inputs[field] = create_labeled_entry(form, f"{field}:", row, icon, width)
        
        form.grid_columnconfigure(1, weight=1)
        self.inputs["Date"].insert(0, DateHelper.now_str())
        
        btn_frame = tk.Frame(form, bg=Config.COLORS['card'])
        btn_frame.grid(row=8, column=0, columnspan=2, pady=(Config.PADDING['lg'], 0))
        
        ModernButton(btn_frame, "Book Appointment", self.save, 'success').pack(side='left', padx=2, fill='x', expand=True)
        ModernButton(btn_frame, "Clear Form", self.clear_form, 'secondary').pack(side='left', padx=2, fill='x', expand=True)
    
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
            tags = ('scheduled',) if a.get('status') == 'Scheduled' else ('completed',)
            cost_display = f"${a.get('cost', 0.0):.2f}" if a.get('cost') else ""
            self.tree.insert("", "end", values=(
                a['id'], a['date'], a['pet']['name'],
                a['owner']['name'], a['vet'], a.get('status', 'Scheduled'),
                a.get('reason', ''), cost_display
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
        
        ModernButton(btn_frame, "Save & Complete", save, 'success').pack(side='left', fill='x', expand=True, padx=2)
        ModernButton(btn_frame, "Cancel", dlg.destroy, 'secondary').pack(side='left', fill='x', expand=True, padx=2)
    
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


def get_vet_tab():
    """Returns the VetTab class for use in the main application."""
    return VetTab
