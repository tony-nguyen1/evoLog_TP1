package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTVisitorMethodsName extends ASTVisitor {
	@Override
	public boolean visit(MethodDeclaration node) {
	    System.out.println("Method Name: " + node.getName());
	    System.out.println("Return Type: " + node.getReturnType2());

	    // Parcourir les paramètres de la méthode
	    node.parameters().forEach(param -> {
	        System.out.println("Parameter: " + param);
	    });

	    return super.visit(node);
	}
}
