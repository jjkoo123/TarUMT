def sum_digits(n):
    
    if n < 1 or n > 1000:
        print("Please enter a number between 1 and 1000.")
        return None
    
    if n == 0:          # stop when no digits left
        return 0
    return (n % 10) + sum_digits(n // 10)   # last digit + sum of remaining digits

num = int(input("Enter a number between 1 and 1000: "))
print("The sum of the digits is", sum_digits(num))
