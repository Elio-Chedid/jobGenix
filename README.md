# jobGenix
This repository contains the backend code for a job recruitment Android application. The backend is developed using Flask and MySQL, and it includes authentication and various API endpoints for user management.
It also include the android mobile application developed
## Installation

1. Clone the repository to your local machine.
   ```
   git clone https://github.com/Elio-Chedid/jobGenix.get
   ```

2. Install the required dependencies. Make sure you have [Python](https://www.python.org/downloads/) and [MySQL](https://www.mysql.com/downloads/) installed.
   ```
   pip install flask flask-mysqldb pyjwt phonenumbers
   ```

3. Create a MySQL database and update the `app.config` settings in the `API.py` file.
   ```
   app.config['SECRET_KEY'] = 'SECRET KEY'
   app.config['MYSQL_HOST'] = '127.0.0.1'
   app.config['MYSQL_USER'] = 'root'
   app.config['MYSQL_PASSWORD'] = 'PASSWORD'
   app.config['MYSQL_DB'] = 'DATABASENAME'
   ```

4. Run the Flask application.
   ```
   python API.py
   ```

## API Endpoints

### Public Endpoint

- `/public`: This endpoint is public and can be accessed without authentication.

### Protected Endpoints

The following endpoints require authentication. Include the `token` parameter in the request URL.

- `/auth`: An example protected endpoint that requires authentication. Returns "authenticated" if the token is valid.

### User Signup and Signin

- `/UsignUp` (POST): User signup endpoint for creating a new employee account. Accepts the following parameters in the request body:
  - `fname`: First name of the user.
  - `lname`: Last name of the user.
  - `email`: Email address of the user.
  - `phone`: Phone number of the user.
  - `longtitude`: User's longitude.
  - `latitude`: User's latitude.
  - `password`: User's password.
  - `username`: User's username.

- `/RsignUp` (POST): Employer signup endpoint for creating a new employer account. Accepts the following parameters in the request body:
  - `fname`: First name of the employer.
  - `lname`: Last name of the employer.
  - `compName`: Company name.
  - `email`: Email address of the employer.
  - `phone`: Phone number of the employer.
  - `longtitude`: Employer's longitude.
  - `latitude`: Employer's latitude.
  - `password`: Employer's password.
  - `username`: Employer's username.

- `/UsignIn` (POST): User signin endpoint for authenticating an employee. Accepts the following parameters in the request body:
  - `password`: User's password.
  - `username`: User's username.

- `/RsignIn` (POST): Employer signin endpoint for authenticating an employer. Accepts the following parameters in the request body:
  - `password`: Employer's password.
  - `username`: Employer's username.



.
