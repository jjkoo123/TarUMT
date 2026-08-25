# 1. Get the user input
monthly_saving = float(input("Enter the monthly saving amount: "))

# 2. Define the fixed variables based on the problem description
annual_rate = 0.05              # 5% annual rate
monthly_rate = annual_rate / 12 # 0.00417...
current_value = 0.0             # Account starts at 0

# 3. Loop for 6 months
# The sample run asks for the result "After the sixth month"
for i in range(6):
    # The Formula:
    # (New Deposit + Existing Money) * (1 + Interest Rate)
    current_value = (monthly_saving + current_value) * (1 + monthly_rate)

# 4. Print the result formatted to 2 decimal places
print(f"After the sixth month, the account value is {current_value:.2f}")