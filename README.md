# ☕ Portfolio Backend

This is the **backend part** of the Portfolio-Dis project, built using **Spring Boot**, **MySQL**, and **Resend Email Service**.

---

# 🛠 Tech Stack

* ☕ Java 17
* 🚀 Spring Boot 3.3.5
* 🗄 MySQL Database
* 📧 Resend Email Service
* 🔒 Input Validation & Rate Limiting
* ☁️ Render Deployment

---

# ⚡ Features

* ✅ REST API for Contact Form
* 📬 Email Notification on new contact
* 🗄 Contact messages stored in MySQL
* ⏱ Rate Limiting — 3 emails per 10 minutes
* 🚫 Duplicate message detection
* ⚠️ Global Exception Handling
* 🌐 CORS Configuration for Frontend

---

# 📂 Project Structure

```plaintext
contact-backend/
├── Dockerfile
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/prabhat/portfolio/
        │   ├── PortfolioBackendApplication.java
        │   ├── configuration/
        │   │   └── CorsConfig.java            # CORS settings
        │   ├── constant/
        │   │   ├── EmailConstants.java         # Email constants
        │   │   └── RateLimitConstants.java     # Rate limit constants
        │   ├── controller/
        │   │   └── ContactController.java      # REST endpoints
        │   ├── dto/
        │   │   ├── RequestDto.java             # Request body
        │   │   └── ResponseDto.java            # Response body
        │   ├── entity/
        │   │   └── Contact.java               # JPA Entity
        │   ├── enums/
        │   │   └── ContactStatus.java         # NEW, READ, RESOLVED
        │   ├── exception/
        │   │   ├── ApiError.java
        │   │   ├── ContactException.java
        │   │   └── GlobalExceptionHandler.java
        │   ├── repository/
        │   │   └── ContactRepository.java
        │   ├── service/
        │   │   ├── ContactService.java
        │   │   ├── EmailService.java
        │   │   └── impl/
        │   └── util/
        │       └── RateLimiter.java
        └── resources/
            ├── application.properties
            ├── application-dev.properties
            └── application-prod.properties
```

---

# 🔗 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/contact` | Submit contact form |
| GET | `/api/contacts` | Get all contacts |
| GET | `/api/contact/{id}` | Get contact by ID |
| DELETE | `/api/contact/{id}` | Delete contact |
| PATCH | `/api/contact/{id}/status` | Update status |

---

# 💻 Project Setup

```bash
# Clone the repo
git clone https://github.com/prabhatpra/contact-backend.git

# Set environment variables
export DB_URL=jdbc:mysql://localhost:3306/portfolio_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export RESEND_API_KEY=your_resend_api_key

# Run the project
mvn spring-boot:run
```

---

# 🚀 Live Deployment

🔗 Backend URL:
https://portfolio-backend-vgeu.onrender.com

---

# 👤 Author

## Prabhat Prajapati

📧 Email: [prabhatprajapati01@gmail.com](mailto:prabhatprajapati01@gmail.com)

🔗 GitHub: https://github.com/prabhatpra

🔗 LinkedIn: https://www.linkedin.com/in/prabhat-prajapati-01p6/

---

# 🙏 Thank You

If you found this project useful, feel free to ⭐ star the repository!

Suggestions and improvements are always welcome 😊
