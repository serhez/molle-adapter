import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class MolleTest {

    @Test
    public void molleAcceptsValidFormulas() {

        MolleAdapter molleAdapter = new MolleAdapter();
        ArrayList<String> formulas = new ArrayList<>();

        // Examples of valid formulas
        formulas.add("((((Q<=>P)=>P)|Q)|(P<=>Q))"); formulas.add("(Q<=>Q)");
        formulas.add("(((Q<=>P)<=>P)|((Q|Q)<=>Q))"); formulas.add("((P&Q)=>([]<>P|P))");
        formulas.add("(~P|(P|(P&Q)))"); formulas.add("[]((((P|~(Q<=>Q))=>P)|(Q<=>P))|Q)");
        formulas.add("([](Q<=>~~(Q|(Q&Q)))|([]Q&Q))"); formulas.add("((Q=>Q)=>(Q<=>(Q|Q)))");
        formulas.add("[][](Q<=>((Q=>Q)=>Q))"); formulas.add("(((P&(P<=>P))|(Q=>Q))|((P|P)|Q))");
        formulas.add("((<>(P|P)&Q)=>(P=>(Q=>Q)))"); formulas.add("((P<=>P)=>(Q|(P|(P=>Q))))");
        formulas.add("(<>P=>(P<=>P))"); formulas.add("(~((Q|Q)=>P)|(~~(Q|[]<>~~P)|(Q=>Q)))");
        formulas.add("(~(([]Q<=>[](P=>Q))<=>Q)=>(Q<=>Q))"); formulas.add("[]((Q=>P)|Q)");

        for (String formula : formulas) {
            try {
                Assertions.assertTrue(molleAdapter.proveFormula(formula) == true, ("Molle cannot validate " + formula));
            } catch (MolleUnrecognizedFormulaException e) {
                e.printMessage();
            }
        }
    }

    @Test
    public void molleRejectsInvalidFormulas() {

        MolleAdapter molleAdapter = new MolleAdapter();
        ArrayList<String> formulas = new ArrayList<>();

        // Examples of invalid formulas
        formulas.add("((P<=>P)&P)"); formulas.add("<>(<>P&(Q=>Q))");
        formulas.add("([](Q<=>(P<=>Q))=>((P&<>(P|Q))<=>Q))"); formulas.add("<>[]P");
        formulas.add("~((<>[]~Q|(P|(Q&(Q=>Q))))<=>Q)"); formulas.add("([]~[][](Q&P)=>P)");
        formulas.add("(P&[](<>(~(P&Q)=>Q)<=>(P&Q)))"); formulas.add("((<>Q<=>Q)|[](Q|([]<>P&Q)))");
        formulas.add("<>((P|Q)<=>((<>[](P<=>(P|Q))|Q)=>P))"); formulas.add("[]Q");
        formulas.add("<>Q"); formulas.add("((~(<>(Q<=>Q)<=>Q)|P)<=>Q)");
        formulas.add("~~~~<>Q"); formulas.add("(<>(<>Q&Q)&(Q|~<>(P<=>~[]Q)))");
        formulas.add("([]Q&((((Q=>Q)&P)<=><>(Q|Q))|Q))"); formulas.add("<>((Q<=>((P=>Q)&Q))<=>P)");

        for (String formula : formulas) {
            try {
                Assertions.assertTrue(molleAdapter.proveFormula(formula) == false, ("Molle cannot invalidate " + formula));
            } catch (MolleUnrecognizedFormulaException e) {
                e.printMessage();
            }
        }
    }

    @Test
    public void crossValidateRandomInputFile() {

        InputGenerator inputGenerator = new InputGenerator();
        Prover prover = new Prover(false);
        MolleAdapter molleAdapter = new MolleAdapter();
        ModalSystem system = new ModalSystem("K");

        ArrayList<String> formulas = inputGenerator.generateFormulas(10000, 50, "K");

        for (int i=0; i<formulas.size(); i++) {
            try {
                Assertions.assertTrue(prover.proveFormula(formulas.get(i), system) == molleAdapter.proveFormula(inputGenerator.translateFormulaToMolle(formulas.get(i))), ("The validity of the formula " + formulas.get(i) + " is not consistent. " + i + " formulas have been successfully tested previously."));
            } catch (UnrecognizableFormulaException e) {
                e.printMessage();
            } catch (MolleUnrecognizedFormulaException e) {
                e.printMessage();
            }
        }
    }
}
