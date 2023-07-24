Recipebox Readme

Features:

User Registration: Users can sign up for an account with their details, including username, first name, last name, email, phone number, and ZIP code.
Login: Registered users can log in using their email and password.
Recipe Upload: Logged-in users can upload recipes with a title, description, ingredients, instructions, and an optional image.
Recipe View: Users can view their uploaded recipes on their profile page.
Recipe Deletion: Users can delete their recipes.
Recipe Search: Users can search for recipes based on the user ID.
Technologies Used:

Spring Boot: Framework for building the application.
Spring Data JPA: Simplifies database access with JPA.
Spring Security: Handles user authentication and authorization.
Thymeleaf: Template engine for HTML rendering.
Bootstrap: CSS framework for styling.
BCrypt: Used for password encryption.
H2 Database: In-memory database for testing and development.
ModelMapper: For mapping DTOs (Data Transfer Objects) to entities.
How to Run:

Clone the repository.
Import the project into your IDE (Eclipse, IntelliJ, etc.).
Set up the required dependencies using Maven.
Run the Application.java class as a Java Application.
The application will start, and you can access it at http://localhost:8080.
Important Notes:

The application uses H2 database by default, which is an in-memory database. Data will be lost when the application is stopped.
For production use, consider configuring a persistent database like MySQL, PostgreSQL, etc.
The password is encrypted using BCrypt before storing it in the database.
The application uses Spring Security for login and user authentication, and roles are not implemented in this simple version.
Additional Enhancements (Future Work):

Implement role-based access control for user roles (user, admin).
Allow users to update their profile information.
Add pagination for displaying recipes on the user's profile page.
Enhance validation and error handling for better user experience.
Implement the ability to categorize recipes and search by categories.
Implement a rating and review system for recipes.
Add the option for users to reset their passwords if forgotten.
Implement password strength validation during registration.
