package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTVisitorClassName extends ASTVisitor {
	@Override
	public boolean visit(TypeDeclaration node) {
	    if (!node.isInterface()) {
	        System.out.println("Class: " + node.getName());

	        // Récupérer la super-classe immédiate
	        if (node.getSuperclassType() != null) {
	            System.out.println("Superclass: " + node.getSuperclassType());
	        } else {
	            System.out.println("No superclass (probably java.lang.Object)");
	        }
	    }
	    return super.visit(node);
	}


}
