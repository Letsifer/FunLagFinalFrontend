package ru.lets.funlagitog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Евгений
 */
@Getter
@Setter
@AllArgsConstructor
public class Operator {
    
    private String name;
    
    private Integer signalLevel;

    @Override
    public String toString() {
        return name;
    }
    
    

}
