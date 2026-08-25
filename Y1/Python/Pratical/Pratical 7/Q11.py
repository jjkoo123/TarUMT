import random

def get_malaysia_data():
    """
    Component 1: Data Management
    Returns a dictionary containing States as keys and Capitals as values.
    """
    return {
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

def check_answer(user_input, correct_capital):
    """
    Component 2: Logic / Validation
    Compares the user's input with the correct capital.
    Returns True if they match (ignoring case and extra spaces), else False.
    """
    # .strip() removes spaces, .lower() makes it case-insensitive
    return user_input.strip().lower() == correct_capital.lower()

def main():
    """
    Component 3: The Main Controller
    Orchestrates the flow of the program using the functions above.
    """
    # 1. Get the data
    data = get_malaysia_data()
    
    # 2. Randomize the order
    # Extract keys (states) into a list and shuffle them
    states_list = list(data.keys())
    random.shuffle(states_list)
    
    # 3. Initialize variables
    correct_count = 0
    total_questions = len(states_list)
    
    print("Identify the capital of following states:\n")
    
    # 4. Start the Loop
    for state in states_list:
        correct_capital = data[state]
        
        # Get input
        user_guess = input(f"Enter the capital of {state}\t: ")
        
        # Use our validation function to check the answer
        if check_answer(user_guess, correct_capital):
            print("Correct")
            correct_count += 1
        else:
            print("Wrong")
        
        print() # Empty line for spacing

    # 5. Final Report
    print(f"You answered {correct_count} questions correctly!")

# Standard Python practice to run the main function
if __name__ == "__main__":
    main()