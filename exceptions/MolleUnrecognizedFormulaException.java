public class MolleUnrecognizedFormulaException extends Exception {
    public MolleUnrecognizedFormulaException(String formulaString) {
        super("The following formula has not being recognized by Molle: " + formulaString);
    }

    public void printMessage() {
        System.out.println(getMessage());
    }
}
