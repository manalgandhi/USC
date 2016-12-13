/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3.fol;

/**
 * Converts FOL sentence to CNF
 *
 * @author manal
 */
public class CNFConverter
{
    public Sentence convert(Sentence folSentence)
    {
        //System.out.println("Inp: " + folSentence.toString());

        Sentence implicationsRemoved = removeImplication(folSentence);
        //System.out.println("ImR: " + implicationsRemoved.toString());

        Sentence negationsMovedInside = checkAndMoveNegationsInside(implicationsRemoved);
        //System.out.println("Neg: " + negationsMovedInside.toString());

        Sentence distributedSentence = distributeAndOverOr(negationsMovedInside);
        //System.out.println("Dis: " + distributedSentence.toString());

        return distributedSentence;
    }

    private Sentence removeImplication(Sentence inputSentence)
    {
        if (inputSentence.isCompound())
        {
            CompoundSentence compundSent = (CompoundSentence) inputSentence;

            Sentence leftSentence = compundSent.getLeftSentence();
            Sentence implicationRemovedLeftSentence = removeImplication(leftSentence);
            Sentence rightSentence = compundSent.getRightSentence();
            Sentence implicationRemovedRightSentence = removeImplication(rightSentence);

            if (compundSent.getConnective().equals(Connective.IMPLICATION))
            {
                NegatedSentence leftNegatedSentence = new NegatedSentence(implicationRemovedLeftSentence);
                CompoundSentence newCompoundSentence = new CompoundSentence(
                        leftNegatedSentence, implicationRemovedRightSentence, Connective.OR);

                return newCompoundSentence;
            }
            else
            {
                CompoundSentence newCompoundSentence = new CompoundSentence(
                        implicationRemovedLeftSentence, implicationRemovedRightSentence, compundSent.getConnective());

                return newCompoundSentence;
            }
        }
        else if (inputSentence instanceof NegatedSentence)
        {
            Sentence implicationsRemoved = removeImplication(((NegatedSentence) inputSentence).getNegatedSentence());
            NegatedSentence negatedSent = new NegatedSentence(implicationsRemoved);

            return negatedSent;
        }
        {
            return inputSentence;
        }
    }

    private Sentence checkAndMoveNegationsInside(Sentence inputSentence)
    {
        if (inputSentence instanceof Predicate)
        {
            return inputSentence;
        }
        else if (inputSentence instanceof NegatedSentence)
        {
            return moveNegationsInside(((NegatedSentence) inputSentence).getNegatedSentence());
        }
        else if (inputSentence instanceof CompoundSentence)
        {
            Sentence leftSentence = ((CompoundSentence) inputSentence).getLeftSentence();
            Sentence rightSentence = ((CompoundSentence) inputSentence).getRightSentence();

            Sentence leftCheckSentence = checkAndMoveNegationsInside(leftSentence);
            Sentence rightCheckSentence = checkAndMoveNegationsInside(rightSentence);

            CompoundSentence newCompoundSentence = new CompoundSentence(
                    leftCheckSentence, rightCheckSentence, ((CompoundSentence) inputSentence).getConnective());
            return newCompoundSentence;
        }

        throw new UnsupportedOperationException("Cannot check and move negation inside for this class: " + inputSentence.getClass().getName());
    }

    //assumes that there was a negation over the input sentence and that this negation has to be moved inside
    private Sentence moveNegationsInside(Sentence inputSentence)
    {
        if (inputSentence instanceof NegatedSentence)
        {
            Sentence nonNegatedSentence = ((NegatedSentence) inputSentence).getNegatedSentence();
            Sentence negationCheckedSentence = checkAndMoveNegationsInside(nonNegatedSentence);

            return negationCheckedSentence;
        }
        else if (inputSentence instanceof Predicate)
        {
            if (((Predicate) inputSentence).isNegated())
            {
                ((Predicate) inputSentence).clearNegation();
            }
            else
            {
                ((Predicate) inputSentence).setNegation();
            }

            return inputSentence;
        }
        else if (inputSentence instanceof CompoundSentence)
        {
            Sentence leftSentence = ((CompoundSentence) inputSentence).getLeftSentence();
            Sentence rightSentence = ((CompoundSentence) inputSentence).getRightSentence();
            Connective connective = ((CompoundSentence) inputSentence).getConnective();

            Sentence negatedLeftSentence = moveNegationsInside(leftSentence);
            Sentence negatedRightSentence = moveNegationsInside(rightSentence);

            Connective newConnective;

            if (connective.equals(Connective.AND))
            {
                newConnective = Connective.OR;
            }
            else if (connective.equals(Connective.OR))
            {
                newConnective = Connective.AND;
            }
            else
            {
                throw new RuntimeException("Connective is not AND or OR");
            }

            return new CompoundSentence(negatedLeftSentence, negatedRightSentence, newConnective);
        }

        throw new UnsupportedOperationException("Cannot move negation inside for this class: " + inputSentence.getClass().getName());
    }

