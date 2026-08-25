# Hourly rates
rate = 40
rate_1_5 = rate * 1.5
rate_2 = rate * 2

# Input
hours = int(input("Enter working hours: "))

# Calculations
regular_pay = 0.0
overtime_pay = 0.0

if hours <= 35:
    regular_pay = hours * rate

elif hours <= 60:
    regular_pay = 35 * rate
    overtime_hours = hours - 35
    overtime_pay = overtime_hours * rate_1_5

else:
    regular_pay = 35 * rate
    overtime_25 = 25 * rate_1_5
    overtime_extra = (hours - 60) * rate_2
    overtime_pay = overtime_25 + overtime_extra

total_salary = regular_pay + overtime_pay

# Output
print()
print("Total salary  : RM" + format(total_salary, ".2f"))
print("-> Regular pay : RM" + format(regular_pay, ".2f"))
print("-> Overtime pay: RM" + format(overtime_pay, ".2f"))
