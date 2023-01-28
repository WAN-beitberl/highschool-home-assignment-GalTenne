import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.sql.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class DBManagement {
    public static Connection GetConnection() {
        Connection con = null;
        String DBaddress = "jdbc:mysql://localhost:3306/SchoolInfo";
        String DBusername = "root";
        String DBpassword = "MySQL4galtenne";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DBaddress, DBusername, DBpassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;

    }

    public static void ReadStudentsData(String file) throws SQLException, IOException
    // this function will read the csv file of "highschool" table.
    //and insert its info, after some normalization, into "Schoolinfo" database
    {
        String Input = null; // read info from file will be inserted to here
        Connection con = GetConnection();
        String InsertToStudentsQuery =
                "insert into students(id, first_name, last_name, email, gender, ip_address, cm_height, age, has_car, car_color, grade, grade_avg, identification_card) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = con.prepareStatement(InsertToStudentsQuery);
        // Create an object of BufferedReader class with CSV file as a parameter.
        BufferedReader ReadLine = new BufferedReader(new FileReader(file));

        // now we need to skip the first line - where the col names are.
        ReadLine.readLine();

        // create csvReader object passing file reader as a parameter
        CSVReader csvReader = new CSVReader(ReadLine);

        while ((Input = ReadLine.readLine()) != null) {
            String[] data = Input.split(","); //like strtok() function
            statement.setInt(1, Integer.parseInt(data[0]));
            statement.setString(2, data[1]);
            statement.setString(3, data[2]);
            statement.setString(4, data[3]);
            statement.setString(5, data[4]);
            statement.setString(6, data[5]);
            statement.setInt(7, Integer.parseInt(data[6]));
            statement.setInt(8, Integer.parseInt(data[7]));
            statement.setBoolean(9, Boolean.parseBoolean(data[8]));
            if (data[8].equals("false")) // of doesn't have car
            {
                statement.setString(10, null);
            } else statement.setString(10, data[9]);
            statement.setInt(11, Integer.parseInt(data[10]));
            statement.setFloat(12, Float.parseFloat(data[11]));
            statement.setString(13, data[12]);
            statement.executeUpdate();
        }
        ReadLine.close();
        con.close();
        System.out.println("finished importing data to DB");
    }

    public static void ReadFriendshipsData(String file) throws SQLException, IOException
    // this function will read the csv file of "highschool_friendships" table.
    //and insert its info, after some normalization, into "Schoolinfo" database
    {
        String Input = null; // read info from file will be inserted to here
        String InsertToFriendshipsQuery =
                "insert into Friendships(id, first_name,friend_id, other_friend_id) values (?, ?, ?)";
            Connection con = GetConnection();
            PreparedStatement statement = con.prepareStatement(InsertToFriendshipsQuery);
            // Create an object of BufferedReader class with CSV file as a parameter.
            BufferedReader ReadLine = new BufferedReader(new FileReader(file));

            // now we need to skip the first line - where the col names are.
            ReadLine.readLine();

            // create csvReader object passing file reader as a parameter
            CSVReader csvReader = new CSVReader(ReadLine);

            while ((Input = ReadLine.readLine()) != null) {
                String[] data = Input.split(","); //like strtok() function
                statement.setInt(1, Integer.parseInt(data[0]));
                if (data[1] != null && data[2] != null)
                {
                    statement.setString(2, data[1]);
                    statement.setString(3, data[2]);
                }

                statement.executeUpdate();
            }
            ReadLine.close();
        con.close();
            System.out.println("finished importing data to DB");
        }

    public static void SchoolAvg() throws SQLException {
        String query = "select avg(grade_avg) as SchoolAVG from Students";
        Connection con = GetConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("school average is: " + rs.getFloat("SchoolAVG"));
        con.close();
    }

    public static void MaleAvg() throws SQLException {
        String query = "select avg(grade_avg) as BoysAvg from Students where gender is 'Male'";
        Connection con = GetConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("male average is: " + rs.getFloat("BoysAvg"));
        con.close();
    }

    public static void FemaleAvg() throws SQLException {
        String query = "select sum(grade_avg) as GirlsAvg from Students where gender is 'Female'";
        Connection con = GetConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("female average is: " + rs.getFloat("GirlsAvg"));
        con.close();
    }

    public static void AvgHeight() throws SQLException {
        String query = "select avg(cm_height) as HighAvg from Students where cm_height>200 and car_color is 'Purple'";
        Connection con = GetConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("high guys with purple car average is: " + rs.getFloat("HighAvg"));
        con.close();
    }

    public static void MyFriends(int id) throws SQLException {
        String firstCircle = "select other_friend_id as MyFriends from Friendships where friend_id="+id;
        String secondCircle = "select other_friend_id as TheirFriends from Friendships where friend_id=";
        int chosenFriend = 0;
        String GetSecCycle = "";
// we will need to iterate through the friends and switch the friend each time
        Connection con = GetConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(firstCircle); // now we have table of friends
        while(rs.next()) // get the second cycle
        {
            chosenFriend = rs.getInt("MyFriends");
            GetSecCycle = firstCircle+chosenFriend;
            ResultSet Secrs = stmt.executeQuery(GetSecCycle);
            System.out.println("Friend: "+chosenFriend+" his friend: "+Secrs.getInt("MyFriends"));
        }
        con.close();
    }


    public static void Popularity() throws SQLException
    {
        int chosenId = 0;
        int CountPop = 0, CountNorm = 0, CountLone = 0;
        Connection con = GetConnection();
        String query = "select count(other_friend_id) as popularity from Friendships where friend_id="+chosenId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            query = "select count(other_friend_id) as populars from Friendships where friend_id="+chosenId;
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            if(rs.getInt("popularity") >= 2) CountPop++;
            else if (rs.getInt("popularity") == 1) CountNorm++;
            else CountLone++;

            chosenId++;
        }
        CountPop /= 10; // same as dividing by 1000 (amount of students) and then mul by 100 (percentage)
        CountNorm /=10;
        CountLone /=10;

        System.out.println("Popular students:"+CountPop+"%, Normal Students:"+CountNorm+"%, Lonely Students:"+CountLone+"%");
        con.close();
    }

    public static void MyAvg(int id) throws SQLException
    {
        String query = "select avg(grade_avg) as MyAvg from Students where id="+id;
        Connection con = GetConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("student number "+id+ " average is: " + rs.getFloat("MyAvg"));
        con.close();
    }
}


