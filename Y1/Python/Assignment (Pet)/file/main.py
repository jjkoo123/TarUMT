"""
Main Application - Ultimate Pet Care Suite
This is the main entry point that uses get_*_tab() functions to connect all modules.
"""
import tkinter as tk
from tkinter import ttk, messagebox

# Import shared utilities
from shared_utils import Config, AppBridge

# Import tab classes using get_*_tab functions
from feeding_tracker import get_feeding_tab
from vet_management import get_vet_tab
from grooming_schedule import get_grooming_tab


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

        # Tabs - Use get_*_tab() functions to get the tab classes
        notebook = ttk.Notebook(self)
        notebook.pack(fill='both', expand=True, padx=10, pady=(10, 5))
        
        # Get tab classes using the get_string functions
        FeedingTab = get_feeding_tab()
        VetTab = get_vet_tab()
        GroomingTab = get_grooming_tab()
        
        # Create and add tabs
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
