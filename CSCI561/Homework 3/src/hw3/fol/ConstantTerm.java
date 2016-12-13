package hw3.fol;

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manal
 */
public class ConstantTerm implements Term
{
    private LogicType logicType;
    private String constantTerm;

    public ConstantTerm(String constantTerm)
    {
        this.constantTerm = constantTerm;
        logicType = LogicType.CONSTANT;
    }

    @Override
    public String getTerm()
    {
        return constantTerm;
    }

    @Override
    public LogicType getType()
    {
        return logicType;
    }

    @Override
    public boolean equals(Object term)
    {
        if (term instanceof ConstantTerm)
        {
            if (this.constantTerm.equals(((ConstantTerm) term).getTerm())
                    && this.logicType.equals(((ConstantTerm) term).getType()))
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
        hash = 97 * hash + Objects.hashCode(this.logicType);
        hash = 97 * hash + Objects.hashCode(this.constantTerm);
        return hash;
    }

    @Override
    public String toString()
    {
        return constantTerm;
    }
}
