package app;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassesNumberVisitor extends ASTVisitor {
	List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	
	@Override
	public boolean visit(TypeDeclaration node) {
	    if (!node.isInterface()) { types.add(node); }
	    return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}

}
