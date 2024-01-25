# LogiStockAPI
## :book:Summary 
* [1. Description](#description)
* [2. Technologies](#technologies)
* [3. Dependencies](#dependencies)
* [4. Install](#install)
* [5. How To Use](#how-to-use)

## Description
:trophy: Registry, update or delete products in the system;

:trophy: Return a paginated list of products or find by barcode or ID;

:trophy: Control stock levels by increasing or decreasing the quantity.

## Technologies
- <img src="https://img.shields.io/static/v1?label=java&message=language&color=red&style=for-the-badge&logo=java"/>
- <img src="https://img.shields.io/static/v1?label=maven&message=build&color=red&style=for-the-badge&logo=apachemaven"/>
- <img src="https://img.shields.io/static/v1?label=postgres&message=database&color=blue&style=for-the-badge&logo=postgresql"/>
- <img src="https://img.shields.io/static/v1?label=spring&message=framework&color=green&style=for-the-badge&logo=spring"/>
- <img src="https://img.shields.io/static/v1?label=junit&message=tests&color=darkgreen&style=for-the-badge&logo=junit5"/>

## Dependencies
 - Spring Data JPA
 - Spring Web
 - [Lombok](https://projectlombok.org/)
 - [Swagger](https://swagger.io/)

## Install 
1. In the terminal, clone the project:
```shell script
git clone https://github.com/GilbertoJNJ/LogiStockAPI.git
```

2. Enter in the projet diretory:
```shell script
cd ~\logistockapi
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

Access documentation after building the project:
```
http://localhost:8080/swagger-ui.html
```
