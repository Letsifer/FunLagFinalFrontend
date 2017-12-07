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
    
    private Integer id;
    
    private String name;

    @Override
    public String toString() {
        return name;
    }
    
    

}
