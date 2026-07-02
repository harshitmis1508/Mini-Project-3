# Expense Approval System

A full-stack Expense Approval System that enables employees to submit expense claims, managers to review and approve/reject them, and finance teams to track department budgets and monthly expense summaries.

## 📌 Features

### Employee
- Submit expense claims
- View claim status
- Track claim history

### Manager
- Review submitted claims
- Approve or reject requests
- Add review comments

### Finance
- Manage department budgets
- Monitor monthly expense summaries
- Track department-wise expenses

## 🛠️ Tech Stack

### Frontend
- React.js
- Vite
- JavaScript
- CSS
- Axios

### Backend
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven

## 📂 Project Structure

```
Expense-Approval-System/
│
├── Backend/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── Frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

---

## 🚀 Getting Started

### Clone Repository

```bash
git clone https://github.com/harshitmis1508/Mini-Project-3.git
```

```bash
cd Mini-Project-3
```

---

# Backend Setup

Go to backend folder

```bash
cd Backend
```

Configure your database in

```
src/main/resources/application.properties
```

Run the application

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

Backend runs on

```
http://localhost:8080
```

---

# Frontend Setup

Go to frontend folder

```bash
cd Frontend
```

Install dependencies

```bash
npm install
```

Start development server

```bash
npm run dev
```

Frontend runs on

```
http://localhost:5173
```

---

## Database

- MySQL
- Spring Data JPA
- Hibernate ORM

---

## REST APIs

### Department APIs

- Create Department
- Get Departments

### Expense Claim APIs

- Submit Claim
- Get All Claims
- Review Claim

### Budget APIs

- Create Budget
- View Budgets

### Summary APIs

- Monthly Expense Summary

---

## Screens

- Submit Expense Claim
- Claim Review Dashboard
- Budget Management
- Monthly Summary

---

## Future Enhancements

- JWT Authentication
- Role Based Authorization
- Email Notifications
- File Attachment Support
- Dashboard Analytics
- Export Reports (PDF/Excel)

---

## Author

**Harshit Mishra**

GitHub: https://github.com/harshitmis1508

---

## License

This project is developed for educational and learning purposes.
