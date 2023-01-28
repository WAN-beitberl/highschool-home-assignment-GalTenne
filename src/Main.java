import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        String SchoolInfoFIleSVC = "C:\\Users\\galte\\IdeaProjects\\SimaSystem\\highschool.csv";
        String FriendshipInfoFileSVC = "C:\\Users\\galte\\IdeaProjects\\SimaSystem\\highschool_friendships.csv";
        DBManagement.ReadStudentsData(SchoolInfoFIleSVC);
       DBManagement.ReadFriendshipsData(FriendshipInfoFileSVC);

        System.out.println("Hello Sima! And welcome to SimaSystem-");
        System.out.println("the automatic system that will allow you to create files and reports quickly,");
        System.out.println("so you cold have more time to shout on poor innocent children!");

        Scanner myObj = new Scanner(System.in);
        System.out.println("What would you like to do today?\n======================");
        System.out.println("1 - Get school's average\n2 - Get male students' average\n3 - Get female students' average\n4 - Get average height of students above 2meters with purple car\n5 - Get first and second level friends of a specific id\n6 - Get percentage of popular students, normal students, and lonely students\n7 - Get average grade for a specific student\n8 - Exit system");
        int input = myObj.nextInt();
        while(true)
        {
            switch (input) {
                case 1 -> DBManagement.SchoolAvg();
                case 2 -> DBManagement.MaleAvg();
                case 3 -> DBManagement.FemaleAvg();
                case 4 -> DBManagement.AvgHeight();
                case 5 -> {
                    Scanner inputobj5 = new Scanner(System.in);
                    System.out.println("write id num");
                    int IDinput5 = inputobj5.nextInt();
                    DBManagement.MyFriends(IDinput5);
                }
                case 6 -> DBManagement.Popularity();
                case 7 -> {
                    Scanner inputobj7 = new Scanner(System.in);
                    System.out.println("write id num");
                    int IDinput7 = inputobj7.nextInt();
                    DBManagement.MyAvg(IDinput7);
                }
                case 8 -> {
                    System.out.println("Goodbye sima!\nplease have mercy on these kids (and have a great day of course)");
                    System.exit(0);
                }
            }
            System.out.println("What else would you like to do?\n====================");
            System.out.println("1 - Get school's average\n2 - Get male students' average\n3 - Get female students' average\n4 - Get average height of students above 2meters with purple car\n5 - Get first and second level friends of a specific id\n6 - Get percentage of popular students, normal students, and lonely students\n7 - Get average grade for a specific student\n8 - Exit system");
            input = myObj.nextInt();
        }

    }
}