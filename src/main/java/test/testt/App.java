package test.testt;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Helllo World!");

   
        @SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(
        		"public class A { int i = 9;  \n int j; \n ArrayList<Integer> al = new ArrayList<Integer>();j=1000; }".toCharArray());
        
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
		cu.accept(new ASTVisitor() {
 
			Set<String> names = new HashSet<String>();
 
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue to avoid usage info
			}
 
			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
				System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
			
			public boolean visit(TypeDeclaration node) {
		        if (node.isInterface()) {
		            System.out.println("Interface: " + node.getName());
		        } else {
		            System.out.println("Class: " + node.getName());
		            for (FieldDeclaration field : node.getFields()) {
		                System.out.println("Field: " + field);
//		                System.out.println(field.);
		            }
		        }
		        return super.visit(node);
		    }
 
		});
        

        System.out.println("Goodbye World!");
    }
}
