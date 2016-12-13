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
public class VariableTerm implements Term
{
    private String variableTerm;
    private LogicType logicType;

    public VariableTerm(String variableTerm)
    {
        this.variableTerm = variableTerm;
        this.logicType = LogicType.VARIABLE;
    }

    @Override
    public String getTerm()
    {
        return variableTerm;
    }

    @Override
    public LogicType getType()
    {
        return logicType;
    }

    @Override
    public boolean equals(Object term)
    {
        if (term instanceof VariableTerm)
        {
            if (this.variableTerm.equals(((VariableTerm) term).getTerm())
                    && this.logicType.equals(((VariableTerm) term).getType()))
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
        hash = 59 * hash + Objects.hashCode(this.variableTerm);
        hash = 59 * hash + Objects.hashCode(this.logicType);
        return hash;
    }

    @Override
    public String toString()
    {
        return variableTerm;
    }
}
