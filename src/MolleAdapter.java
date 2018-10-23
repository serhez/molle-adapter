import formulaParsing.FormulaParser;
import formulaParsing.MolleLexicalErrorException;
import formulaParsing.MolleSyntaxErrorException;
import semanticTableau.logics.KModalRules;
import java.io.StringReader;

public class MolleAdapter {

    public boolean proveFormula(String formula) throws MolleUnrecognizedFormulaException {

        FormulaParser formulaParser = new FormulaParser(new StringReader(formula), false);
        formulaTree.Formula parsedFormula;
        try {
            parsedFormula = formulaParser.parse();
        } catch (MolleLexicalErrorException e) {
            throw new MolleUnrecognizedFormulaException(formula);
        } catch (MolleSyntaxErrorException e) {
            throw new MolleUnrecognizedFormulaException(formula);
        }

        semanticTableau.Tableau tableau = new semanticTableau.Tableau(new KModalRules("."), parsedFormula);

        boolean canBeExpanded = true;
        while (canBeExpanded) {
            canBeExpanded = tableau.step();
        }

        return tableau.isClosed();
    }
}