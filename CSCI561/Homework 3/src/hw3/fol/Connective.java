/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3.fol;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author manal
 */
public enum Connective
{
    AND("&"),
    OR("|"),
    NOT("~"),
    IMPLICATION("=>");
    
    private String stringRepresentation;
    
    private static Map<String, Connective> stringRepVSConnective = new HashMap<>();
    
    static
    {
        for(Connective connective : Connective.values())
        {
            stringRepVSConnective.put(connective.stringRepresentation, connective);
        }
    }
    
    private Connective(final String strRepresentation)
    {
        this.stringRepresentation = strRepresentation;
    }
    
    public static Connective connectiveFor(String str)
    {
        return stringRepVSConnective.get(str);
    }
    
    @Override
    public String toString()
    {
        return stringRepresentation;
    }
}
