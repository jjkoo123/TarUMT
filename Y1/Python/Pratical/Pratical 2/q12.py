
def solve_linear_equations(a, b, e, c, d, f):
    determinant = a * d - b * c
    if determinant == 0:
        return None  # No unique solution exists
    x = (e * d - b * f) / determinant
    y = (a * f - e * c) / determinant
    return x, y

num1 = float(input("Enter a: "))
num2 = float(input("Enter b: "))
num3 = float(input("Enter e: "))
num4 = float(input("Enter c: "))
num5 = float(input("Enter d: "))
num6 = float(input("Enter f: "))

solution = solve_linear_equations(num1, num2, num3, num4, num5, num6)
if solution is None:
    print("No unique solution exists.")
else:
    x, y = solution
    print(f"The solution is x = {x:.2f}, y = {y:.2f}")
# d --- IGNORE ---

