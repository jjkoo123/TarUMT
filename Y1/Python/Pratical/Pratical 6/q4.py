import random

history = []

correct_count = 0
incorrect_count = 0

while True:
    try:
        print("Enter your guess as 1 for a head, 2 for a tail, or 0 to exit: ", end="")
        user_input_str = input()
        
        user_guess = int(user_input_str)
        
        if user_guess == 0:
            print("Exiting the program.")
            break
    
        if user_guess not in [1, 2]:
            print("Invalid input. Please enter 1, 2, or 0 to exit.")
            continue
        
        correct_anser = random.randint(1, 2)
        
        if user_guess == correct_anser:
            print("You guessed it right!")
            correct_count += 1
            history.append("Correct")
        else:
            print("Sorry, your guess is wrong.")
            incorrect_count += 1
            history.append("Incorrect")
            
        history.append(f"Your guess: {'Head' if user_guess == 1 else 'Tail'}, Correct answer: {'Head' if correct_anser == 1 else 'Tail'}")
        
    except ValueError:
        print("Invalid input. Please enter a valid number.")
        
print("\nRESULTS")
print("-------")
print(f"You have made {correct_count} correct guesses and {incorrect_count} incorrect guess.\n")

