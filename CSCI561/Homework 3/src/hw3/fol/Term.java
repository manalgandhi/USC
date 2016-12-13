package hw3.fol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manal
 */
public interface Term
{
    public String getTerm();
    
    public LogicType getType();
    
    @Override
    public boolean equals(Object term);
}
