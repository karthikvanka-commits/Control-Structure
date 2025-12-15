public static void main(String[] args) {
    System.out.println(">> System initializing...");

    while (true) {  // loop until user chooses Exit
        System.out.println("\n--- WELCOME MENU ---");
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.println("3. Exit Application");
        System.out.print("Select Option: ");

        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input!");
            scanner.nextLine();   // clear buffer
            continue;             // go back to while loop
        }

        switch (choice) {         // switch control structure
            case 1:
                if (performLogin())   // if condition
                    runDashboard();   // only if login success
                break;
            case 2:
                registerUser();
                break;
            case 3:
                System.out.println("Saving data and exiting.");
                saveDatabase();
                return;              // exit main
            default:
                System.out.println("Invalid option.");
        }
    }
}


public static boolean performLogin() {
    System.out.println("\n--- LOGIN ---");
    System.out.print("Username: ");
    String user = scanner.next();
    System.out.print("Password: ");
    String pass = scanner.next();

    if (userDatabase.containsKey(user) && userDatabase.get(user).equals(pass)) {
        System.out.println("\n>> Login Successful! Welcome, " + user + ".");
        return true;
    } else {
        System.out.println(">> Error: Invalid Username or Password.");
        return false;
    }
}



public static void runDashboard() {
    while (true) {   // keep showing dashboard until Logout
        System.out.println("\n======= STUDENT DASHBOARD =======");
        System.out.println("1. View Timetable (Full Week)");
        System.out.println("2. View Specific Day");
        System.out.println("3. EDIT Timetable");
        System.out.println("4. VIEW PRESENT CLASS (Live)");
        System.out.println("5. Logout");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1: printFullWeek();      break;
            case 2: viewDay();            break;
            case 3: editSlot();           break;
            case 4: viewCurrentClass();   break;
            case 5:
                System.out.println("Logging out...");
                return;                   // exit dashboard loop
            default:
                System.out.println("Invalid choice.");
        }
    }
}



public static void setupDefaultSystem() {
    for (int i = 0; i < 6; i++) {          // outer loop: days
        for (int j = 0; j < 9; j++) {      // inner loop: slots
            if (j == 4)                    // if condition
                timetable[i][j] = new Lecture(true);
            else
                timetable[i][j] = new Lecture(false);
        }
    }
}

public static void printFullWeek() {
    for (int i = 0; i < 6; i++) {          // loop through all days
        printDaySchedule(i);
    }
}


public static void viewCurrentClass() {
    LocalTime now = LocalTime.now();
    LocalDate todayDate = LocalDate.now();
    DayOfWeek day = todayDate.getDayOfWeek();
    int dayIndex = day.getValue() - 1;

    System.out.println("\n--- LIVE CLASS STATUS ---");
    System.out.println("Current Time: " + now.getHour() + ":" + String.format("%02d", now.getMinute()));
    System.out.println("Current Day:  " + day);

    if (dayIndex > 5 || dayIndex < 0) {     // weekend check
        System.out.println(">> NO CLASSES TODAY (Weekend).");
        return;
    }

    boolean classFound = false;

    for (int i = 0; i < 9; i++) {           // loop over slots
        boolean isAfterStart  = now.compareTo(slotStartTimes[i]) >= 0;
        boolean isBeforeEnd   = now.compareTo(slotEndTimes[i]) < 0;

        if (isAfterStart && isBeforeEnd) {  // if current time in this slot
            System.out.println("----------------------------------------");
            System.out.println("ONGOING: " + defaultTimeSlots[i]);
            System.out.println("DETAILS: " + timetable[dayIndex][i].toString());
            System.out.println("----------------------------------------");
            classFound = true;
            break;                          // stop loop once found
        }
    }

    if (!classFound) {
        System.out.println(">> You are currently on a break or outside class hours.");
    }
}


