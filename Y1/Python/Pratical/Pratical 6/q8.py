import random

# 1. Define the data using a Dictionary (Collection)
# Key = State Name, Value = Capital City
# Note: Based on the screenshot, Labuan's capital is expected to be "Victoria".
malaysia_data = {
    "Johor": "Johor Bahru",
    "Kedah": "Alor Setar",
    "Kelantan": "Kota Bharu",
    "Malacca": "Malacca City",
    "Negeri Sembilan": "Seremban",
    "Pahang": "Kuantan",
    "Penang": "George Town",
    "Perak": "Ipoh",
    "Perlis": "Kangar",
    "Sabah": "Kota Kinabalu",
    "Sarawak": "Kuching",
    "Selangor": "Shah Alam",
    "Terengganu": "Kuala Terengganu",
    "Kuala Lumpur": "Kuala Lumpur",
    "Labuan": "Victoria",
    "Putrajaya": "Putrajaya"
}

# 2. Randomize the order
# We cannot shuffle a dictionary directly, so we create a list of the keys (States)
# and shuffle that list.
states_list = list(malaysia_data.keys())
random.shuffle(states_list)

# Initialize score counter
correct_count = 0

print("Identify the capital of following states:\n")

# 3. Loop through the randomized list
for state in states_list:
    # Get the correct answer from the dictionary
    correct_capital = malaysia_data[state]
    
    # Prompt user for input
    # \t adds a tab space to align the colons slightly better
    user_guess = input(f"Enter the capital of {state}\t: ")
    
    # 4. Check answer (Case Insensitive)
    # .strip() removes accidental spaces at the start/end
    # .lower() converts everything to small letters for fair comparison
    if user_guess.strip().lower() == correct_capital.lower():
        print("Correct")
        correct_count += 1
    else:
        print("Wrong")
    
    print() # Print an empty line for spacing between questions

# 5. Final Report
print(f"You answered {correct_count} questions correctly!")