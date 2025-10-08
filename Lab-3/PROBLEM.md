JAVA LAB WORK – Object-Oriented Programming Concepts
Topic: Polymorphism, Abstraction, and Interfaces
1. Objective
To understand and implement the key object-oriented programming concepts of abstraction,
interfaces, and polymorphism in Java.
2. Outcomes
After completing this lab, the student will be able to:
• Create abstract classes and methods in Java.
• Implement and use interfaces.
• Demonstrate compile-time and run-time polymorphism.
• Apply these concepts to real-world problem-solving.
3. Lab Tasks / Questions
Q1. Abstraction
Problem Statement:
Design an abstract class Shape with the following specifications:
• Data member: color (String)
• Abstract method: double area()
• Concrete method: String getColor()
Create two subclasses:
• Circle (with radius as data member)
• Rectangle (with length and width as data members)
Each subclass must implement the area() method.
Write a main program to:
• Accept details for each shape (color, dimensions)
• Display the area of each shape.
Hint: Use constructor overloading and method overriding.
Q2. Interfaces
Problem Statement:
Create an interface Animal with two methods:
• void eat()
• void sleep()
Implement this interface in two classes:
• Dog
• Cat
Each class should give its own definition for eat() and sleep().
Write a main method to:
• Create an array of Animal references.
• Store objects of Dog and Cat in it.
• Invoke methods polymorphically using the array.
Extension Task:
Add another interface Pet with a method void play().
Implement it in Dog and demonstrate multiple inheritance using interfaces.
Q3. Polymorphism
Problem Statement:
Create a class Calculator that demonstrates method overloading for the following operations:
• add(int a, int b)
• add(int a, int b, int c)
• add(double a, double b)
Then, create a class hierarchy to demonstrate run-time polymorphism:
• Base class: Employee with method void showRole()
• Subclasses:
o Manager (prints “Manages team”)
o Developer (prints “Writes code”)
In the main method:
• Create objects of each subclass using a Employee reference.
• Call the showRole() method for each and observe dynamic bindin