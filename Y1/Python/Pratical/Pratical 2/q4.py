line_1 = "| X |   | O |"
line_2 = "|   | X | O |"
line_3 = "|   |   | O |"

# Count how many cells by counting '|' minus 1 (since first and last are borders)
cells = line_1.count('|') - 1

# Each cell has 3 dashes
border = '+' + ('---+' * cells)

print(border)
print(line_1)
print(border)
print(line_2)
print(border)
print(line_3)
print(border)


#################################################################################   
print(" ")
print("#############################################################")
print(" ")
###################################################################################


board = [["X", " ", "O"],
         [" ", "X", "O"],
         [" ", " ", "O"]
         ]

for row in board:
    print("+---+---+---+")
    print("| " + " | ".join(row) + " |")
print("+---+---+---+")
#####################################################################################