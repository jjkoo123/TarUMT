sales_values = float(input("Enter sales values: "))

if 1 <= sales_values <= 1000:
    commission_rate = 0.01
elif 1001 <= sales_values <= 10000:
    commission_rate = 0.05
elif 10001 <= sales_values <= 100000:
    commission_rate = 0.1
else:
    commission_rate = 0
    print("Sales value out of range for commission.")

commission = sales_values * commission_rate
total = sales_values + commission

print("Commission rate: " + format(commission_rate, ".0%"))
print("Commission     : RM" + format(commission, ".2f"))

