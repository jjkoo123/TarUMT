positive_count = 0
negative_count = 0
total_count = 0
total_sum = 0

while True:
    number = int(input("Enter a number: "))
    
    if number == 0:
        break
    
    total_sum += number
    total_count += 1
    
    if number > 0:
        positive_count += 1
    elif number < 0:
        negative_count += 1
        
    if total_count > 0:
        average = total_sum / total_count
    else:
        average = 0
        
print()
print(f"No. of positive numbers: {positive_count}")
print(f"No. of negative numbers: {negative_count}")
print(f"Sum of all numbers     : {total_sum}")
print(f"Average of all numbers : {average:.2f}")
        