package pl.psi;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyChangeListener;

public interface MapObjectIf extends PropertyChangeListener {

    int getCurrentHp();

    boolean checkIfAlive(MapObjectIf defender);

    int getMaxHp();

    void setCurrentHp(int max);

    String getName();

    int getMoveRange();

    int getAmount();
    void setAmount(int a);

    boolean canAttack();
    boolean canHeal();

}
