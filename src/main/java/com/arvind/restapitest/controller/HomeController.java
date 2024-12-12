package com.arvind.restapitest.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcome() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Student REST API</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        margin: 0;
                        padding: 0;
                        min-height: 100vh;
                        display: flex;
                        flex-direction: column;
                    }
                    header {
                        background: #333;
                        color: white;
                        text-align: center;
                        padding: 1rem;
                    }
                    main {
                        max-width: 800px;
                        margin: 0 auto;
                        padding: 2rem;
                        flex: 1;
                    }
                    .endpoint {
                        background: #f4f4f4;
                        padding: 1rem;
                        margin: 1rem 0;
                        border-radius: 5px;
                    }
                    .endpoint code {
                        background: #e0e0e0;
                        padding: 0.2rem 0.5rem;
                        border-radius: 3px;
                    }
                    footer {
                        background: #333;
                        color: white;
                        text-align: center;
                        padding: 1rem;
                        margin-top: auto;
                    }
                    .credit {
                        font-size: 0.9rem;
                        margin-top: 0.5rem;
                    }
                    a {
                        color: #0066cc;
                        text-decoration: none;
                    }
                    footer a {
                        color: #66b3ff;
                    }
                    .code-block {
                        background: #f8f8f8;
                        padding: 1rem;
                        border-radius: 5px;
                        margin: 1rem 0;
                        font-family: monospace;
                    }
                </style>
            </head>
            <body>
                <header>
                    <h1>REST API APPLICATION</h1>
                </header>
                
                <main>
                    <h2>Student REST API Documentation</h2>
                    <p>This API provides CRUD operations for managing student data.</p>
                    
                    <h3>Available Endpoints:</h3>
                    
                    <div class="endpoint">
                        <h4>1. Get All Students</h4>
                        <p><strong>Endpoint:</strong> <code>GET /api/students</code></p>
                        <p><strong>Description:</strong> Returns a list of all students</p>
                        <div class="code-block">
                            curl https://restapiapp.onrender.com/api/students
                        </div>
                    </div>
                    
                    <div class="endpoint">
                        <h4>2. Get Student by ID</h4>
                        <p><strong>Endpoint:</strong> <code>GET /api/students/{id}</code></p>
                        <p><strong>Description:</strong> Returns a specific student by ID</p>
                        <div class="code-block">
                            curl https://restapiapp.onrender.com/api/students/1
                        </div>
                    </div>
                    
                    <div class="endpoint">
                        <h4>3. Add New Student</h4>
                        <p><strong>Endpoint:</strong> <code>POST /api/students</code></p>
                        <p><strong>Description:</strong> Adds a new student</p>
                        <div class="code-block">
                            curl -X POST https://restapiapp.onrender.com/api/students \\
                                 -H "Content-Type: application/xml" \\
                                 -d '<student>
                                        <id>3</id>
                                        <name>John Doe</name>
                                        <location>New York</location>
                                        <phone>1234567890</phone>
                                        <course>Computer Science</course>
                                    </student>'
                        </div>
                    </div>
                    
                    <div class="endpoint">
                        <h4>4. Update Student</h4>
                        <p><strong>Endpoint:</strong> <code>PUT /api/students/{id}</code></p>
                        <p><strong>Description:</strong> Updates all fields of a student</p>
                        <div class="code-block">
                            curl -X PUT https://restapiapp.onrender.com/api/students/1 \\
                                 -H "Content-Type: application/xml" \\
                                 -d '<student>
                                        <name>Updated Name</name>
                                        <location>Updated Location</location>
                                        <phone>9876543210</phone>
                                        <course>Updated Course</course>
                                    </student>'
                        </div>
                    </div>
                    
                    <div class="endpoint">
                        <h4>5. Partial Update Student</h4>
                        <p><strong>Endpoint:</strong> <code>PATCH /api/students/{id}</code></p>
                        <p><strong>Description:</strong> Updates only specified fields</p>
                        <div class="code-block">
                            curl -X PATCH https://restapiapp.onrender.com/api/students/1 \\
                                 -H "Content-Type: application/xml" \\
                                 -d '<student>
                                        <name>New Name</name>
                                    </student>'
                        </div>
                    </div>
                    
                    <div class="endpoint">
                        <h4>6. Delete Student</h4>
                        <p><strong>Endpoint:</strong> <code>DELETE /api/students/{id}</code></p>
                        <p><strong>Description:</strong> Deletes a student by ID</p>
                        <div class="code-block">
                            curl -X DELETE https://restapiapp.onrender.com/api/students/1
                        </div>
                    </div>
                    
                    <h3>Notes:</h3>
                    <ul>
                        <li>All endpoints support both XML and JSON formats</li>
                        <li>Set appropriate Content-Type header for POST, PUT, and PATCH requests</li>
                        <li>For JSON requests, use Content-Type: application/json</li>
                        <li>For XML requests, use Content-Type: application/xml</li>
                    </ul>
                </main>
                
                <footer>
                    <div>Developed by Arvind Maurya</div>
                    <div class="credit">Built with <a href="https://cursor.sh/" target="_blank">Cursor Editor</a></div>
                    <div class="credit">© 2024 All rights reserved</div>
                </footer>
            </body>
            </html>
            """;
    }
} 