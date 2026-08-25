while True:
    user_input = input("Enter a score or 'q'/ 'Q' to exit: ")
    
    if user_input.lower() == 'q':
        print("Exiting the program.")
        break
    
    try:
        score = float(user_input)

        if score < 0 or score > 100:
            print("Only 0 ≤ number ≤ 100 inclusive are allowed!")
            continue
    
        if score >= 90:
            print("You got an A for the test!")
        elif score >= 80:
            print("You got a B for the test!")
        elif score >= 70:
            print("You got a C for the test!")
        elif score >= 60:
            print("You got a D for the test!")
        else:
            print("You got an F for the test!")
    
    except ValueError:
        print("Invalid input. Please enter a valid score or 'q' to exit.")