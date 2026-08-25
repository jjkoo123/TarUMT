import random

guess = int(input("Enter your guess as 1 for a head, 2 for a tail, or 0 to exit: "))

while guess != 0:
    
    # coin flip: 1 = head, 2 = tail
    coin = random.randint(1, 2)

    if guess == coin:
        print("Your guess is correct!")
    else:
        print("Your guess is incorrect!")

    print()
    guess = int(input("Enter your guess as 1 for a head, 2 for a tail, or 0 to exit: "))

print("Thank you for playing the game!")
