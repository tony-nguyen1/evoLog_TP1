package test.testt;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import visitor.ASTVisitorClassName;

import org.eclipse.jdt.core.dom.Modifier;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Helllo World!");

   
        @SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(
        		"public class A { int i = 9;  \n public int j; \n private ArrayList<Integer> al = new ArrayList<Integer>();j=1000; private boolean isBool(boolean b) { return b; }".toCharArray());
        
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
		                for (Object fragment : field.fragments()) {
		                    VariableDeclarationFragment variable = (VariableDeclarationFragment) fragment;
		                    System.out.println("Field Name: " + variable.getName());
		                    System.out.println("Field Type: " + field.getType());
		                    
		                 // Récupérer les modificateurs
		                    int modifiers = field.getModifiers();

		                    // Vérifier si le champ est public, private, protected, ou autre
		                    if (Modifier.isPublic(modifiers)) {
		                        System.out.println("Field is public");
		                    } else if (Modifier.isPrivate(modifiers)) {
		                        System.out.println("Field is private");
		                    } else if (Modifier.isProtected(modifiers)) {
		                        System.out.println("Field is protected");
		                    } else {
		                        System.out.println("Field has package-private access (no explicit modifier)");
		                    }
		                }
		            }
		        }
		        return super.visit(node);
		    }
			
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

 
		});
        
//        cu.accept(new ASTVisitorClassName());
        

        System.out.println("Goodbye World!");
    }
}
