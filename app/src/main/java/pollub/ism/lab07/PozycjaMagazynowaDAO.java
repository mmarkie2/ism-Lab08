package pollub.ism.lab07;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface PozycjaMagazynowaDAO {

    @Insert  //Automatyczna kwerenda wystarczy
    public void insert(PozycjaMagazynowa pozycja);

    @Update
        //Automatyczna kwerenda wystarczy
    void update(PozycjaMagazynowa pozycja);

    @Query("SELECT QUANTITY FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa ORDER BY DATE DESC LIMIT 1")
        //Nasza kwerenda
    int findQuantityByName(String wybraneWarzywoNazwa);

    @Query("SELECT DATE FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa ORDER BY DATE DESC LIMIT 1")
        //Nasza kwerenda
    Date findDateOfChangeByName(String wybraneWarzywoNazwa);

    @Query("UPDATE Warzywniak SET QUANTITY = :wybraneWarzywoNowaIlosc WHERE NAME= :wybraneWarzywoNazwa")
    void updateQuantityByName(String wybraneWarzywoNazwa, int wybraneWarzywoNowaIlosc);

    @Query("SELECT COUNT(*) FROM Warzywniak")
        //Ile jest rekordów w tabeli
    int size();

    @Query("SELECT * FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa")
        //Nasza kwerenda
    List<PozycjaMagazynowa> findHistoryByName(String wybraneWarzywoNazwa);
}
