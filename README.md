# Student REST API

A Spring Boot REST API application for managing student data with support for XML and JSON formats.

## Live Demo
The application is deployed at: [https://restapiapp.onrender.com](https://restapiapp.onrender.com)

## Features
- CRUD operations for student management
- Support for both XML and JSON formats
- In-memory data storage
- Cross-Origin Resource Sharing (CORS) enabled
- Detailed API documentation on homepage

## Tech Stack
- Java 17
- Spring Boot 3.2.1
- Jakarta XML Binding (JAXB)
- Docker
- Maven

## API Endpoints

### 1. Get All Students 
GET /api/students
curl https://restapiapp.onrender.com/api/students

### 2. Get Student by ID
GET /api/students/{id}
curl https://restapiapp.onrender.com/api/students/1

### 3. Add New Student
POST /api/students
# XML Format
curl -X POST https://restapiapp.onrender.com/api/students \
     -H "Content-Type: application/xml" \
     -d '<student>
            <id>3</id>
            <name>John Doe</name>
            <location>New York</location>
            <phone>1234567890</phone>
            <course>Computer Science</course>
        </student>'

# JSON Format
curl -X POST https://restapiapp.onrender.com/api/students \
     -H "Content-Type: application/json" \
     -d '{
            "id": "3",
            "name": "John Doe",
            "location": "New York",
            "phone": "1234567890",
            "course": ["Computer Science"]
        }'

### 4. Update Student
PUT /api/students/{id}
curl -X PUT https://restapiapp.onrender.com/api/students/1 \
     -H "Content-Type: application/xml" \
     -d '<student>
            <name>Updated Name</name>
            <location>Updated Location</location>
            <phone>9876543210</phone>
            <course>Updated Course</course>
        </student>'

### 5. Partial Update Student
PATCH /api/students/{id}
curl -X PATCH https://restapiapp.onrender.com/api/students/1 \
     -H "Content-Type: application/xml" \
     -d '<student>
            <name>New Name</name>
        </student>'

### 6. Delete Student
DELETE /api/students/{id}
curl -X DELETE https://restapiapp.onrender.com/api/students/1

## Local Development

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Docker (optional)

### Running Locally

1. Clone the repository
```bash
git clone https://github.com/arvind-git-code/REST_API_APPLICATION.git
cd REST_API_APPLICATION
```

2. Build the project
```bash
./mvnw clean package
```

3. Run the application
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

### Running with Docker

1. Build the Docker image
```bash
docker build -t REST_API_APPLICATION .
```

2. Run the container
```bash
docker run -p 8080:8080 REST_API_APPLICATION
```

## Deployment

The application is deployed on Render.com using Docker containerization. The deployment process is automated through Render's continuous deployment feature.

### Environment Variables
- `PORT`: The port on which the application runs (default: 8080)

## Data Structure

### Student Model
```xml
<student>
    <id>string</id>
    <name>string</name>
    <location>string</location>
    <phone>string</phone>
    <course>array of strings</course>
</student>
```

## Error Handling
- 200 OK: Successful operation
- 404 Not Found: Student not found
- 500 Internal Server Error: Server-side error

## Notes
- All endpoints support both XML and JSON formats
- Set appropriate Content-Type header for POST, PUT, and PATCH requests
- For JSON requests, use `Content-Type: application/json`
- For XML requests, use `Content-Type: application/xml`
- Data is stored in memory and resets when the application restarts

## Contributing
Feel free to submit issues and enhancement requests.

## Author
Arvind Maurya

## Built With
- [Cursor Editor](https://cursor.sh/)

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- Spring Boot Team
- Render.com for hosting