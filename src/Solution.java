public class Solution {

    public static void main(String[] args) {
    }

    private static Operation joinChar(char first, char second) {
        if ((first == '=' || second == '=')
                || (first == '<' && second == '<')
                || (first == '>' && second == '>')) {
            if (first == '!' || second == '^' || first == '^' || second == '!')
                return Operation.ERROR;
            else return Operation.EQUALLY;
        }
        if (first == '!' || second == '!') {
            if (first == '>' || second == '>' || (first == '!' && second == '!'))
                return Operation.ERROR;
            else if (second == '<')
                return Operation.MORE;
            else if (first == '<')
                return Operation.LESS;
        }
        if (first == '^' || second == '^') {
            if (first == '<' || second == '<' || (first == '^' && second == '^'))
                return Operation.ERROR;
            else if (second=='>')
                return Operation.LESS;
            else if(first=='>')
                return Operation.MORE;
        }
        if(first=='<'&&second=='>')
            return Operation.LESS_OR_EQUALLY;
        if (first=='>'&&second=='<')
            return Operation.MORE_OR_EQUALLY;
    }

    private enum Operation {
        LESS,
        LESS_OR_EQUALLY,
        EQUALLY,
        MORE_OR_EQUALLY,
        MORE,
        UNKNOWN,
        ERROR
    }
}
