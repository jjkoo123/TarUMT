def show(n):
    if n > 4:      # stop at 4
        return
    print(f"{n}   {n**2}   {n**3}")
    show(n + 1)    # recursive call

print("x   x^2   x^3")
show(1)
