from datetime import datetime, timedelta
from tkinter import ttk, messagebox, scrolledtext
import tkinter as tk
import json
import os
from typing import Optional, List, Dict, Tuple

# ============================================================================
# ANIMAL CLASSES - Using proper encapsulation and type hints
# ============================================================================

class Animal:
    """Base class for all animals with encapsulation"""
    
    def __init__(self, name: str, pet_type: str, age: int):
        self.__name = name
        self.__pet_type = pet_type
        self.__age = age

    @property
    def name(self) -> str:
        """Getter for name using property decorator"""
        return self.__name

    @property
    def pet_type(self) -> str:
        """Getter for pet_type using property decorator"""
        return self.__pet_type

    @property
    def age(self) -> int:
        """Getter for age using property decorator"""
        return self.__age

    @age.setter
    def age(self, value: int):
        """Setter for age with validation"""
        if value > 0:
            self.__age = value
        else:
            raise ValueError("Age must be positive")

    def get_info(self) -> str:
        """Returns formatted pet information"""
        return f"Name: {self.__name}, Type: {self.__pet_type}, Age: {self.__age}"
    
    def __str__(self) -> str:
        """String representation for easy printing"""
        return self.get_info()


class Dog(Animal):
    """Dog class with breed information"""
    
    def __init__(self, name: str, age: int, breed: str = "Mixed"):
        super().__init__(name, "Dog", age)
        self.__breed = breed
    
    @property
    def breed(self) -> str:
        return self.__breed
    
    def special_diet_info(self) -> str:
        return f"{self.name} the Dog requires a high-protein diet."


class Cat(Animal):
    """Cat class with breed information"""
    
    def __init__(self, name: str, age: int, breed: str = "Mixed"):
        super().__init__(name, "Cat", age)
        self.__breed = breed
    
    @property
    def breed(self) -> str:
        return self.__breed
    
    def special_diet_info(self) -> str:
        return f"{self.name} the Cat requires a balanced diet with taurine."


class Bird(Animal):
    """Bird class with species information"""
    
    def __init__(self, name: str, age: int, breed: str = "Unknown"):
        super().__init__(name, "Bird", age)
        self.__breed = breed
    
    @property
    def breed(self) -> str:
        return self.__breed
    
    def special_diet_info(self) -> str:
        return f"{self.name} the Bird requires a diet rich in seeds and fruits."


class OtherPet(Animal):
    """Generic pet class for other animals"""
    
    def __init__(self, name: str, age: int, pet_type: str, breed: str = "Unknown"):
        super().__init__(name, pet_type, age)
        self.__breed = breed
    
    @property
    def breed(self) -> str:
        return self.__breed
    
    def special_diet_info(self) -> str:
        return f"{self.name} the {self.pet_type} has specific dietary needs."


# ============================================================================
# FEEDING TRACKER - Complete implementation
# ============================================================================

