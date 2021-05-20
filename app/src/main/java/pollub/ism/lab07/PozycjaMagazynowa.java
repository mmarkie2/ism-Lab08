package pollub.ism.lab07;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Warzywniak")
public class PozycjaMagazynowa {

    @PrimaryKey(autoGenerate = true)
    public int _id;
    public String NAME;
    public int QUANTITY;
    public int OLDQUANTITY;
    public Date DATE;

    public int get_id() {
        return _id;
    }

    public String getNAME() {
        return NAME;
    }

    public int getQUANTITY() {
        return QUANTITY;
    }

    public int getOLDQUANTITY() {
        return OLDQUANTITY;
    }

    public Date getDATE() {
        return DATE;
    }
}
