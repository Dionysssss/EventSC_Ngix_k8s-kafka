# Event SC Project

This project is a full-stack application for managing events, allowing users to create events, view event details, leave comments, validate events with thumbs up/down, and get directions to event locations. It consists of a backend server (Spring Boot with MySQL database) and an Android frontend.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Setup Instructions](#setup-instructions)
    - [Database Setup](#database-setup)
    - [Backend Setup](#backend-setup)
    - [Frontend Setup](#frontend-setup)
3. [Running the Application](#running-the-application)
4. [Project Structure](#project-structure)
5. [Features and Usage](#features-and-usage)
6. [Troubleshooting](#troubleshooting)
7. [Notes](#notes)
8. [Maven Installation](#maven-installation)
9. [Improved Capabilities since Project 2.4](#improved-capabilities-since-project-2-4)

---

### Prerequisites

Before starting, ensure you have the following installed on your system:

1. **Java Development Kit (JDK)** - Version 11 or higher
2. **IntelliJ IDEA** - For backend development
3. **Android Studio** - With a configured emulator (recommended: Medium Phone API 35)
4. **MySQL Server** - For the database
5. **MySQL Workbench** - For managing the database (optional but recommended)
6. **Maven** - For building and running the backend (see [Maven Installation](#maven-installation) for setup instructions)

---

### Setup Instructions

#### Database Setup
1. Open **MySQL Workbench**.
2. Run the SQL script `event_sc.sql` in MySQL Workbench to set up the necessary database schema and tables.

#### Backend Setup
1. Open the backend project in **IntelliJ IDEA**.
2. Configure the database connection:
    - Go to `src/main/resources/application.yml`.
    - Update the `application.yml` file to match your MySQL configuration:
      ```yaml
      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/event_sc  # Ensure the database name matches the name in `event_sc.sql`
          username: your_mysql_username
          password: your_mysql_password  # Replace with your MySQL password
      ```
3. Run the backend server:
    - In the terminal within IntelliJ, navigate to the project directory.
    - Run the following Maven command to start the server:
      ```bash
      ./mvnw spring-boot:run
      ```
    - Alternatively, you can run the `EventScApplication` class directly from IntelliJ.
4. The backend server should now be running on `http://localhost:8080`.

#### Frontend Setup
1. Open the frontend project in **Android Studio**.
2. Configure the Emulator:
    - Use an emulator configured with **Medium Phone API 35** or any API level compatible with your project dependencies.
    - Ensure that the emulator can access `http://localhost:8080` by mapping localhost to `http://10.0.2.2:8080` in your network settings if required.
3. **Sync the Project with Gradle**:
    - In Android Studio, click **File > Sync Project with Gradle Files** to make sure all dependencies are up-to-date.
4. **Fix SDK Location Error (if applicable)**:
    - If the system shows an error about the SDK location being incorrect, go to **File > Project Structure** in Android Studio, and it should automatically detect and update the SDK location for you.
5. Run the frontend:
    - Click the **Run** button in Android Studio or press `Shift+F10` to deploy the application to the emulator.

---

### Running the Application

1. **Start the Backend**: Follow the [Backend Setup](#backend-setup) instructions to ensure the backend server is running.
2. **Launch the Android Application**: Follow the [Frontend Setup](#frontend-setup) instructions to run the Android app on the emulator.
3. **Verify**: The application should now be fully functional, connected to the backend, and ready for use.

---

### Project Structure

#### Backend (Spring Boot with MySQL)
- **Controller Layer**: Manages HTTP requests and routes them to the appropriate service.
- **Service Layer**: Contains the business logic for event management, user validation, and comments.
- **Repository Layer**: Interfaces with the MySQL database using Spring Data JPA.

#### Frontend (Android)
- **Activities**: Each screen in the app has a corresponding Activity, such as `EventDetailsActivity`.
- **Adapters**: Used for displaying lists (e.g., comments) in UI components like `ListView`.
- **Models**: Data models that represent the structure of data being passed between the frontend and backend.
- **Networking**: Retrofit library is used to handle API calls to the backend server.

---

### Features and Usage

#### Event Management
- **View Events**: Users can see a list of all available events.
- **Event Details**: Tap on an event to view details such as name, location, time, description, and creator.
- **Commenting**: Users can leave comments on events and view comments from others.

#### Event Validation
- **Thumbs Up / Thumbs Down**: Users can validate an event by giving it a thumbs up (confirming its validity) or thumbs down (reporting it as false).
- **Count Display**: The app shows the count of thumbs up and thumbs down for each event.

#### Directions to Events
- **Get Directions Button**: Users can view a route from a predefined starting location to the event's location on Google Maps.
- **Starting Location**: The starting location for directions is preset to:
    - **Latitude**: 34.019437
    - **Longitude**: -118.289525

#### Editing Events
- **Edit Event**: Users who created an event can edit its details.
- **Save Changes**: After editing, the event updates in both the frontend and backend.

---

### Troubleshooting

- **Backend Connection Issues**: Ensure the backend server is running on `http://localhost:8080`. If using an emulator, make sure to access it via `http://10.0.2.2:8080`.
- **Database Errors**: Verify that MySQL is running and that the `event_sc.sql` script was successfully executed.
- **Emulator Network Access**: For emulator network issues, check that it can route requests to `http://10.0.2.2:8080`.
- **Gradle Sync Issues**: If there are Gradle sync errors, try resyncing the project in Android Studio.
- **Permission Errors**: Ensure that Android permissions (e.g., internet) are properly configured in `AndroidManifest.xml`.

---

### Notes

- **Database Configuration**: Ensure the MySQL server is running whenever the backend server is started.
- **Fixed Starting Location**: For directions, the starting location is preset to:
    - Latitude: 34.019437
    - Longitude: -118.289525

---

### Maven Installation

Maven is required to build and run the backend. Follow the instructions below to install Maven on your operating system.

#### Installing Maven on Windows

1. **Download Maven**:
    - Go to [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).
    - Download the binary zip archive (e.g., `apache-maven-x.x.x-bin.zip`).

2. **Extract the Zip File**:
    - Extract the downloaded zip file to a location of your choice, such as `C:\Program Files\Apache\maven` or `C:\maven`.

3. **Set Up Environment Variables**:
    - Open the **Start Menu**, search for "Environment Variables," and select **Edit the system environment variables**.
    - In the System Properties window, click **Environment Variables**.
    - Under **System Variables**, click **New** and add:
        - **Variable name**: `MAVEN_HOME`
        - **Variable value**: Path to the extracted Maven folder (e.g., `C:\maven\apache-maven-x.x.x`).
    - Add Maven’s `bin` directory to the `Path` variable:
        - In **System Variables**, find and select `Path`, then click **Edit**.
        - Click **New** and add the path to Maven’s `bin` directory (e.g., `C:\maven\apache-maven-x.x.x\bin`).
    - Click **OK** to close all dialogs.

4. **Verify Maven Installation**:
    - Open **Command Prompt** and type:
      ```bash
      mvn -version
      ```
    - You should see the Maven version and Java information displayed.

#### Installing Maven on macOS

1. **Using Homebrew (Recommended)**:
    - Open **Terminal** and run:
      ```bash
      brew install maven
      ```

2. **Manual Installation**:
    - Download Maven from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).
    - Extract the downloaded file and move it to `/usr/local/apache-maven`.
    - Open the Terminal and edit the `~/.zshrc` or `~/.bash_profile` file to add Maven to your PATH:
      ```bash
      export PATH=/usr/local/apache-maven/bin:$PATH
      ```
    - Save the file and reload it by running:
      ```bash
      source ~/.zshrc   # or source ~/.bash_profile
      ```

3. **Verify Maven Installation**:
    - In Terminal, run:
      ```bash
      mvn -version
      ```
    - You should see Maven's version information.

### Improved Capabilities since Project 2.4

1. **Update UI of Login Page**
   - Redesigned the login page to align with USC's official design standards, enhancing user trust and professionalism.

2. **Display Latitude/Longitude and Address on Create Event Page**
   - Integrated a feature to display both latitude/longitude and the human-readable address on the event creation page for better location verification.

3. **Display Address on Event Details Page**
   - Updated the event details page to replace latitude/longitude with a readable address for improved clarity and usability.

4. **Dynamic Address Update on Modify Event Page**
   - Implemented real-time address updates during event modifications to provide users with confidence in the new event location.

5. **Secure the Password Store in the Database**
   - Secured password storage by implementing hashing using `PasswordEncoder`, ensuring sensitive user data is protected against unauthorized access.

---

This project should now be fully set up and ready to use. Enjoy managing your events with Event SC!