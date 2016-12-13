/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

/**
 *
 * @author manal
 */
public class Counter
{
    private int counterValue;

    public Counter()
    {
        counterValue = 0;
    }
    
    public void increment()
    {
        counterValue++;
    }
    
    public int getValue()
    {
        return counterValue;
    }
}
