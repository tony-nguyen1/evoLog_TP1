package visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTVisitorSuperClassesName extends ASTVisitor {
	@Override
	public boolean visit(TypeDeclaration node) {
	    if (!node.isInterface()) {
	        System.out.println("Class: " + node.getName());

	        // Récupérer la super-classe immédiate
	        if (node.getSuperclassType() != null) {
	        	Type test = node.getSuperclassType();
	            System.out.println("Superclass: " + node.getSuperclassType());
	            
//	            while(node.getSuperclassType() != null) {
//	            	Type superClasse = node.getSuperclassType();
//	            	System.out.println("Superclass: " + superClasse.getParent());
//	            }
	            
	            this.custom(test);
	        } else {
	            System.out.println("No superclass (probably java.lang.Object)");
	        }
	    }
	    return super.visit(node);
	}
	
//	@Override
//	public void postVisit(ASTNode type) {
//		System.out.println("yo");
//	}
	
	private void custom(Type type) {
		System.out.println("yo");
		
	}
}