class FeedingTracker:
    """Manages pets and their feeding records with file persistence"""
    
    # Class constants
    PET_TYPES = {
        'dog': Dog,
        'cat': Cat,
        'bird': Bird,
        'other': OtherPet
    }
    
    # Default feeding schedules (times per day)
    DEFAULT_SCHEDULES = {
        'dog': ['08:00', '12:00', '18:00'],  # 3 meals
        'cat': ['08:00', '18:00'],           # 2 meals
        'bird': ['08:00', '14:00', '19:00'], # 3 meals
        'other': ['08:00', '18:00']          # 2 meals
    }
    
    def __init__(self, data_file: str = "feeding_data.json"):
        self.__pets: List[Animal] = []
        self.__feeding_records: List[Dict] = []
        self.__feeding_schedules: Dict = {}  # Pet schedules
        self.__data_file = data_file
        self._ensure_data_file_exists()
        self._load_from_file()

    def _ensure_data_file_exists(self) -> None:
        """Create data file if it doesn't exist"""
        if not os.path.exists(self.__data_file):
            try:
                self._save_to_file()
                print(f"✅ Created new data file: {self.__data_file}")
            except Exception as e:
                print(f"⚠️ Error creating data file: {e}")
        else:
            print(f"✅ Data file exists: {self.__data_file}")

    def add_pet(self, pet_type: str, name: str, age: int, breed: Optional[str] = None, 
                custom_type: Optional[str] = None) -> Tuple[bool, str]:
        """Add a new pet with validation"""
        try:
            # Validate inputs
            if not name or not name.strip():
                return False, "Pet name cannot be empty"
            
            if age <= 0:
                return False, "Age must be greater than 0"
            
            # Check for duplicates
            if self._get_pet_by_name(name):
                return False, f"Pet named '{name}' already exists"
            
            # Handle "Other" pet type
            pet_type_lower = pet_type.lower()
            
            if pet_type_lower == 'other':
                if not custom_type or not custom_type.strip():
                    return False, "Please specify the pet type"
                pet = OtherPet(name, age, custom_type.strip().title(), breed or "Unknown")
            else:
                # Create pet instance for standard types
                pet_class = self.PET_TYPES.get(pet_type_lower)
                if not pet_class:
                    return False, f"Invalid pet type: {pet_type}"
                
                pet = pet_class(name, age, breed or "Mixed")
            
            self.__pets.append(pet)
            self._save_to_file()
            
            return True, f"✓ {name} added successfully!"
            
        except Exception as e:
            return False, f"Error adding pet: {str(e)}"

    def _get_pet_by_name(self, name: str) -> Optional[Animal]:
        """Find pet by name (case-insensitive)"""
        name_lower = name.lower().strip()
        return next((pet for pet in self.__pets if pet.name.lower() == name_lower), None)
    
    def remove_pet(self, name: str) -> Tuple[bool, str]:
        """Remove a pet and all associated records"""
        try:
            pet = self._get_pet_by_name(name)
            if not pet:
                return False, "Pet not found"
            
            self.__pets.remove(pet)
            # Remove feeding records efficiently
            self.__feeding_records = [r for r in self.__feeding_records 
                                     if r['name'].lower() != name.lower()]
            self._save_to_file()
            
            return True, f"✓ {name} removed successfully!"
            
        except Exception as e:
            return False, f"Error removing pet: {str(e)}"

    def add_feeding_record(self, name: str, food_type: str, quantity: str, 
                          meal_time: Optional[str] = None, notes: str = "") -> Tuple[bool, str]:
        """Add a feeding record with validation"""
        try:
            # Validate pet exists
            pet = self._get_pet_by_name(name)
            if not pet:
                return False, "Pet not found"
            
            # Validate food type
            if not food_type or not food_type.strip():
                return False, "Food type is required"
            
            # Validate quantity
            try:
                quantity_float = float(quantity)
                if quantity_float <= 0:
                    return False, "Quantity must be greater than 0"
            except (ValueError, TypeError):
                return False, "Invalid quantity. Please enter a valid number"
            
            # Set meal time
            if not meal_time:
                meal_time = datetime.now().strftime("%Y-%m-%d %H:%M")
            
            # Create record
            record = {
                'name': pet.name,
                'meal_time': meal_time,
                'food_type': food_type.strip(),
                'quantity': quantity_float,
                'notes': notes.strip(),
                'timestamp': datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            }
            
            self.__feeding_records.append(record)
            self._save_to_file()
            
            return True, "✓ Feeding logged successfully!"
            
        except Exception as e:
            return False, f"Error logging feeding: {str(e)}"

    def get_feeding_history(self, name: Optional[str] = None, 
                          date_filter: Optional[str] = None) -> List[Dict]:
        """Get filtered feeding records"""
        filtered = self.__feeding_records
        
        if name:
            name_lower = name.lower()
            filtered = [r for r in filtered if r['name'].lower() == name_lower]
        
        if date_filter:
            filtered = [r for r in filtered if r['meal_time'].startswith(date_filter)]
        
        return filtered

    @property
    def all_pets(self) -> List[Animal]:
        """Get all pets"""
        return self.__pets.copy()

    @property
    def all_records(self) -> List[Dict]:
        """Get all feeding records"""
        return self.__feeding_records.copy()

    def search_records(self, keyword: str) -> List[Dict]:
        """Search records by keyword"""
        if not keyword:
            return []
        
        keyword_lower = keyword.lower()
        return [
            record for record in self.__feeding_records
            if keyword_lower in record['name'].lower()
            or keyword_lower in record['food_type'].lower()
            or keyword_lower in record.get('notes', '').lower()
        ]

    def get_today_schedule(self) -> List[Dict]:
        """Get today's feeding records"""
        today = datetime.now().strftime("%Y-%m-%d")
        return self.get_feeding_history(date_filter=today)

    # Auto-scheduling methods
    
    def get_default_schedule(self, pet_type: str) -> List[str]:
        """Get default feeding schedule for pet type"""
        return self.DEFAULT_SCHEDULES.get(pet_type.lower(), ['08:00', '18:00'])
    
    def set_custom_schedule(self, pet_name: str, times: List[str]) -> Tuple[bool, str]:
        """Set custom feeding schedule for a pet"""
        try:
            pet = self._get_pet_by_name(pet_name)
            if not pet:
                return False, "Pet not found"
            
            # Validate time format
            for time_str in times:
                try:
                    datetime.strptime(time_str, "%H:%M")
                except ValueError:
                    return False, f"Invalid time format: {time_str}. Use HH:MM"
            
            self.__feeding_schedules[pet_name] = sorted(times)
            self._save_to_file()
            
            return True, f"✓ Schedule set for {pet_name}!"
            
        except Exception as e:
            return False, f"Error setting schedule: {str(e)}"
    
    def get_pet_schedule(self, pet_name: str) -> List[str]:
        """Get feeding schedule for a pet"""
        pet = self._get_pet_by_name(pet_name)
        if not pet:
            return []
        
        # Return custom schedule if exists, else default
        if pet_name in self.__feeding_schedules:
            return self.__feeding_schedules[pet_name]
        else:
            return self.get_default_schedule(pet.pet_type)
    
    def get_next_feeding_time(self, pet_name: str) -> Optional[str]:
        """Get next scheduled feeding time for a pet"""
        schedule = self.get_pet_schedule(pet_name)
        if not schedule:
            return None
        
        now = datetime.now()
        current_time = now.strftime("%H:%M")
        
        # Find next feeding time today
        for feeding_time in schedule:
            if feeding_time > current_time:
                return f"{now.strftime('%Y-%m-%d')} {feeding_time}"
        
        # If no more today, return first time tomorrow
        tomorrow = (now + timedelta(days=1)).strftime("%Y-%m-%d")
        return f"{tomorrow} {schedule[0]}"
    
    def get_missed_feedings(self, pet_name: str) -> List[str]:
        """Get list of missed feedings for today"""
        schedule = self.get_pet_schedule(pet_name)
        if not schedule:
            return []
        
        today = datetime.now().strftime("%Y-%m-%d")
        current_time = datetime.now().strftime("%H:%M")
        
        # Get today's feeding records
        today_records = self.get_feeding_history(pet_name, today)
        fed_times = [record['meal_time'].split()[1] if ' ' in record['meal_time'] else record['meal_time'] 
                    for record in today_records]
        
        # Find missed feedings
        missed = []
        for scheduled_time in schedule:
            if scheduled_time < current_time:
                # Check if fed within 30 minutes of scheduled time
                try:
                    is_fed = any(
                        abs((datetime.strptime(scheduled_time, "%H:%M") - 
                             datetime.strptime(fed_time, "%H:%M")).total_seconds()) <= 1800
                        for fed_time in fed_times
                    )
                except:
                    is_fed = False
                    
                if not is_fed:
                    missed.append(scheduled_time)
        
        return missed
    
    def get_feeding_streak(self, pet_name: str) -> int:
        """Get consecutive days the pet has been fed"""
        records = self.get_feeding_history(pet_name)
        if not records:
            return 0
        
        # Get unique feeding dates
        dates = sorted(set(record['meal_time'].split()[0] for record in records), reverse=True)
        
        if not dates:
            return 0
        
        # Check if fed today
        today = datetime.now().strftime("%Y-%m-%d")
        if dates[0] != today:
            return 0
        
        # Count consecutive days
        streak = 1
        for i in range(len(dates) - 1):
            date1 = datetime.strptime(dates[i], "%Y-%m-%d")
            date2 = datetime.strptime(dates[i + 1], "%Y-%m-%d")
            
            if (date1 - date2).days == 1:
                streak += 1
            else:
                break
        
        return streak
    
    def get_suggested_quantity(self, pet_name: str) -> Optional[float]:
        """Get suggested food quantity based on pet's history"""
        records = self.get_feeding_history(pet_name)
        if not records:
            return None
        
        # Calculate average from last 10 feedings
        recent_records = records[-10:]
        avg_quantity = sum(r['quantity'] for r in recent_records) / len(recent_records)
        
        return round(avg_quantity, 1)
    
    def auto_fill_next_feeding(self, pet_name: str) -> Dict:
        """Auto-fill feeding form with smart suggestions"""
        next_time = self.get_next_feeding_time(pet_name)
        suggested_quantity = self.get_suggested_quantity(pet_name)
        
        # Get most common food type
        records = self.get_feeding_history(pet_name)
        if records:
            food_types = [r['food_type'] for r in records]
            most_common_food = max(set(food_types), key=food_types.count)
        else:
            most_common_food = ""
        
        return {
            'meal_time': next_time or datetime.now().strftime("%Y-%m-%d %H:%M"),
            'food_type': most_common_food,
            'quantity': suggested_quantity or 0,
            'notes': ''
        }

    def _save_to_file(self) -> bool:
        """Save data to JSON file"""
        try:
            data = {
                'pets': [
                    {
                        'name': pet.name,
                        'type': pet.pet_type,
                        'age': pet.age,
                        'breed': getattr(pet, 'breed', None)
                    }
                    for pet in self.__pets
                ],
                'records': self.__feeding_records,
                'schedules': self.__feeding_schedules
            }
            
            with open(self.__data_file, 'w') as f:
                json.dump(data, f, indent=2)
            
            return True
            
        except Exception as e:
            print(f"⚠️ Error saving to file: {e}")
            return False

    def _load_from_file(self) -> bool:
        """Load data from JSON file"""
        try:
            if not os.path.exists(self.__data_file):
                return False
            
            with open(self.__data_file, 'r') as f:
                data = json.load(f)
            
            # Load pets
            for pet_data in data.get('pets', []):
                pet_type = pet_data['type'].lower()
                breed = pet_data.get('breed', 'Mixed')
                
                if pet_type in ['dog', 'cat', 'bird']:
                    pet_class = self.PET_TYPES.get(pet_type)
                    if pet_class:
                        pet = pet_class(pet_data['name'], pet_data['age'], breed)
                        self.__pets.append(pet)
                else:
                    pet = OtherPet(pet_data['name'], pet_data['age'], pet_data['type'], breed)
                    self.__pets.append(pet)
            
            # Load records
            self.__feeding_records = data.get('records', [])
            
            # Load schedules
            self.__feeding_schedules = data.get('schedules', {})
            
            print(f"✅ Loaded {len(self.__pets)} pets and {len(self.__feeding_records)} records")
            return True
            
        except json.JSONDecodeError:
            print("⚠️ Invalid JSON file. Starting fresh.")
            return False
        except Exception as e:
            print(f"⚠️ Error loading file: {e}")
            return False