    private Sentence distributeAndOverOr(Sentence inputSentence)
    {
        if (inputSentence instanceof Predicate)
        {
            return inputSentence;
        }
        else if (inputSentence instanceof NegatedSentence)
        {
            if (((NegatedSentence) inputSentence).getNegatedSentence() instanceof Predicate == Boolean.FALSE)
            {
                throw new RuntimeException("Negated sentence does not contain predicate. "
                        + "This should not occur at this stage. NegatedSentence: " + ((NegatedSentence) inputSentence).toString());
            }
        }
        else if (inputSentence instanceof CompoundSentence)
        {
            Sentence leftSentence = ((CompoundSentence) inputSentence).getLeftSentence();
            Sentence rightSentence = ((CompoundSentence) inputSentence).getRightSentence();
            Connective connective = ((CompoundSentence) inputSentence).getConnective();

            if (connective.equals(Connective.AND))
            {
                Sentence leftDistributedSentence = distributeAndOverOr(leftSentence);
                Sentence rightDistributedSentence = distributeAndOverOr(rightSentence);

                CompoundSentence newCompoundSentence = new CompoundSentence(leftDistributedSentence, rightDistributedSentence, connective);
                return newCompoundSentence;
            }
            else if (connective.equals(Connective.OR))
            {
                if (leftSentence instanceof CompoundSentence)
                {
                    if (((CompoundSentence) leftSentence).getConnective().equals(Connective.AND))
                    {
                        Sentence leftLeftSentence = ((CompoundSentence) leftSentence).getLeftSentence();
                        Sentence leftRightSentence = ((CompoundSentence) leftSentence).getRightSentence();

                        Sentence leftLeftAndRightSent = new CompoundSentence(
                                leftLeftSentence, rightSentence, Connective.OR);
                        Sentence leftLeftAndRightDistSent = distributeAndOverOr(leftLeftAndRightSent);

                        Sentence leftRightAndRightSent = new CompoundSentence(
                                leftRightSentence, rightSentence, Connective.OR);
                        Sentence leftRightAndRightDistSent = distributeAndOverOr(leftRightAndRightSent);

                        Sentence distributedInputSentence = new CompoundSentence(
                                leftLeftAndRightDistSent, leftRightAndRightDistSent, Connective.AND);

                        return distributedInputSentence;
                    }
                }

                if (rightSentence instanceof CompoundSentence)
                {
                    if (((CompoundSentence) rightSentence).getConnective().equals(Connective.AND))
                    {
                        Sentence rightLeftSentence = ((CompoundSentence) rightSentence).getLeftSentence();
                        Sentence rightRightSentence = ((CompoundSentence) rightSentence).getRightSentence();

                        Sentence leftAndRightLeftSent = new CompoundSentence(
                                leftSentence, rightLeftSentence, Connective.OR);
                        Sentence leftAndRightLeftDistSent = distributeAndOverOr(leftAndRightLeftSent);

                        Sentence leftAndRightRightSent = new CompoundSentence(
                                leftSentence, rightRightSentence, Connective.OR);
                        Sentence leftAndRightRightDistSent = distributeAndOverOr(leftAndRightRightSent);

                        Sentence distributedInputSentence = new CompoundSentence(
                                leftAndRightLeftDistSent, leftAndRightRightDistSent, Connective.AND);

                        return distributedInputSentence;
                    }
                }
            }
            else
            {
                throw new RuntimeException("Connective is not AND or OR");
            }
        }

        return inputSentence;
    }

}
