/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.lets.funlagitog;

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
public class Response {

    private String status;

    private String phone;

    private String operator;

    private Integer id;

    @Override
    public String toString() {
        return "Status " + status + " id " + id;
    }

}
