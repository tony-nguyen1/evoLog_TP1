package app;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LineCountVisitor extends ASTVisitor {

    private CompilationUnit compilationUnit;
    private int totalLines = 0;
    private int nbMethod = 0;
    private int nbMethodLine = 0;
    private int nbAttr = 0;

    public LineCountVisitor(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
//    	System.out.println(node.getName());
        // Récupérer le nombre de lignes de la classe (ou interface)
        int startLine = compilationUnit.getLineNumber(node.getStartPosition());
        int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
        int classLineCount = endLine - startLine + 1;
        totalLines += classLineCount;
//        System.out.println("Classe: " + node.getName() + " -> " + classLineCount + " lignes.");
//        System.out.println(startLine + " ---> " + endLine);
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
    	this.nbMethod++;
    	
    	// Récupérer le nombre de lignes d'une méthode
		int startLine = compilationUnit.getLineNumber(node.getStartPosition());
		int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
		int methodLineCount = endLine - startLine + 1;
		this.nbMethodLine += methodLineCount;
		  
		  
		return super.visit(node);
    }
    
    public boolean visit(VariableDeclarationFragment node) {
    	this.nbAttr++;
		return false;
	}
    

    public int getTotalLines() {
        return totalLines;
    }
    
    public int getNbMethodLine() {
		return nbMethodLine;
	}

	public int getNbAttr() {
		return nbAttr;
	}
    
    
}
