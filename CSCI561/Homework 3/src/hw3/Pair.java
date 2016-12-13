/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

import java.util.Objects;

/**
 *
 * @author manal
 */
public class Pair<T, E>
{
    private T first;
    private E second;

    public Pair(T first, E second)
    {
        this.first = first;
        this.second = second;
    }

    public T getFirst()
    {
        return first;
    }

    public E getSecond()
    {
        return second;
    }

    @Override
    public boolean equals(Object pair)
    {
        if (pair instanceof Pair)
        {
            if (first.equals(((Pair) pair).getFirst())
                    && second.equals(((Pair) pair).getSecond()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.first);
        hash = 89 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public String toString()
    {
        return "[ " + first.toString() + ", " + second.toString() + " ]";
    }
}
