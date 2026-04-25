import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Room implements Serializable {
    String roomName, area, roomImage;
    int roomID, noOfPersons, noOfReviews, price;
    double stars;
    Date startd, lastd;
    ArrayList<Reservation> list = new ArrayList<Reservation>();

    public Room(int roomID, String roomName, int noOfPersons, String area, double stars, int noOfReviews, int price, String roomImage){
        this.roomID = roomID;
        this.roomName = roomName;
        this.noOfPersons = noOfPersons;
        this.area = area;
        this.stars = stars;
        this.noOfReviews = noOfReviews;
        this.price = price;
        this.roomImage = roomImage;
    }

    public void InitializeDateRange(String firstd, String secondd){
        startd = DateFormat(firstd);
        lastd = DateFormat(secondd);
    }

    public boolean isBooked(Reservation reservation){
        boolean flag;
        for (Reservation i : list) {
            flag = reservation.collision(i);
            if (flag){
                return true;
            }
        }
        return false;
    }

    public boolean reserve(String date1, String date2){
        Date first = DateFormat(date1);
        Date second = DateFormat(date2);
        if (startd != null && lastd != null) {
            if ((first.after(startd) || first.equals(startd)) && first.before(lastd) && second.after(startd) && (second.before(lastd) || second.equals(lastd))) {
                Reservation reservation = new Reservation(first, second);
                if (isBooked(reservation)) {
                    return false;
                }
                list.add(reservation);
                return true;
            }
        }
        return false;
    }

    public boolean available(String date1, String date2){
        boolean flag = false;
        Date first = DateFormat(date1);
        Date second = DateFormat(date2);
        Reservation reservation = new Reservation(first, second);
        flag = !isBooked(reservation);
        return flag;
    }
    public Date DateFormat(String sdate){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        try {
            return formatter.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getRoomID(){
        return roomID;
    }

    public void setRoomID(int roomID){
        this.roomID = roomID;
    }
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int noOfPersons) {
        this.noOfPersons = noOfPersons;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public int getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(int noOfReviews) {
        this.noOfReviews = noOfReviews;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public ArrayList<Reservation> getReservations(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList totalReservations = new ArrayList();

        for (Reservation reservation : list){
            String dateString = "(" + dateFormat.format(reservation.first) + ", " + dateFormat.format(reservation.last) + ")";
            totalReservations.add(dateString);
        }

        return totalReservations;
    }
}

