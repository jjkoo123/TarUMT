def savings(amount, month, interest = 0.05/12):
    if month == 0:
        return 0
    return (amount + savings(amount , month - 1)) * (1 + interest)

monthly = float(input("Enter the monthly savings amount:"))
value = savings(monthly, 6)
print(f"After the sixth month, the account value is {value:.2f}")