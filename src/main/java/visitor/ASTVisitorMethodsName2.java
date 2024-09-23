package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTVisitorMethodsName2 extends ASTVisitor {
	public boolean visit(TypeDeclaration node) {
        if (node.isInterface()) {
            System.out.println("Interface: " + node.getName());
        } else {
            System.out.println("Class: " + node.getName());
        }
        return super.visit(node);
    }

}
