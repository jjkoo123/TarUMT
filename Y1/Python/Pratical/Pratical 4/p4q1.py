score = float(input("Enter a score: "))

# Determine grade
if score >= 90:
    grade = "A"
elif score >= 80:
    grade = "B"
elif score >= 70:
    grade = "C"
elif score >= 60:
    grade = "D"
else:
    grade = "F"

# Use match-case to choose article
match grade:
    case "A" | "F":
        article = "an"
    case _:
        article = "a"

print(f"You got {article} {grade} for the test.")
