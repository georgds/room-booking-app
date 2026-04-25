import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    Date first, last;

    public Reservation(Date first, Date last){
        this.first = first;
        this.last = last;
    }

    public boolean collision(Reservation res){
        return first.after(res.first) && first.before(res.last) || last.after(res.first) && last.before(res.last) || first.equals(res.first) || first.equals(res.last) || last.equals(res.first) || last.equals(res.last);
    }

}