# ============================================================================
# GUI CLASS - Complete implementation
# ============================================================================

class FeedingTrackerGUI:
    """GUI for Pet Feeding Tracker with modern design"""
    
    # Enhanced color scheme
    COLORS = {
        'primary': '#1B5E20',
        'primary_light': '#4CAF50',
        'primary_dark': '#2E7D32',
        'secondary': '#F57C00',
        'secondary_light': '#FFB74D',
        'accent': '#1976D2',
        'accent_light': '#64B5F6',
        'danger': '#D32F2F',
        'danger_light': '#E57373',
        'success': '#388E3C',
        'warning': '#FFA000',
        'bg': '#ECEFF1',
        'card': '#FFFFFF',
        'text': '#212121',
        'text_light': '#757575',
        'border': '#BDBDBD'
    }
    
    # Enhanced font constants
    FONTS = {
        'header': ('Segoe UI', 32, 'bold'),
        'subtitle': ('Segoe UI', 13),
        'section': ('Segoe UI', 15, 'bold'),
        'label': ('Segoe UI', 11, 'bold'),
        'body': ('Segoe UI', 10),
        'button': ('Segoe UI', 11, 'bold'),
        'mono': ('Courier New', 10)
    }
    
    def __init__(self, root: tk.Tk):
        self.root = root
        self._configure_window()
        self._setup_styles()
        
        self.tracker = FeedingTracker()
        self._create_widgets()

    def _configure_window(self):
        """Configure main window properties"""
        self.root.title("🐾 Pet Feeding Tracker")
        
        # Get screen dimensions
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()
        
        # Calculate window size (90% of screen)
        window_width = int(screen_width * 0.9)
        window_height = int(screen_height * 0.9)
        
        # Center the window
        position_x = int((screen_width - window_width) / 2)
        position_y = int((screen_height - window_height) / 2)
        
        self.root.geometry(f"{window_width}x{window_height}+{position_x}+{position_y}")
        
        # Make window pop out
        self.root.lift()
        self.root.attributes('-topmost', True)
        self.root.after(100, lambda: self.root.attributes('-topmost', False))
        self.root.focus_force()
        
        self.root.configure(bg=self.COLORS['bg'])
        self.root.minsize(900, 650)

    def _setup_styles(self):
        """Configure ttk styles"""
        style = ttk.Style()
        style.theme_use('clam')
        
        style.configure('TNotebook', 
                       background=self.COLORS['bg'], 
                       borderwidth=0,
                       tabmargins=[2, 5, 2, 0])
        
        style.configure('TNotebook.Tab',
                       padding=[25, 12],
                       font=self.FONTS['label'],
                       background=self.COLORS['card'],
                       borderwidth=2,
                       relief='raised')
        
        style.map('TNotebook.Tab',
                 background=[('selected', self.COLORS['primary_light'])],
                 foreground=[('selected', 'white'), ('!selected', self.COLORS['text'])],
                 expand=[('selected', [1, 1, 1, 0])])
        
        style.configure('TCombobox',
                       fieldbackground='white',
                       background=self.COLORS['primary_light'],
                       foreground=self.COLORS['text'],
                       font=self.FONTS['body'],
                       borderwidth=1,
                       relief='solid')

    def _create_widgets(self):
        """Create all GUI widgets"""
        self._create_header()
        
        main_container = tk.Frame(self.root, bg=self.COLORS['bg'])
        main_container.pack(fill='both', expand=True, padx=25, pady=(0, 25))
        
        self.notebook = ttk.Notebook(main_container)
        self.notebook.pack(fill='both', expand=True)
        
        self._create_pet_management_tab()
        self._create_feeding_log_tab()
        self._create_history_tab()
        self._create_schedule_tab()

    def _create_header(self):
        """Create application header"""
        header_container = tk.Frame(self.root, bg=self.COLORS['primary'])
        header_container.pack(fill='x')
        
        top_gradient = tk.Frame(header_container, bg=self.COLORS['primary'], height=70)
        top_gradient.pack(fill='x')
        top_gradient.pack_propagate(False)
        
        title_frame = tk.Frame(top_gradient, bg=self.COLORS['primary'])
        title_frame.pack(expand=True)
        
        tk.Label(
            title_frame,
            text="🐾 Pet Feeding Tracker 🐾",
            font=self.FONTS['header'],
            bg=self.COLORS['primary'],
            fg='white'
        ).pack()
        
        bottom_gradient = tk.Frame(header_container, bg=self.COLORS['primary_dark'], height=45)
        bottom_gradient.pack(fill='x')
        bottom_gradient.pack_propagate(False)
        
        tk.Label(
            bottom_gradient,
            text="✨ Manage your pets' meals with ease and love ✨",
            font=self.FONTS['subtitle'],
            bg=self.COLORS['primary_dark'],
            fg='white'
        ).pack(expand=True)

    def _create_card(self, parent, header_text: str, header_color: str, icon: str = "") -> Tuple[tk.Frame, tk.Frame]:
        """Create a styled card with tighter spacing and consistent width."""
        shadow = tk.Frame(parent, bg=self.COLORS['border'])
        # Anchor top-left instead of full expand to reduce blank space below
        shadow.pack(fill='x', pady=10, anchor='nw')
        card = tk.Frame(shadow, bg='white')
        card.pack(padx=2, pady=2, fill='x')
        header = tk.Frame(card, bg=header_color, height=50)
        header.pack(fill='x')
        header.pack_propagate(False)
        tk.Label(
            header,
            text=f"{icon} {header_text}" if icon else header_text,
            font=('Segoe UI', 14, 'bold'),
            bg=header_color,
            fg='white',
            anchor='w',
            padx=18
        ).pack(fill='both')
        # Reduce side padding for tighter layout
        content = tk.Frame(card, bg='white')
        content.pack(fill='x', padx=18, pady=14)
        return shadow, content

    def _create_button(self, parent, text: str, command, bg_color: str, 
                      hover_color: str, icon: str = "", fg_color: str = 'white', **kwargs) -> tk.Button:
        """Create a styled button"""
        display_text = f"{icon} {text}" if icon else text
        
        btn = tk.Button(
            parent,
            text=display_text,
            command=command,
            bg=bg_color,
            fg=fg_color,              # use provided text color
            font=self.FONTS['button'],
            relief='flat',
            cursor='hand2',
            borderwidth=0,
            **kwargs
        )
        
        btn.bind('<Enter>', lambda e: btn.config(bg=hover_color))
        btn.bind('<Leave>', lambda e: btn.config(bg=bg_color))
        
        return btn

    def _create_entry(self, parent, **kwargs):
        """Create a styled entry"""
        entry = tk.Entry(
            parent,
            font=self.FONTS['body'],
            relief='solid',
            borderwidth=2,
            bg='white',
            fg=self.COLORS['text'],
            **kwargs
        )
        
        entry.bind('<FocusIn>', lambda e: entry.config(borderwidth=2))
        entry.bind('<FocusOut>', lambda e: entry.config(borderwidth=1))
        
        return entry

    def _create_form_row(self, parent: tk.Frame, label_text: str, row: int, icon: str = "") -> tk.Label:
        """Slimmer labels aligned left."""
        display_text = f"{icon} {label_text}" if icon else label_text
        label = tk.Label(
            parent, text=display_text,
            font=('Segoe UI', 10, 'bold'),
            bg='white', fg=self.COLORS['text'],
            anchor='w'
        )
        label.grid(row=row, column=0, sticky='w', padx=(0, 12), pady=4)
        return label

    def _create_pet_management_tab(self):
        """Create pet management tab"""
        frame = tk.Frame(self.notebook, bg=self.COLORS['bg'])
        self.notebook.add(frame, text='🐕 Manage Pets')
        
        content = tk.Frame(frame, bg=self.COLORS['bg'])
        content.pack(fill='both', expand=True, padx=30, pady=25)
        
        add_shadow, add_content = self._create_card(
            content, "Add New Pet", self.COLORS['primary_light'], "➕"
        )
        add_shadow.pack(fill='x', pady=(0, 25))
        
        self._create_form_row(add_content, "Pet Type:", 0, "🐾")
        
        type_frame = tk.Frame(add_content, bg='white')
        type_frame.grid(row=0, column=1, pady=15, sticky='w')
        
        self.pet_type_var = tk.StringVar(value="Dog")
        self.pet_type_combo = ttk.Combobox(
            type_frame,
            textvariable=self.pet_type_var,
            values=["Dog", "Cat", "Bird", "Other"],
            state='readonly',
            width=33
        )
        self.pet_type_combo.pack(side='left')
        self.pet_type_combo.bind('<<ComboboxSelected>>', self._on_pet_type_changed)
        
        self.custom_type_entry = self._create_entry(type_frame, width=28)
        self.custom_type_var = tk.StringVar()
        self.custom_type_entry.config(textvariable=self.custom_type_var)
        
        self.back_to_list_btn = tk.Button(
            type_frame,
            text="⬅ Back",
            command=self._switch_back_to_combo,
            bg=self.COLORS['accent'],
            fg='white',
            font=('Segoe UI', 9, 'bold'),
            relief='flat',
            cursor='hand2',
            padx=8,
            pady=5
        )
        
        self._create_form_row(add_content, "Pet Name:", 1, "📛")
        self.pet_name_entry = self._create_entry(add_content, width=37)
        self.pet_name_entry.grid(row=1, column=1, pady=15, sticky='w')
        
        self._create_form_row(add_content, "Age (years):", 2, "🎂")
        self.pet_age_entry = self._create_entry(add_content, width=37)
        self.pet_age_entry.grid(row=2, column=1, pady=15, sticky='w')
        
        self._create_form_row(add_content, "Breed/Species:", 3, "🏷️")
        self.pet_breed_entry = self._create_entry(add_content, width=37)
        self.pet_breed_entry.grid(row=3, column=1, pady=15, sticky='w')
        
        btn_frame = tk.Frame(add_content, bg='white' )
        btn_frame.grid(row=4, column=0, columnspan=2, pady=25)
        
        # Add Pet button: white background, black text
        self._create_button(
            btn_frame, "Add Pet", self._add_pet,
            bg_color='white', hover_color='#F5F5F5', icon="✓",
            fg_color='#000000', padx=40, pady=12, width=20
        ).pack()
        
        list_shadow, list_content = self._create_card(
            content, "Your Pets", self.COLORS['accent'], "📋"
        )
        list_shadow.pack(fill='both', expand=True)
        
        listbox_frame = tk.Frame(list_content, relief='solid', borderwidth=2, bg='white')
        listbox_frame.pack(fill='both', expand=True, pady=(0, 15))
        
        scrollbar = tk.Scrollbar(listbox_frame)
        scrollbar.pack(side='right', fill='y')
        
        self.pet_listbox = tk.Listbox(
            listbox_frame,
            font=self.FONTS['body'],
            bg='white',            # force white
            fg='#000000',
            yscrollcommand=scrollbar.set,
            relief='flat',
            selectmode='single',
            activestyle='none',
            highlightthickness=0,
            selectbackground=self.COLORS['accent_light'],
            selectforeground='white'
        )
        self.pet_listbox.pack(side='left', fill='both', expand=True, padx=2, pady=2)
        scrollbar.config(command=self.pet_listbox.yview)
        
        btn_container = tk.Frame(list_content, bg='white')
        btn_container.pack(fill='x')
        
        # Refresh button: white background, black text
        self._create_button(
            btn_container, "Refresh", self._refresh_pet_list,
            bg_color='white', hover_color='#F5F5F5', icon="🔄",
            fg_color='#000000', padx=25, pady=10, width=15
        ).pack(side='left', padx=(0, 15))
        
        # Remove Selected button: white background, black text
        self._create_button(
            btn_container, "Remove Selected", self._remove_pet,
            bg_color='white', hover_color='#F5F5F5', icon="🗑️",
            fg_color='#000000', padx=25, pady=10, width=18
        ).pack(side='left')
        
        self._refresh_pet_list()

    def _on_pet_type_changed(self, event=None):
        """Handle pet type change"""
        if self.pet_type_var.get() == "Other":
            self.pet_type_combo.pack_forget()
            self.custom_type_entry.pack(side='left', padx=(0, 5))
            self.back_to_list_btn.pack(side='left')
            self.custom_type_entry.focus()
            self.custom_type_var.set("")
            self.custom_type_entry.insert(0, "Enter pet type...")
            
            def clear_placeholder(e):
                if self.custom_type_entry.get() == "Enter pet type...":
                    self.custom_type_entry.delete(0, tk.END)
            
            self.custom_type_entry.bind('<FocusIn>', clear_placeholder)
        else:
            self.custom_type_entry.pack_forget()
            self.back_to_list_btn.pack_forget()
            self.pet_type_combo.pack(side='left')

    def _switch_back_to_combo(self):
        """Switch back to combo"""
        self.custom_type_entry.pack_forget()
        self.back_to_list_btn.pack_forget()
        self.pet_type_combo.pack(side='left')
        self.pet_type_var.set("Dog")

    def _create_feeding_log_tab(self):
        """Create feeding log tab with improved alignment."""
        frame = tk.Frame(self.notebook, bg=self.COLORS['bg'])
        self.notebook.add(frame, text='🍽️ Log Feeding')

        # Container anchor northwest to avoid centering large empty area
        content = tk.Frame(frame, bg=self.COLORS['bg'])
        content.pack(fill='both', expand=True, padx=22, pady=18, anchor='nw')

        form_shadow, form_content = self._create_card(
            content, "Add Feeding Record", self.COLORS['secondary'], "📝"
        )

        # Unified grid: 2 columns (label, input group)
        form_content.grid_columnconfigure(0, weight=0)
        form_content.grid_columnconfigure(1, weight=1)

        # Row 0: Pet selection
        self._create_form_row(form_content, "Pet Name:", 0, "🐾")
        pet_input_frame = tk.Frame(form_content, bg='white')
        pet_input_frame.grid(row=0, column=1, sticky='w', pady=8)
        self.feeding_pet_var = tk.StringVar()
        self.feeding_pet_combo = ttk.Combobox(
            pet_input_frame,
            textvariable=self.feeding_pet_var,
            state='readonly',
            width=24
        )
        self.feeding_pet_combo.pack(side='left')
        # Auto-Fill button: white background, black text
        self._create_button(
            pet_input_frame, "Auto-Fill", self._auto_fill_feeding,
            bg_color='white', hover_color='#F5F5F5', icon="⚡",
            fg_color='#000000', padx=10, pady=6
        ).pack(side='left', padx=8)

        # Row 1: Meal time
        self._create_form_row(form_content, "Meal Time:", 1, "🕐")
        self.meal_time_entry = self._create_entry(form_content, width=28)
        self.meal_time_entry.insert(0, datetime.now().strftime("%Y-%m-%d %H:%M"))
        self.meal_time_entry.grid(row=1, column=1, sticky='w', pady=8)

        # Row 2: Food type
        self._create_form_row(form_content, "Food Type:", 2, "🍖")
        self.food_type_entry = self._create_entry(form_content, width=28)
        self.food_type_entry.grid(row=2, column=1, sticky='w', pady=8)

        # Row 3: Quantity
        self._create_form_row(form_content, "Quantity (g):", 3, "⚖️")
        qty_wrap = tk.Frame(form_content, bg='white')
        qty_wrap.grid(row=3, column=1, sticky='w', pady=8)
        self.quantity_entry = self._create_entry(qty_wrap, width=12)
        self.quantity_entry.pack(side='left')
        self.suggested_quantity_label = tk.Label(
            qty_wrap, text="", font=('Segoe UI', 9, 'italic'),
            bg='white', fg=self.COLORS['accent']
        )
        self.suggested_quantity_label.pack(side='left', padx=10)

        # Row 4: Notes
        self._create_form_row(form_content, "Notes:", 4, "📝")
        notes_wrap = tk.Frame(form_content, bg='white')
        notes_wrap.grid(row=4, column=1, sticky='w', pady=8)
        self.notes_entry = tk.Text(
            notes_wrap, width=40, height=4,
            font=self.FONTS['body'], relief='solid', borderwidth=1,
            bg='white', fg='#000000', insertbackground='#000000'
        )
        self.notes_entry.pack()

        # Row 5: Log Feeding button (white background, black text)
        btn_wrap = tk.Frame(form_content, bg='white')
        btn_wrap.grid(row=5, column=0, columnspan=2, pady=14)
        self._create_button(
            btn_wrap, "Log Feeding", self._log_feeding,
            bg_color='white', hover_color='#F5F5F5', icon="✓",
            fg_color='#000000', padx=40, pady=10, width=18
        ).pack()

        # Row 6: Info panel full width
        self.info_panel = tk.Frame(form_content, bg='white',
                                   relief='solid', borderwidth=1)
        self.info_panel.grid(row=6, column=0, columnspan=2, sticky='ew', pady=(12, 0))
        self.info_label = tk.Label(
            self.info_panel,
            text="💡 Select a pet to see feeding info",
            font=('Segoe UI', 10),
            bg='white',
            fg='#000000',
            justify='left', anchor='w'
        )
        self.info_label.pack(fill='x', padx=10, pady=10)

        self.feeding_pet_combo.bind('<<ComboboxSelected>>', self._update_feeding_info)
        self._update_pet_combobox()

    def _create_history_tab(self):
        """History tab alignment fix (tight top controls)."""
        frame = tk.Frame(self.notebook, bg=self.COLORS['bg'])
        self.notebook.add(frame, text='📊 History')
        content = tk.Frame(frame, bg=self.COLORS['bg'])
        content.pack(fill='both', expand=True, padx=22, pady=18, anchor='nw')

        # Search bar row
        bar = tk.Frame(content, bg='white', relief='solid', borderwidth=1)
        bar.pack(fill='x', pady=(0, 12))
        tk.Label(bar, text="🔍", bg='white', font=('Segoe UI', 14)).pack(side='left', padx=10)
        self.search_entry = self._create_entry(bar, width=40)
        self.search_entry.pack(side='left', padx=6, pady=8)
        self.search_entry.insert(0, "Search by pet name, food type, or notes...")
        
        # Search button: white background, black text
        self._create_button(
            bar, "Search", self._search_records,
            bg_color='white', hover_color='#F5F5F5', icon="🔍",
            fg_color='#000000', padx=18, pady=8
        ).pack(side='left', padx=6)
        
        # Show All button: white background, black text
        self._create_button(
            bar, "Show All", self._show_all_records,
            bg_color='white', hover_color='#F5F5F5', icon="📋",
            fg_color='#000000', padx=18, pady=8
        ).pack(side='left')

        # Records area
        rec_wrap_shadow, rec_wrap = self._create_card(
            content, "Feeding Records", self.COLORS['accent'], "📜"
        )
        # Convert to full height scroll area
        text_frame = tk.Frame(rec_wrap, bg='white')
        text_frame.pack(fill='both', expand=True)
        self.history_text = scrolledtext.ScrolledText(
            text_frame,
            font=self.FONTS['mono'],
            wrap=tk.WORD,
            relief='solid',
            borderwidth=1,
            padx=10,
            pady=10,
            height=18,
            bg='white',
            fg='#000000',
            insertbackground='#000000'
        )
        self.history_text.pack(fill='both', expand=True)
        self._show_all_records()

    def _create_schedule_tab(self):
        """Schedule tab with left anchor layout."""
        frame = tk.Frame(self.notebook, bg=self.COLORS['bg'])
        self.notebook.add(frame, text='📅 Schedule')
        content = tk.Frame(frame, bg=self.COLORS['bg'])
        content.pack(fill='both', expand=True, padx=22, pady=18, anchor='nw')

        top_bar = tk.Frame(content, bg='white', relief='solid', borderwidth=1)
        top_bar.pack(fill='x', pady=(0, 12))
        tk.Label(top_bar, text="Pet:", bg='white', font=('Segoe UI', 10, 'bold')).pack(side='left', padx=10)
        self.schedule_pet_var = tk.StringVar()
        self.schedule_pet_combo = ttk.Combobox(
            top_bar, textvariable=self.schedule_pet_var, state='readonly', width=18
        )
        self.schedule_pet_combo.pack(side='left', padx=6, pady=6)
        
        # View button: white background, black text
        self._create_button(
            top_bar, "View", self._refresh_schedule,
            bg_color='white', hover_color='#F5F5F5', icon="📋",
            fg_color='#000000', padx=14, pady=6
        ).pack(side='left', padx=4)
        
        # Custom button: white background, black text
        self._create_button(
            top_bar, "Custom", self._open_custom_schedule,
            bg_color='white', hover_color='#F5F5F5', icon="⚙️",
            fg_color='#000000', padx=14, pady=6
        ).pack(side='left', padx=4)

        sched_shadow, sched_content = self._create_card(
            content, "Feeding Schedule & Status", self.COLORS['success'], "🗓️"
        )
        text_frame = tk.Frame(sched_content, bg='white')
        text_frame.pack(fill='both', expand=True)
        self.schedule_text = scrolledtext.ScrolledText(
            text_frame,
            font=self.FONTS['mono'],
            wrap=tk.WORD,
            relief='solid',
            borderwidth=1,
            padx=10,
            pady=10,
            height=18,
            bg='white',
            fg='#000000',
            insertbackground='#000000'
        )
        self.schedule_text.pack(fill='both', expand=True)

        pet_names = [p.name for p in self.tracker.all_pets]
        self.schedule_pet_combo['values'] = pet_names
        if pet_names:
            self.schedule_pet_combo.current(0)
        self._refresh_schedule()

    # Event handlers
    
    def _add_pet(self):
        """Add pet handler"""
        try:
            pet_type = self.pet_type_var.get()
            name = self.pet_name_entry.get().strip()
            age_str = self.pet_age_entry.get().strip()
            breed = self.pet_breed_entry.get().strip()
            
            custom_type = None
            if pet_type == "Other":
                custom_type = self.custom_type_var.get().strip()
                if custom_type == "Enter pet type..." or not custom_type:
                    messagebox.showerror("Error", "Please specify the pet type")
                    return
            
            if not name:
                messagebox.showerror("Error", "Pet name is required")
                return
            
            try:
                age = int(age_str)
            except ValueError:
                messagebox.showerror("Error", "Please enter a valid age")
                return
            
            success, message = self.tracker.add_pet(pet_type, name, age, breed, custom_type)
            
            if success:
                messagebox.showinfo("Success ✓", message)
                self.pet_name_entry.delete(0, tk.END)
                self.pet_age_entry.delete(0, tk.END)
                self.pet_breed_entry.delete(0, tk.END)
                self.pet_type_var.set("Dog")
                self._on_pet_type_changed()
                self._refresh_pet_list()
                self._update_pet_combobox()
            else:
                messagebox.showerror("Error", message)
                
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {str(e)}")

    def _remove_pet(self):
        """Remove pet handler"""
        try:
            selection = self.pet_listbox.curselection()
            if not selection:
                messagebox.showwarning("Warning ⚠️", "Please select a pet to remove")
                return
            
            pet_info = self.pet_listbox.get(selection[0])
            pet_name = pet_info.split(',')[0].split(':')[1].strip()
            
            confirm = messagebox.askyesno(
                "Confirm Removal",
                f"Are you sure you want to remove {pet_name}?\n\n"
                f"This will delete all feeding records.\n"
                f"This action cannot be undone."
            )
            
            if confirm:
                success, message = self.tracker.remove_pet(pet_name)
                if success:
                    messagebox.showinfo("Success ✓", message)
                    self._refresh_pet_list()
                    self._update_pet_combobox()
                    self._show_all_records()
                    self._refresh_schedule()
                else:
                    messagebox.showerror("Error", message)
                    
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {str(e)}")

    def _log_feeding(self):
        """Log feeding handler"""
        try:
            pet_name = self.feeding_pet_var.get()
            meal_time = self.meal_time_entry.get().strip()
            food_type = self.food_type_entry.get().strip()
            quantity = self.quantity_entry.get().strip()
            notes = self.notes_entry.get("1.0", tk.END).strip()
            
            if not pet_name:
                messagebox.showerror("Error", "Please select a pet")
                return
            
            success, message = self.tracker.add_feeding_record(
                pet_name, food_type, quantity, meal_time, notes
            )
            
            if success:
                messagebox.showinfo("Success ✓", message)
                self.food_type_entry.delete(0, tk.END)
                self.quantity_entry.delete(0, tk.END)
                self.notes_entry.delete("1.0", tk.END)
                self.meal_time_entry.delete(0, tk.END)
                self.meal_time_entry.insert(0, datetime.now().strftime("%Y-%m-%d %H:%M"))
                self._show_all_records()
                self._refresh_schedule()
            else:
                messagebox.showerror("Error", message)
                
        except Exception as e:
            messagebox.showerror("Error", f"An error occurred: {str(e)}")

    def _refresh_pet_list(self):
        """Refresh pet list"""
        self.pet_listbox.delete(0, tk.END)
        pets = self.tracker.all_pets
        
        if not pets:
            self.pet_listbox.insert(tk.END, "📭 No pets added yet. Add your first pet above!")
        else:
            for pet in pets:
                self.pet_listbox.insert(tk.END, f"  {str(pet)}")

    def _refresh_schedule(self):
        """Refresh schedule display"""
        pet_name = self.schedule_pet_var.get()
        self.schedule_text.delete('1.0', tk.END)
        if not pet_name:
            self.schedule_text.insert(tk.END, "No pet selected.\n")
            return
        schedule = self.tracker.get_pet_schedule(pet_name)
        next_time = self.tracker.get_next_feeding_time(pet_name)
        missed = self.tracker.get_missed_feedings(pet_name)
        streak = self.tracker.get_feeding_streak(pet_name)
        suggested = self.tracker.get_suggested_quantity(pet_name)
        self.schedule_text.insert(tk.END, f"Pet: {pet_name}\n")
        self.schedule_text.insert(tk.END, f"Schedule: {', '.join(schedule)}\n")
        self.schedule_text.insert(tk.END, f"Next feeding: {next_time or 'None'}\n")
        self.schedule_text.insert(tk.END, f"Missed today: {', '.join(missed) if missed else 'None'}\n")
        self.schedule_text.insert(tk.END, f"Streak: {streak} day(s)\n")
        if suggested is not None:
            self.schedule_text.insert(tk.END, f"Suggested quantity (avg last 10): {suggested}g\n")
        self.schedule_text.insert(tk.END, "\nToday's records:\n")
        today = datetime.now().strftime("%Y-%m-%d")
        today_records = self.tracker.get_feeding_history(pet_name, today)
        if not today_records:
            self.schedule_text.insert(tk.END, "  None logged yet.\n")
        else:
            for r in today_records:
                self.schedule_text.insert(tk.END, f"  {r['meal_time']} | {r['food_type']} | {r['quantity']}g\n")

    def _update_feeding_info(self, event=None):
        """Update right-side info panel when pet selection changes."""
        pet_name = self.feeding_pet_var.get()
        if not pet_name:
            self.info_label.config(text="💡 Select a pet to see feeding info")
            self.suggested_quantity_label.config(text="")
            return
        next_time = self.tracker.get_next_feeding_time(pet_name)
        missed = self.tracker.get_missed_feedings(pet_name)
        streak = self.tracker.get_feeding_streak(pet_name)
        suggested = self.tracker.get_suggested_quantity(pet_name)
        schedule = self.tracker.get_pet_schedule(pet_name)
        info = [
            f"📊 Feeding Info for {pet_name}",
            f"⏰ Schedule: {', '.join(schedule)}",
            f"⏭️ Next: {next_time or 'None'}",
            f"⚠️ Missed: {', '.join(missed) if missed else 'None'}",
            f"🔥 Streak: {streak} day(s)"
        ]
        if suggested is not None:
            info.append(f"💡 Suggested: {suggested}g")
            self.suggested_quantity_label.config(text=f"(Suggested: {suggested}g)")
        else:
            self.suggested_quantity_label.config(text="")
        self.info_label.config(text="\n".join(info))

    def _auto_fill_feeding(self):
        """Auto-fill feeding form with smart suggestions."""
        pet_name = self.feeding_pet_var.get()
        if not pet_name:
            messagebox.showwarning("Warning", "Select a pet first")
            return
        data = self.tracker.auto_fill_next_feeding(pet_name)
        self.meal_time_entry.delete(0, tk.END)
        self.meal_time_entry.insert(0, data['meal_time'])
        self.food_type_entry.delete(0, tk.END)
        self.food_type_entry.insert(0, data['food_type'])
        self.quantity_entry.delete(0, tk.END)
        if data['quantity']:
            self.quantity_entry.insert(0, str(data['quantity']))
        messagebox.showinfo("Auto-Fill", "Form filled with suggestions.")

    def _show_all_records(self):
        """Display all feeding records in history tab."""
        self.history_text.delete('1.0', tk.END)
        records = self.tracker.all_records
        if not records:
            self.history_text.insert(tk.END, "📭 No feeding records yet.\n")
            return
        self.history_text.insert(tk.END, f"📊 All Records ({len(records)})\n" + "═"*70 + "\n\n")
        for i, r in enumerate(records, 1):
            self.history_text.insert(tk.END,
                f"#{i} Pet: {r['name']} | Time: {r['meal_time']} | Food: {r['food_type']} | Qty: {r['quantity']}g"
                + (f" | Notes: {r['notes']}" if r.get('notes') else "") + "\n")

    def _search_records(self):
        """Search feeding records by keyword."""
        keyword = self.search_entry.get().strip()
        if not keyword or keyword == "Search by pet name, food type, or notes...":
            messagebox.showwarning("Warning", "Enter a search keyword")
            return
        results = self.tracker.search_records(keyword)
        self.history_text.delete('1.0', tk.END)
        if not results:
            self.history_text.insert(tk.END, f"No results for '{keyword}'.\n")
            return
        self.history_text.insert(tk.END, f"🔍 Results for '{keyword}' ({len(results)})\n" + "═"*70 + "\n\n")
        for i, r in enumerate(results, 1):
            self.history_text.insert(tk.END,
                f"#{i} Pet: {r['name']} | Time: {r['meal_time']} | Food: {r['food_type']} | Qty: {r['quantity']}g"
                + (f" | Notes: {r['notes']}" if r.get('notes') else "") + "\n")

    def _open_custom_schedule(self):
        """Open dialog to set custom schedule for selected pet."""
        pet_name = self.schedule_pet_var.get()
        if not pet_name:
            messagebox.showwarning("Warning", "Select a pet first")
            return
        dialog = tk.Toplevel(self.root)
        dialog.title(f"Custom Schedule - {pet_name}")
        dialog.geometry("360x320")
        dialog.transient(self.root)
        dialog.grab_set()
        tk.Label(dialog, text=f"Set feeding times for {pet_name} (HH:MM)", font=('Segoe UI', 11, 'bold')).pack(pady=10)
        entries = []
        container = tk.Frame(dialog); container.pack(pady=5)
        for i in range(5):
            row = tk.Frame(container); row.pack(anchor='w', pady=3)
            tk.Label(row, text=f"Time {i+1}:", width=10, anchor='w').pack(side='left')
            e = tk.Entry(row, width=8); e.pack(side='left')
            entries.append(e)
        # preload existing schedule
        for t, e in zip(self.tracker.get_pet_schedule(pet_name), entries):
            e.insert(0, t)
        def save():
            times = [e.get().strip() for e in entries if e.get().strip()]
            if not times:
                messagebox.showerror("Error", "Enter at least one time")
                return
            ok, msg = self.tracker.set_custom_schedule(pet_name, times)
            (messagebox.showinfo if ok else messagebox.showerror)("Result", msg)
            if ok:
                dialog.destroy()
                self._refresh_schedule()
        tk.Button(dialog, text="Save", command=save, bg="#388E3C", fg="white").pack(pady=10)
        tk.Button(dialog, text="Cancel", command=dialog.destroy, bg="#D32F2F", fg="white").pack()

    def _update_pet_combobox(self):
        """Update pet-related comboboxes safely."""
        pet_names = [p.name for p in self.tracker.all_pets]
        # Feeding tab combo
        self.feeding_pet_combo['values'] = pet_names
        if pet_names and not self.feeding_pet_var.get():
            self.feeding_pet_combo.current(0)
        # Schedule tab combo (may not exist yet during initial build)
        if hasattr(self, 'schedule_pet_combo'):
            self.schedule_pet_combo['values'] = pet_names
            if pet_names and not self.schedule_pet_var.get():
                self.schedule_pet_combo.current(0)
        # Update info panels only if dependency widgets exist
        if hasattr(self, 'info_label'):
            self._update_feeding_info()
        if hasattr(self, 'schedule_text'):
            self._refresh_schedule()
# --- ENTRY POINT ---
def main():
    root = tk.Tk()
    FeedingTrackerGUI(root)
    root.mainloop()

if __name__ == "__main__":
    main()
# --- END FILE ---