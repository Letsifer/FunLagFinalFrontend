package ru.lets.funlagitog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Евгений
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    
    private String name;
    
    private String ipAddress;

    @Override
    public String toString() {
        return name;
    }
    
    
}
