import math

pi = math.pi

radius = int(input("Enter a radius of a cylinder: "))
height = int(input("Enter the height of the cylinder: "))

surface_area = 2 * pi * radius * (radius + height)
volume = pi * radius ** 2 * height

print(f"Its surface area and volume are {surface_area} and {volume} respectively")