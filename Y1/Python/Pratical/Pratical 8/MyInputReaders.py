def read_string(prompt_msg):
    """
    Purpose:
        Reads a string from user and removes leading and trailing whitespace.

    Parameters:
        prompt_msg (str): The message displayed to the user when prompting for
                          input.

    Returns:
        str: The cleaned string entered by the user.
    """
    # Prompt the user using the provided message
    raw_input = input(prompt_msg)
    
    # Remove leading and trailing whitespace using .strip()
    cleaned_string = raw_input.strip()
    
    # Return the result
    return cleaned_string

def read_integer(prompt="Enter an integer: ", error="Error: Wrong input!\n", minimum=-(10**10), maximum=10**10):
    """
    Purpose:
        Reads an integer from user input.

    Parameters:
        prompt (str): The prompt message, defaulting to 'Enter an integer: '
        error (str): The error message, defaulting to 'Error: Wrong input!'
        minimum (int): The lower bound of user input, defaulting to -(10**10).
        maximum (int): The upper bound of user input, defaulting to 10**10.

    Returns:
        int: The integer entered by the user.
    """
    while True:
        try:
            # 1. Get input
            user_input = input(prompt)
            
            # 2. Try converting to integer
            value = int(user_input)
            
            # 3. Check if it is within the allowed range
            if minimum <= value <= maximum:
                return value
            else:
                # If out of range, print error and loop again
                # end="" is used because the error message already has a \n (newline)
                print(error, end="")
                
        except ValueError:
            # 4. If int() conversion fails (e.g., user entered "abc"), print error
            print(error, end="")