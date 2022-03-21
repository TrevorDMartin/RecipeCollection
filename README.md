# RecipeCollection
## Simple API for creating and storing recipes

This is a side-project I started working on in the middle of the database module.  
I built and refactored it as we went through the module to help solidify the concepts I was learning.  
I also built integration tests for this application. 

I created a directory of recipes in the ExampleFiles and built a method called createDatabase that runs a DDL file and then reads all the recipe files and populates the database.  


I designed and built the PostgreSQL database using normalization design techniques.  
The server follows MVC design patterns, implements a RESTful API using Spring Boot, and uses Spring JDBC to access and update a PostgreSQL database following the DAO design pattern.  

I hope for this to be an ongoing project the gains functionality as I gain experience.  

[Java Classes](recipe-collection-api/src/main/java/com/techelevator/Recipe_Collection/)

[Integration Tests](recipe-collection-api/src/test/java/com/techelevator/Recipe_Collection/dao/)

[Database DDL](recipe-collection-api/src/test/resources/test-data.sql)
