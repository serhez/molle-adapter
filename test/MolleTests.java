import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class MolleTests {

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
                Assertions.assertTrue(molleAdapter.proveFormula(formula), ("Molle cannot validate " + formula));
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
                Assertions.assertFalse(molleAdapter.proveFormula(formula), ("Molle cannot invalidate " + formula));
            } catch (MolleUnrecognizedFormulaException e) {
                e.printMessage();
            }
        }
    }

    @Test
    public void crossValidateRandomInputFile() throws InvalidNumberOfPropositionsException, IncompatibleFrameConditionsException {
        InputGenerator inputGenerator = new InputGenerator();
        ModalLogic logic = new ModalLogic("K");
        ArrayList<String> formulas = inputGenerator.generateFormulas(10000, 50, 2);
        crossValidate(formulas, logic);
    }

    @Test
    public void crossValidateInconsistentFormulas() throws IncompatibleFrameConditionsException {

        ModalLogic logic = new ModalLogic("K");

        // Current set of inconsistent formulas
        ArrayList<String> inconsistentFormulas = new ArrayList<>();
        inconsistentFormulas.add("(([](p&q)&q)|([]<>(p&p)|~[]<>(q&p)))");
        inconsistentFormulas.add("(([](q|p)|[](q->p))->(<>[]~(p<->p)-><>[]q))");
        inconsistentFormulas.add("(((p|p)<->p)<->(<>[](q<->p)->~~<>[](p->q)))");
        inconsistentFormulas.add("(~((q->q)&<>[](p&p))|(q|~[]<>~(q|p)))");
        inconsistentFormulas.add("((q|p)->(<><>[](p->q)|[][]([](q<->p)->q)))");
        inconsistentFormulas.add("[]((~<>~<>(q<->q)|(p&p))|<>[]p)");
        inconsistentFormulas.add("[]((<>[][]((p&p)&q)&(q|q))-><>[][](q->p))");
        inconsistentFormulas.add("([]<>(p->p)|(p-><>~(~<>p<-><>(p&q))))");
        inconsistentFormulas.add(" (([]<>~(p->q)->[]<>~q)|~(q->[]~q))");

        crossValidate(inconsistentFormulas, logic);
    }

    void crossValidate(ArrayList<String> formulas, ModalLogic logic) throws IncompatibleFrameConditionsException {

        InputGenerator inputGenerator = new InputGenerator();
        Prover prover = new Prover();
        MolleAdapter molleAdapter = new MolleAdapter();

        System.out.println();
        System.out.println("=================================================");
        System.out.println();

        for (int i=0; i<formulas.size(); i++) {
            try {
                Assertions.assertTrue(prover.proveFormula(formulas.get(i), logic) == molleAdapter.proveFormula(inputGenerator.translateFormulaToMolle(formulas.get(i))), ("The validity of the formula " + formulas.get(i) + " is not consistent. " + i + " formulas have been successfully tested previously."));
                System.out.println("Cross-validated " + (i+1) + " formulas successfully");
            } catch (UnrecognizableFormulaException e) {
                e.printMessage();
            } catch (MolleUnrecognizedFormulaException e) {
                e.printMessage();
            }
        }
    }
}
