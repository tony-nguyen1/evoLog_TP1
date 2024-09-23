package test.testt;

import java.util.ArrayList;
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

import visitor.ASTVisitorAttributsName;
import visitor.ASTVisitorClassName;
import visitor.ASTVisitorSuperClassesName;

import org.eclipse.jdt.core.dom.Modifier;

/**
 * Hello world!
 */
public class SmallerApp {
    public static void main(String[] args) {
        System.out.println("Helllo World!");

   
        @SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(
        		"public class A extends ArrayList<Object> { int i = 9;  \n public int j; \n private ArrayList<Integer> al = new ArrayList<Integer>();j=1000; private boolean isBool(boolean b) { return b; }".toCharArray());
        
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
        
        cu.accept(new ASTVisitorSuperClassesName());
        

        System.out.println("Goodbye World!");
    }
}
