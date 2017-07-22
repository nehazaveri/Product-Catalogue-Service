
PRODUCT-CATALOGUE-SERVICE

Assumption:
Either Docker or MongoDB is installed and up and running in the system
This is rest micro service which provides functionality to
1. Add Product
2. Find products by type
3. Delete Product

Technologies Used
1. FrameWork : DropWizard
2. Java : Java8
3. Build Tool : Gradle
4. DataBase : MongoDB

To Start DataBase
1. If mongoDB is not running in the system , execute ./scripts/start-mongodb.sh script

To Run This Service execute following command
1. ./gradlew clean test build (from the root directory of the service)
2. ./gradlew run (This will start the application on port 8080)
3. Open the browser and enter http://localhost:8080/product-service/swagger , it will display list of all the endpoints exposed in the service