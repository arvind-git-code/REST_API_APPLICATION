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
                </style>
            </head>
            <body>
                <header>
                    <h1>Student REST API Documentation</h1>
                </header>
                
                <main>
                    <h2>Welcome to the Student REST API</h2>
                    <p>This API provides access to student information through various endpoints.</p>
                    
                    <h3>Available Endpoints:</h3>
                    
                    <div class="endpoint">
                        <h4>Get All Students</h4>
                        <p><strong>Endpoint:</strong> <code>GET /students</code></p>
                        <p><strong>Description:</strong> Returns a list of all students</p>
                        <p><strong>Example:</strong> <code>curl https://your-app-name.onrender.com/students</code></p>
                    </div>
                    
                    <div class="endpoint">
                        <h4>Get Student by ID</h4>
                        <p><strong>Endpoint:</strong> <code>GET /students/{id}</code></p>
                        <p><strong>Description:</strong> Returns a specific student by their ID</p>
                        <p><strong>Example:</strong> <code>curl https://your-app-name.onrender.com/students/1</code></p>
                    </div>
                    
                    <h3>How to Use</h3>
                    <p>You can access this API using any HTTP client. Here are some methods:</p>
                    <ul>
                        <li>Using a web browser for GET requests</li>
                        <li>Using tools like Postman or Insomnia</li>
                        <li>Using cURL in the command line</li>
                        <li>Using programming languages with HTTP client libraries</li>
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