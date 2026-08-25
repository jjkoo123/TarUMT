
# Initialize variables to track scores
# We start them at -1 assuming scores cannot be negative.
highest_score = -1.0
second_highest_score = -1.0
total_score = 0.0

# 1. Ask for the number of students
num_students = int(input("Enter the number of students: "))

# 2. Loop through each student to get their score
for i in range(1, num_students + 1):
    
    # LOGIC FOR ORDINAL SUFFIXES (st, nd, rd, th)
    # We use modulo (%) math because string methods are not allowed.
    # i % 10 gets the last digit (e.g., 21 -> 1).
    # i % 100 gets the last two digits (e.g., 111 -> 11).
    
    suffix = ""
    
    # We pass 'i' (the current student number) into the match statement
    match i:
        # Handle the "teen" exceptions first (11th, 12th, 13th)
        # We use a guard (if statement) inside the case
        case _ if 11 <= (i % 100) <= 13:
            suffix = "th"
        # Handle numbers ending in 1 (1st, 21st, 31st), excluding 11
        case _ if (i % 10) == 1:
            suffix = "st"
        # Handle numbers ending in 2 (2nd, 22nd), excluding 12
        case _ if (i % 10) == 2:
            suffix = "nd"
        # Handle numbers ending in 3 (3rd, 23rd), excluding 13
        case _ if (i % 10) == 3:
            suffix = "rd"
        # All other numbers get "th"
        case _:
            suffix = "th"

    # Get the score input using the formatted suffix
    score = float(input(f"Enter the score of the {i}{suffix} student: "))
    
    # Add to total for average calculation later
    total_score += score
    
    # LOGIC TO FIND HIGHEST AND SECOND HIGHEST
    if score > highest_score:
        # Current score is the new winner. 
        # The old winner moves down to second place.
        second_highest_score = highest_score
        highest_score = score
    elif score > second_highest_score:
        # Current score isn't number 1, but it beats the current number 2.
        second_highest_score = score

# 3. Calculate Average
average_score = total_score / num_students

# 4. Output the results formatted as requested
print() # Empty line for spacing
# Using simple printing for the first line to match the image's "100%" style (no decimals if int)
# However, to be safe with floats, we usually format. 
# The code below mimics the screenshot's alignment.
print(f"The highest score       : {int(highest_score) if highest_score.is_integer() else highest_score}%")
print(f"The second highest score: {second_highest_score:.2f}%")
print(f"The average score       : {average_score:.2f}%")