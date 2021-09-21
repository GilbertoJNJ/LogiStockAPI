# Products-API
## :book:Summary 
* [1. Description](#description)
* [2. Technologies](#technologies)
* [3. Dependencies](#dependencies)
* [4. Install](#install)
* [5. How To Use](#how-to-use)

## Description
:trophy: Registry, update or delete products in a database;

:trophy: Lists all the products of an establishment and searches by name;

:trophy: Control stock by increasing or decreasing the quantity of products.

## Technologies
- <img src="https://img.shields.io/static/v1?label=java&message=language&color=red&style=for-the-badge&logo=java"/>
- <img src="https://img.shields.io/static/v1?label=maven&message=build&color=red&style=for-the-badge&logo=apachemaven"/>
- <img src="https://img.shields.io/static/v1?label=h2-database&message=database&color=blue&style=for-the-badge&logo=h2database"/>
- <img src="https://img.shields.io/static/v1?label=spring&message=framework&color=green&style=for-the-badge&logo=spring"/>
- <img src="https://img.shields.io/static/v1?label=postman&message=apiclient&color=orange&style=for-the-badge&logo=postman"/>
- <img src="https://img.shields.io/static/v1?label=junit&message=tests&color=darkgreen&style=for-the-badge&logo=junit5"/>
- <img src="https://img.shields.io/static/v1?label=heroku&message=deploy&color=purple&style=for-the-badge&logo=heroku"/>
- <img src="https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white"/>

## Dependencies
 - Spring Data JPA
 - Spring Web
 - Spring Boot DevTools
 - Spring Test
 - [Lombok](https://projectlombok.org/)
 - [H2 Database](https://www.h2database.com/html/main.html)
 - [MapStruct](https://mapstruct.org/)

## Install 
1. In the terminal, clone the project:
```shell script
git clone https://github.com/GilbertoJNJ/Store-API.git
```

2. Enter in the projet diretory:
```shell script
cd ~\productsapi
```

3. Execute the command:
```shell script
mvn spring-boot:run
```

4. To run the tests:
```shell script
mvn clean test
```

## How To Use 
### Postman
1. POST Products:
 ```shell script
 http://localhost:8080/api/v1/products
```
```shell script
{
    "name": "",
    "buyPrice": "",
    "sellPrice": "",
    "category":{
        "category": ""
    },
    "quantity": ,
    "maxQuantity": 
}
```

2. PUT Products (by id):
 ```shell script
 http://localhost:8080/api/v1/products/[id]
```
```shell script
{
    "id": ,
    "name": "",
    "buyPrice": "",
    "sellPrice": "",
    "category":{
        "category": ""
    },
    "quantity": ,
    "maxQuantity": 
}
```

3. DELETE Products (by id):
 ```shell script
 http://localhost:8080/api/v1/products/[id]
```

4. GET all Products:
 ```shell script
 http://localhost:8080/api/v1/products
```

5. GET Products (by name):
```shell script
http://localhost:8080/api/v1/products/[name]
```

6. PATCH quantity of Products (by id):
   
- 6.1 Increment
```shell script
http://localhost:8080/api/v1/products/[id]/increment
```
```shell script
{
    "quantity":    
}
```

- 6.2 Decrement
```shell script
http://localhost:8080/api/v1/products/[id]/decrement
```
```shell script
{
    "quantity":    
}
```
